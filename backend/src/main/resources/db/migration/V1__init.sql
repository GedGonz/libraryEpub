-- Base schema for libraryEPubApi.
-- Keep column names aligned with JPA @Column mappings.

CREATE TABLE books (
    book_id INTEGER PRIMARY KEY,
    title TEXT NOT NULL,
    description TEXT,
    published_year INTEGER,
    page_count INTEGER,
    sha256sum CHAR(64) UNIQUE,
    file_size BIGINT,
    file_name TEXT
);

CREATE TABLE authors (
    author_id SERIAL PRIMARY KEY,
    name TEXT UNIQUE NOT NULL
);

CREATE TABLE labels (
    label_id SERIAL PRIMARY KEY,
    name TEXT UNIQUE NOT NULL
);

CREATE TABLE book_authors (
    book_id INTEGER REFERENCES books(book_id),
    author_id INTEGER REFERENCES authors(author_id),
    PRIMARY KEY (book_id, author_id)
);

CREATE TABLE book_labels (
    book_id INTEGER REFERENCES books(book_id),
    label_id INTEGER REFERENCES labels(label_id),
    PRIMARY KEY (book_id, label_id)
);

