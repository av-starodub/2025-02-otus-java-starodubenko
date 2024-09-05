DROP TABLE IF EXISTS products CASCADE;

CREATE TABLE products
(
    id    bigint NOT NULL PRIMARY KEY,
    name  VARCHAR(100)   NOT NULL,
    price DECIMAL(10, 2) NOT NULL
);
