import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { PageResponse } from '../models/page-response';
import { Author, BookDetail, BookListItem, Label } from '../models/library.models';

@Injectable({ providedIn: 'root' })
export class ApiService {
  private readonly http = inject(HttpClient);

  listBooks(page: number, size: number): Observable<PageResponse<BookListItem>> {
    return this.http.get<PageResponse<BookListItem>>('/api/books', {
      params: new HttpParams().set('page', String(page)).set('size', String(size)),
    });
  }

  searchBooks(q: string, page: number, size: number): Observable<PageResponse<BookListItem>> {
    return this.http.get<PageResponse<BookListItem>>('/api/books/search', {
      params: new HttpParams()
        .set('q', q)
        .set('page', String(page))
        .set('size', String(size)),
    });
  }

  listBooksByTitlePrefix(prefix: string, page: number, size: number): Observable<PageResponse<BookListItem>> {
    return this.http.get<PageResponse<BookListItem>>(`/api/books/by-title-prefix/${encodeURIComponent(prefix)}`, {
      params: new HttpParams().set('page', String(page)).set('size', String(size)),
    });
  }

  listBooksByAuthor(authorId: number, prefix: string | null, page: number, size: number): Observable<PageResponse<BookListItem>> {
    let params = new HttpParams().set('page', String(page)).set('size', String(size));
    if (prefix !== null) {
      params = params.set('prefix', prefix);
    }
    return this.http.get<PageResponse<BookListItem>>(`/api/books/by-author/${authorId}`, { params });
  }

  listBooksByLabel(labelId: number, prefix: string | null, page: number, size: number): Observable<PageResponse<BookListItem>> {
    let params = new HttpParams().set('page', String(page)).set('size', String(size));
    if (prefix !== null) {
      params = params.set('prefix', prefix);
    }
    return this.http.get<PageResponse<BookListItem>>(`/api/books/by-label/${labelId}`, { params });
  }

  getBookById(bookId: number): Observable<BookDetail> {
    return this.http.get<BookDetail>(`/api/books/${bookId}`);
  }

  getBookFileBlob(bookId: number): Observable<Blob> {
    return this.http.get(`/api/books/${bookId}/file`, { responseType: 'blob' });
  }

  getBookFileArrayBuffer(bookId: number): Observable<ArrayBuffer> {
    return this.http.get(`/api/books/${bookId}/file`, { responseType: 'arraybuffer' });
  }

  listAuthors(page: number, size: number): Observable<PageResponse<Author>> {
    return this.http.get<PageResponse<Author>>('/api/authors', {
      params: new HttpParams().set('page', String(page)).set('size', String(size)),
    });
  }

  searchAuthorsByPrefix(prefix: string, page: number, size: number): Observable<PageResponse<Author>> {
    return this.http.get<PageResponse<Author>>('/api/authors/search', {
      params: new HttpParams().set('prefix', prefix).set('page', String(page)).set('size', String(size)),
    });
  }

  getAuthorById(authorId: number): Observable<Author> {
    return this.http.get<Author>(`/api/authors/${authorId}`);
  }

  listLabels(page: number, size: number): Observable<PageResponse<Label>> {
    return this.http.get<PageResponse<Label>>('/api/labels', {
      params: new HttpParams().set('page', String(page)).set('size', String(size)),
    });
  }

  searchLabelsByPrefix(prefix: string, page: number, size: number): Observable<PageResponse<Label>> {
    return this.http.get<PageResponse<Label>>('/api/labels/search', {
      params: new HttpParams().set('prefix', prefix).set('page', String(page)).set('size', String(size)),
    });
  }

  getLabelById(labelId: number): Observable<Label> {
    return this.http.get<Label>(`/api/labels/${labelId}`);
  }
}

