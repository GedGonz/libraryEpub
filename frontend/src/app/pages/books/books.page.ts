import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { RouterLink } from '@angular/router';
import { ActivatedRoute } from '@angular/router';
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

  constructor(private readonly api: ApiService) {}

  ngOnInit() {
    this.load();
  }

  applySearch() {
    const q = this.searchQuery.trim();
    if (!q) {
      this.activeLetter = 'ALL';
    } else {
      this.activeLetter = 'ALL';
    }
    this.page = 1;
    this.load();
  }

  onLetterSelected(letter: string) {
    this.activeLetter = letter;
    this.searchQuery = '';
    this.page = 1;
    this.load();
  }

  changePage(nextPage: number) {
    if (nextPage < 1) return;
    if (this.data && nextPage > this.data.totalPages) return;
    this.page = nextPage;
    this.load();
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

