import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
import { ActivatedRoute, RouterLink } from '@angular/router';
import { ApiService } from '../../services/api.service';
import { BookListItem } from '../../models/library.models';
import { PageResponse } from '../../models/page-response';
import { LetterFilterComponent } from '../../components/letter-filter/letter-filter.component';
import { FavoriteBooksService } from '../../services/favorite-books.service';

@Component({
  selector: 'app-label-detail-page',
  standalone: true,
  imports: [CommonModule, RouterLink, LetterFilterComponent],
  templateUrl: './label-detail.page.html',
  styleUrl: './label-detail.page.css',
})
export class LabelDetailPage {
  readonly size = 20;
  activeLetter = 'ALL';
  page = 1;

  labelName: string | null = null;

  loading = false;
  error: string | null = null;
  books: PageResponse<BookListItem> | null = null;

  private labelId: number | null = null;

  constructor(
    private readonly api: ApiService,
    private readonly route: ActivatedRoute,
    private readonly favoriteBooks: FavoriteBooksService,
  ) {}

  ngOnInit() {
    this.labelId = Number(this.route.snapshot.paramMap.get('labelId'));
    if (!this.labelId || Number.isNaN(this.labelId)) {
      this.error = 'Temática inválida';
      return;
    }
    this.load();
  }

  onLetterSelected(letter: string) {
    this.activeLetter = letter;
    this.page = 1;
    this.load();
  }

  changePage(nextPage: number) {
    if (nextPage < 1) return;
    if (this.books && nextPage > this.books.totalPages) return;
    this.page = nextPage;
    this.load();
  }

  isFavorite(bookId: number): boolean {
    return this.favoriteBooks.isFavorite(bookId);
  }

  toggleFavorite(book: BookListItem, event: Event) {
    event.preventDefault();
    event.stopPropagation();
    this.favoriteBooks.toggleFavorite(book);
  }

  private load() {
    if (!this.labelId) return;
    this.loading = true;
    this.error = null;

    this.api.getLabelById(this.labelId).subscribe({
      next: (label) => {
        this.labelName = label.name;
      },
      error: (err) => {
        this.loading = false;
        this.error = err?.error?.message ?? 'Error cargando temática';
      },
    });

    const prefix: string | null = this.activeLetter === 'ALL' ? null : this.activeLetter;
    this.api.listBooksByLabel(this.labelId, prefix, this.page, this.size).subscribe({
      next: (res) => {
        this.books = res;
        this.loading = false;
      },
      error: (err) => {
        this.loading = false;
        this.error = err?.error?.message ?? 'Error cargando libros';
      },
    });
  }
}

