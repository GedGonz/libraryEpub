import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
import { ActivatedRoute, Router, RouterLink } from '@angular/router';
import { ApiService } from '../../services/api.service';
import { Label } from '../../models/library.models';
import { PageResponse } from '../../models/page-response';
import { LetterFilterComponent } from '../../components/letter-filter/letter-filter.component';

@Component({
  selector: 'app-labels-page',
  standalone: true,
  imports: [CommonModule, RouterLink, LetterFilterComponent],
  templateUrl: './labels.page.html',
  styleUrl: './labels.page.css',
})
export class LabelsPage {
  activeLetter = 'ALL';
  page = 1;
  readonly size = 20;

  loading = false;
  error: string | null = null;
  data: PageResponse<Label> | null = null;

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

      this.load();
    });
  }

  onLetterSelected(letter: string) {
    this.activeLetter = letter;
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
      },
      replaceUrl: true,
    });
  }

  private load() {
    this.loading = true;
    this.error = null;

    const request$ =
      this.activeLetter === 'ALL'
        ? this.api.listLabels(this.page, this.size)
        : this.api.searchLabelsByPrefix(this.activeLetter, this.page, this.size);

    request$.subscribe({
      next: (res) => {
        this.data = res;
        this.loading = false;
      },
      error: (err) => {
        this.loading = false;
        this.error = err?.error?.message ?? 'Error cargando temáticas';
      },
    });
  }
}

