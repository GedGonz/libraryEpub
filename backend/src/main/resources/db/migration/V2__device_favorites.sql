CREATE TABLE device_favorites (
    client_id VARCHAR(64) NOT NULL,
    book_id INTEGER NOT NULL REFERENCES books(book_id) ON DELETE CASCADE,
    created_at TIMESTAMP NOT NULL DEFAULT NOW(),
    PRIMARY KEY (client_id, book_id)
);

CREATE INDEX idx_device_favorites_client_created
    ON device_favorites (client_id, created_at DESC);

