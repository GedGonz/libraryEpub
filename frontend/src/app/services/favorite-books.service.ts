import { Injectable } from '@angular/core';
import { BehaviorSubject } from 'rxjs';
import { BookListItem } from '../models/library.models';
import { ApiService } from './api.service';

export type FavoriteBook = BookListItem;

@Injectable({ providedIn: 'root' })
export class FavoriteBooksService {
  private readonly favoritesSubject = new BehaviorSubject<FavoriteBook[]>([]);
  readonly favorites$ = this.favoritesSubject.asObservable();

  constructor(private readonly api: ApiService) {
    this.refresh();
  }

  isFavorite(bookId: number): boolean {
    return this.favoritesSubject.value.some((item) => item.bookId === bookId);
  }

  toggleFavorite(book: BookListItem) {
    if (this.isFavorite(book.bookId)) {
      this.removeFavorite(book.bookId, false);
      return;
    }
    this.addFavorite(book, false);
  }

  addFavorite(book: BookListItem, silentRefresh = true) {
    const current = this.favoritesSubject.value.filter((item) => item.bookId !== book.bookId);
    const updated = [book, ...current];
    this.favoritesSubject.next(updated);

    this.api.addFavoriteBook(book.bookId).subscribe({
      next: () => {
        if (!silentRefresh) this.refresh();
      },
      error: () => this.refresh(),
    });
  }

  removeFavorite(bookId: number, silentRefresh = true) {
    const updated = this.favoritesSubject.value.filter((item) => item.bookId !== bookId);
    this.favoritesSubject.next(updated);

    this.api.removeFavoriteBook(bookId).subscribe({
      next: () => {
        if (!silentRefresh) this.refresh();
      },
      error: () => this.refresh(),
    });
  }

  refresh() {
    this.api.listFavoriteBooks().subscribe({
      next: (favorites) => this.favoritesSubject.next(favorites),
      error: () => this.favoritesSubject.next([]),
    });
  }
}

