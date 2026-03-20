import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
import { ActivatedRoute, RouterLink } from '@angular/router';
import { ApiService } from '../../services/api.service';
import { BookListItem } from '../../models/library.models';
import { PageResponse } from '../../models/page-response';
import { LetterFilterComponent } from '../../components/letter-filter/letter-filter.component';

@Component({
  selector: 'app-author-detail-page',
  standalone: true,
  imports: [CommonModule, RouterLink, LetterFilterComponent],
  templateUrl: './author-detail.page.html',
  styleUrl: './author-detail.page.css',
})
export class AuthorDetailPage {
  readonly size = 20;
  activeLetter = 'ALL';
  page = 1;

  authorName: string | null = null;

  loading = false;
  error: string | null = null;
  books: PageResponse<BookListItem> | null = null;

  private authorId: number | null = null;

  constructor(private readonly api: ApiService, private readonly route: ActivatedRoute) {}

  ngOnInit() {
    this.authorId = Number(this.route.snapshot.paramMap.get('authorId'));
    if (!this.authorId || Number.isNaN(this.authorId)) {
      this.error = 'Autor inválido';
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

  private load() {
    if (!this.authorId) return;
    this.loading = true;
    this.error = null;

    this.api.getAuthorById(this.authorId).subscribe({
      next: (author) => {
        this.authorName = author.name;
      },
      error: (err) => {
        this.loading = false;
        this.error = err?.error?.message ?? 'Error cargando autor';
      },
    });

    const prefix: string | null = this.activeLetter === 'ALL' ? null : this.activeLetter;
    this.api.listBooksByAuthor(this.authorId, prefix, this.page, this.size).subscribe({
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

