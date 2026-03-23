import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { ActivatedRoute, Router, RouterLink } from '@angular/router';
import { ApiService } from '../../services/api.service';
import { BookListItem } from '../../models/library.models';
import { LetterFilterComponent } from '../../components/letter-filter/letter-filter.component';
import { PageResponse } from '../../models/page-response';

@Component({
  selector: 'app-books-page',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterLink, LetterFilterComponent],
  templateUrl: './books.page.html',
  styleUrl: './books.page.css',
})
export class BooksPage {
  searchQuery = '';
  activeLetter = 'ALL';

  page = 1;
  readonly size = 20;

  loading = false;
  error: string | null = null;
  data: PageResponse<BookListItem> | null = null;

  constructor(
    private readonly api: ApiService,
    private readonly route: ActivatedRoute,
    private readonly router: Router,
  ) {}

  ngOnInit() {
    this.route.queryParamMap.subscribe((params) => {
      const pageParam = Number(params.get('page'));
      this.page = Number.isFinite(pageParam) && pageParam > 0 ? pageParam : 1;

      const letterParam = (params.get('letter') ?? 'ALL').toUpperCase();
      this.activeLetter = /^[A-Z]$/.test(letterParam) ? letterParam : 'ALL';

      const qParam = (params.get('q') ?? '').trim();
      this.searchQuery = qParam;

      this.load();
    });
  }

  applySearch() {
    const q = this.searchQuery.trim();
    if (!q) {
      this.activeLetter = 'ALL';
    } else {
      this.activeLetter = 'ALL';
    }
    this.page = 1;
    void this.syncUrlState();
  }

  onLetterSelected(letter: string) {
    this.activeLetter = letter;
    this.searchQuery = '';
    this.page = 1;
    void this.syncUrlState();
  }

  changePage(nextPage: number) {
    if (nextPage < 1) return;
    if (this.data && nextPage > this.data.totalPages) return;
    this.page = nextPage;
    void this.syncUrlState();
  }

  private syncUrlState() {
    return this.router.navigate([], {
      relativeTo: this.route,
      queryParams: {
        page: this.page > 1 ? this.page : null,
        letter: this.activeLetter !== 'ALL' ? this.activeLetter : null,
        q: this.searchQuery.trim() || null,
      },
      replaceUrl: true,
    });
  }

  private load() {
    this.loading = true;
    this.error = null;

    const q = this.searchQuery.trim();
    let request$;

    if (q) {
      request$ = this.api.searchBooks(q, this.page, this.size);
    } else if (this.activeLetter !== 'ALL') {
      request$ = this.api.listBooksByTitlePrefix(this.activeLetter, this.page, this.size);
    } else {
      request$ = this.api.listBooks(this.page, this.size);
    }

    request$.subscribe({
      next: (res) => {
        this.data = res;
        this.loading = false;
      },
      error: (err) => {
        this.loading = false;
        this.error = err?.error?.message ?? 'Error cargando libros';
      },
    });
  }
}

