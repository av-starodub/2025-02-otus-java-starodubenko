DROP TABLE IF EXISTS clients CASCADE;
DROP TABLE IF EXISTS products CASCADE;
DROP TABLE IF EXISTS purchases CASCADE;

CREATE TABLE clients
(
    id   bigint       NOT NULL PRIMARY KEY,
    name VARCHAR(100) NOT NULL
);

CREATE TABLE products
(
    id    bigint         NOT NULL PRIMARY KEY,
    name  VARCHAR(100)   NOT NULL,
    price DECIMAL(10, 2) NOT NULL
);

CREATE TABLE purchases
(
    id         BIGINT         NOT NULL PRIMARY KEY,
    client_id  BIGINT         NOT NULL,
    product_id BIGINT         NOT NULL,
    created_at TIMESTAMP      NOT NULL,
    price      DECIMAL(10, 2) NOT NULL,
    FOREIGN KEY (client_id) REFERENCES clients (id),
    FOREIGN KEY (product_id) REFERENCES products (id)
);

CREATE INDEX idx_client_id ON purchases (client_id);

CREATE INDEX idx_product_id ON purchases (product_id);
