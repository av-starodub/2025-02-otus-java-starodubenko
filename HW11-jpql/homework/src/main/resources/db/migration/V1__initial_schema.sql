create sequence global_seq start with 100000;

create table address
(
    id     bigint       not null primary key,
    street varchar(120) not null
);

create table client
(
    id         bigint      not null primary key,
    name       varchar(50) not null,
    address_id bigint references address (id) on delete cascade
);

create table phone
(
    id        bigint      not null primary key,
    number    varchar(20) not null,
    client_id bigint      not null references client (id) on delete cascade
);
