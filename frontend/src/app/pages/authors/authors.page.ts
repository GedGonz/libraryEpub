import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
import { RouterLink } from '@angular/router';
import { ApiService } from '../../services/api.service';
import { Author } from '../../models/library.models';
import { PageResponse } from '../../models/page-response';
import { LetterFilterComponent } from '../../components/letter-filter/letter-filter.component';

@Component({
  selector: 'app-authors-page',
  standalone: true,
  imports: [CommonModule, RouterLink, LetterFilterComponent],
  templateUrl: './authors.page.html',
  styleUrl: './authors.page.css',
})
export class AuthorsPage {
  activeLetter = 'ALL';
  page = 1;
  readonly size = 20;

  loading = false;
  error: string | null = null;
  data: PageResponse<Author> | null = null;

  constructor(private readonly api: ApiService) {}

  ngOnInit() {
    this.load();
  }

  onLetterSelected(letter: string) {
    this.activeLetter = letter;
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

    const request$ =
      this.activeLetter === 'ALL'
        ? this.api.listAuthors(this.page, this.size)
        : this.api.searchAuthorsByPrefix(this.activeLetter, this.page, this.size);

    request$.subscribe({
      next: (res) => {
        this.data = res;
        this.loading = false;
      },
      error: (err) => {
        this.loading = false;
        this.error = err?.error?.message ?? 'Error cargando autores';
      },
    });
  }
}

