CREATE SEQUENCE global_seq START WITH 100000;

CREATE TABLE address
(
    id     bigint       NOT NULL PRIMARY KEY,
    street varchar(120) NOT NULL

);

CREATE TABLE client
(
    id   bigint      NOT NULL PRIMARY KEY,
    name varchar(50) NOT NULL,
    address_id bigint REFERENCES address (id) ON DELETE CASCADE
);
