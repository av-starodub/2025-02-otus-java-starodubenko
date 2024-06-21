CREATE SEQUENCE global_seq START WITH 100000;

CREATE TABLE client
(
    id   bigint not null primary key,
    name varchar(50)
);
