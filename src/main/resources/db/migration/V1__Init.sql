-- Financial Classification Table
create table if not exists financial_classification
(
    id   varchar(255) not null
    primary key,
    name varchar(255) not null,
    type varchar(255) not null
    constraint financial_classification_type_check
    check ((type)::text = ANY ((ARRAY ['INCOME'::character varying, 'EXPENSE'::character varying])::text[]))
    );

alter table financial_classification
    owner to admin;

create table if not exists financial_transaction
(
    id                varchar(255)   not null
    primary key,
    amount            numeric(38, 2) not null,
    description       varchar(500),
    transaction_date  timestamp(6)   not null,
    classification_id varchar(255)   not null
    constraint fkbko2lx1rkuksu3f12ng264wpg
    references financial_classification
    );

alter table financial_transaction
    owner to admin;

create table if not exists invalidated_token
(
    id          varchar(255) not null
    primary key,
    expiry_time timestamp(6)
    );

alter table invalidated_token
    owner to admin;

create table if not exists tb_user
(
    id        varchar(255) not null
    primary key,
    mail      varchar(255),
    password  varchar(255),
    roles     varchar(255)[],
    user_name varchar(255)
    );

alter table tb_user
    owner to admin;

