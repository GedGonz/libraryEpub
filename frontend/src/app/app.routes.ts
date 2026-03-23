import { Routes } from '@angular/router';
import { BooksPage } from './pages/books/books.page';
import { AuthorsPage } from './pages/authors/authors.page';
import { AuthorDetailPage } from './pages/authors/author-detail.page';
import { LabelsPage } from './pages/labels/labels.page';
import { LabelDetailPage } from './pages/labels/label-detail.page';
import { BookDetailPage } from './pages/books/book-detail.page';
import { FavoritesPage } from './pages/favorites/favorites.page';

export const routes: Routes = [
  { path: '', redirectTo: 'books', pathMatch: 'full' },
  { path: 'books', component: BooksPage },
  { path: 'books/:bookId', component: BookDetailPage },
  { path: 'authors', component: AuthorsPage },
  { path: 'authors/:authorId', component: AuthorDetailPage },
  { path: 'labels', component: LabelsPage },
  { path: 'labels/:labelId', component: LabelDetailPage },
  { path: 'favorites', component: FavoritesPage },
];
