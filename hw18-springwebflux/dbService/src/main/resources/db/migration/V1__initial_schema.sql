-- Для @GeneratedValue(strategy = GenerationType.IDENTITY)
/*
create table client
(
    id   bigserial not null primary key,
    name varchar(50)
);

 */

-- Для @GeneratedValue(strategy = GenerationType.SEQUENCE)
--create sequence client_sequence start with 1 increment by 1;
--create sequence address_sequence start with 1 increment by 1;
--create sequence phone_sequence start with 1 increment by 1;

create table client
(
    id   bigserial not null primary key,
    name varchar(50)
);

create table address
(
    id   bigserial not null primary key,
    street varchar(50),
    client_id bigint references client(id)
);

create table phone
(
    id   bigserial not null primary key,
    number varchar(50),
    client_id bigint references client(id)
);

insert into client (name)
values
    ('AAAname'),
    ('BBBname'),
    ('CCCname');
    ('DDDname');
    ('EEEname');

insert into address (street, client_id)
    values
    ('AAAstreet', 1),
    ('BBBstreet', 2);

insert into phone (number, client_id)
    values
    ('11111', 1),
    ('22222', 2),
    ('33333', 2);