import { CommonModule } from '@angular/common';
import { ChangeDetectorRef, Component } from '@angular/core';
import { ActivatedRoute, RouterLink } from '@angular/router';
import { ApiService } from '../../services/api.service';
import { BookDetail } from '../../models/library.models';
import { firstValueFrom } from 'rxjs';
import ePub from 'epubjs';

@Component({
  selector: 'app-book-detail-page',
  standalone: true,
  imports: [CommonModule, RouterLink],
  templateUrl: './book-detail.page.html',
  styleUrl: './book-detail.page.css',
})
export class BookDetailPage {
  bookId!: number;

  loading = false;
  error: string | null = null;
  book: BookDetail | null = null;

  viewerOpen = false;

  authorNames: string[] = [];
  labelNames: string[] = [];

  private epubBook: any = null;
  private rendition: any = null;
  private isLoadingViewer = false;

  constructor(
    private readonly api: ApiService,
    private readonly route: ActivatedRoute,
    private readonly cdr: ChangeDetectorRef,
  ) {}

  ngOnInit() {
    this.bookId = Number(this.route.snapshot.paramMap.get('bookId'));
    if (!this.bookId || Number.isNaN(this.bookId)) {
      this.error = 'Libro inválido';
      return;
    }
    this.load();
  }

  private load() {
    this.loading = true;
    this.error = null;

    this.api.getBookById(this.bookId).subscribe({
      next: (res) => {
        this.book = res;
        this.loading = false;
        void this.enrichRelatedNames();
      },
      error: (err) => {
        this.loading = false;
        this.error = err?.error?.message ?? 'Error cargando libro';
      },
    });
  }

  private async enrichRelatedNames() {
    if (!this.book) return;

    const authorIds = this.book.authorIds ?? [];
    const labelIds = this.book.labelIds ?? [];

    const [authorsRes, labelsRes] = await Promise.all([
      Promise.allSettled(authorIds.map((id) => firstValueFrom(this.api.getAuthorById(id)))),
      Promise.allSettled(labelIds.map((id) => firstValueFrom(this.api.getLabelById(id)))),
    ]);

    this.authorNames = Array.from(
      new Set(
        authorsRes
          .filter((r): r is PromiseFulfilledResult<{ authorId: number; name: string }> => r.status === 'fulfilled')
          .map((r) => r.value.name),
      ),
    );
    this.labelNames = Array.from(
      new Set(
        labelsRes
          .filter((r): r is PromiseFulfilledResult<{ labelId: number; name: string }> => r.status === 'fulfilled')
          .map((r) => r.value.name),
      ),
    );

    // Limita a lo visual (pills) para no saturar.
    this.authorNames = this.authorNames.slice(0, 4);
    this.labelNames = this.labelNames.slice(0, 6);

    // Fuerza refresco de UI cuando las promesas terminan fuera de zona.
    this.cdr.detectChanges();
  }

  get epubUrl(): string {
    return `/api/books/${this.bookId}/file`;
  }

  openViewer() {
    this.viewerOpen = true;

    // Esperamos a que el modal se renderice en el DOM (por el *ngIf).
    setTimeout(() => {
      void this.loadAndRenderViewer();
    }, 0);
  }

  closeViewer() {
    this.viewerOpen = false;

    this.destroyViewer();
  }

  prevPage() {
    if (this.rendition) this.rendition.prev();
  }

  nextPage() {
    if (this.rendition) this.rendition.next();
  }

  private async loadAndRenderViewer() {
    if (!this.bookId) return;
    if (this.isLoadingViewer) return;

    this.isLoadingViewer = true;

    try {
      this.destroyViewer();

      const viewerEl = document.getElementById('viewer');
      const pageInfoEl = document.getElementById('page-info');
      if (viewerEl) viewerEl.innerHTML = 'Cargando contenido...';
      if (pageInfoEl) pageInfoEl.textContent = 'Calculando páginas...';

      const data = await firstValueFrom(this.api.getBookFileArrayBuffer(this.bookId));

      // Inicializamos el libro EPUB con el binario descargado.
      this.epubBook = ePub(data);

      await this.epubBook.ready;
      // Genera el mapeo CFI -> "ubicación" para poder calcular página actual/total.
      await this.epubBook.locations.generate(750);

      // Aseguramos que no quede el texto de carga encima del render.
      if (viewerEl) viewerEl.innerHTML = '';

      this.rendition = this.epubBook.renderTo('viewer', {
        width: '100%',
        height: '100%',
        flow: 'paginated',
        manager: 'default',
      });

      this.rendition.on('relocated', () => {
        this.updatePageInfo();
      });

      await this.rendition.display();
      this.updatePageInfo();
    } catch (err) {
      const viewerEl = document.getElementById('viewer');
      if (viewerEl) viewerEl.innerHTML = 'Error al abrir el libro';
      console.error(err);
    } finally {
      this.isLoadingViewer = false;
    }
  }

  private updatePageInfo() {
    try {
      if (!this.rendition || !this.epubBook || !this.epubBook.locations) return;
      const currentLocation = this.rendition.currentLocation();
      if (!currentLocation?.start?.cfi) return;

      const index = this.epubBook.locations.locationFromCfi(currentLocation.start.cfi);
      const currentPage = index + 1;
      const totalPages = this.epubBook.locations.total;

      const el = document.getElementById('page-info');
      if (el) el.textContent = `Página ${currentPage} de ${totalPages}`;
    } catch (err) {
      // No rompemos la UI si el visor todavía está inicializando.
    }
  }

  private destroyViewer() {
    try {
      if (this.rendition) {
        this.rendition.destroy?.();
      }
      if (this.epubBook) {
        this.epubBook.destroy?.();
      }
    } finally {
      this.rendition = null;
      this.epubBook = null;
    }
  }
}

