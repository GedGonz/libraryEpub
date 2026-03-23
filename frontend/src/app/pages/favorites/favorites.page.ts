import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
import { RouterLink } from '@angular/router';
import { FavoriteBook, FavoriteBooksService } from '../../services/favorite-books.service';

@Component({
  selector: 'app-favorites-page',
  standalone: true,
  imports: [CommonModule, RouterLink],
  templateUrl: './favorites.page.html',
  styleUrl: './favorites.page.css',
})
export class FavoritesPage {
  favorites: FavoriteBook[] = [];

  constructor(private readonly favoriteBooks: FavoriteBooksService) {}

  ngOnInit() {
    this.favoriteBooks.favorites$.subscribe((items) => {
      this.favorites = items;
    });
  }

  remove(bookId: number, event?: Event) {
    event?.preventDefault();
    event?.stopPropagation();
    this.favoriteBooks.removeFavorite(bookId);
  }
}

