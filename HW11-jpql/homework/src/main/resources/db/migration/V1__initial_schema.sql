-- Для @GeneratedValue(strategy = GenerationType.IDENTITY)
/*
create table client
(
    id   bigserial not null primary key,
    name varchar(50)
);

 */

-- Для @GeneratedValue(strategy = GenerationType.SEQUENCE)
create sequence global_seq start with 100000;

create table address
(
    id     bigint not null primary key,
    street varchar(120)
);
create table client
(
    id   bigint not null primary key,
    name varchar(50),
    address_id bigint references address (id) on delete cascade
);
