export interface BookListItem {
  bookId: number;
  title: string;
  description: string;
  publishedYear: number;
  pageCount: number;
  sha256sum: string;
  fileSize: number;
  fileName: string;
  authorNames?: string[];
}

export interface BookDetail extends BookListItem {
  authorIds: number[];
  labelIds: number[];
}

export interface Author {
  authorId: number;
  name: string;
}

export interface Label {
  labelId: number;
  name: string;
}

