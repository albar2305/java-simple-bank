CREATE DATABASE simple_bank;

USE simple_bank;

drop database simple_bank;

CREATE TABLE accounts (
                            id varchar(100) NOT NULL PRIMARY KEY ,
                            owner varchar(100) NOT NULL,
                            balance bigint NOT NULL,
                            currency varchar(100) NOT NULL,
                            created_at timestamp NOT NULL default (now())
);

describe accounts;
describe users;
describe transfers;

drop table accounts;
drop table entries;

CREATE TABLE entries (
                           id varchar(100) PRIMARY KEY,
                           account_id varchar(100) NOT NULL,
                           amount bigint NOT NULL,
                           created_at timestamp NOT NULL DEFAULT (now()),
                            foreign key fk_account_entries (account_id) REFERENCES accounts(id)
);

CREATE TABLE transfers (
                             id varchar(100) PRIMARY KEY,
                             from_account_id varchar(100) NOT NULL,
                             to_account_id varchar(100) NOT NULL,
                             amount bigint NOT NULL,
                             created_at timestamp NOT NULL DEFAULT (now()),
                             foreign key fk_from_account_transfers (from_account_id) REFERENCES accounts(id),
                             foreign key fk_to_account_transfers (to_account_id) REFERENCES accounts(id)
);

CREATE TABLE users (
                         username varchar(100) PRIMARY KEY,
                         hashed_password varchar(100) NOT NULL,
                         full_name varchar(100) NOT NULL,
                         email varchar(100) UNIQUE NOT NULL,
                         password_changed_at timestamp NOT NULL DEFAULT('0001-01-01 00:00:00'),
                         created_at timestamp NOT NULL DEFAULT (now())
);



describe users;

ALTER TABLE accounts ADD FOREIGN KEY fk_owner_user_accounts (owner) REFERENCES users (username);

ALTER TABLE accounts ADD CONSTRAINT owner_currency_key UNIQUE (owner, currency);

CREATE TABLE roles (
    id INT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(100) NOT NULL
);

CREATE TABLE user_roles(
    username VARCHAR(100) ,
    role_id INT ,
    FOREIGN KEY (username) REFERENCES users (username),
    FOREIGN KEY (role_id) REFERENCES roles (id)
);

use simple_bank;

