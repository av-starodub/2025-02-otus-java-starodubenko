DROP TABLE IF EXISTS address;
DROP TABLE IF EXISTS client;
DROP TABLE IF EXISTS phone;

CREATE SEQUENCE global_seq START WITH 100000;

CREATE TABLE addresses
(
    id     bigint       NOT NULL PRIMARY KEY,
    street varchar(120) NOT NULL

);

CREATE TABLE clients
(
    id   bigint      NOT NULL PRIMARY KEY,
    name varchar(50) NOT NULL,
    address_id bigint REFERENCES addresses (id) ON DELETE CASCADE
);

CREATE TABLE phones
(
    id        bigint      NOT NULL PRIMARY KEY,
    number    varchar(20) NOT NULL,
    client_id bigint     REFERENCES clients (id) ON DELETE CASCADE
);
