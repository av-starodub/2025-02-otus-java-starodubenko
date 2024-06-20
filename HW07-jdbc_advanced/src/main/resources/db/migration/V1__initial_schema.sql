DROP TABLE IF EXISTS users;
DROP TABLE IF EXISTS accounts;

CREATE TABLE users
(
    id       bigint GENERATED ALWAYS AS IDENTITY,
    login    varchar(20),
    password varchar(20),
    nickname varchar(20),
    CONSTRAINT pk_user PRIMARY KEY (id)
);

CREATE TABLE accounts
(
    id          bigint GENERATED ALWAYS AS IDENTITY,
    amount      bigint,
    tp varchar(20),
    status varchar(20),
    CONSTRAINT pk_account PRIMARY KEY (id)
);
