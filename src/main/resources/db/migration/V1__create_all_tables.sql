create sequence coin_seq start with 1 increment by 50;
create sequence coinage_seq start with 1 increment by 50;
create sequence material_seq start with 1 increment by 50;
create sequence nominal_seq start with 1 increment by 50;
create sequence series_seq start with 1 increment by 50;
create sequence users_seq start with 1 increment by 50;

create table coin
(
    id                   integer not null,
    authors              TEXT,
    catalog_number       varchar(255),
    circulation          varchar(255),
    design_herd          TEXT,
    diameter             varchar(255),
    historical_reference TEXT,
    is_investment        boolean not null,
    length               varchar(255),
    link_to_bank_page    TEXT,
    name                 varchar(255),
    obverse              TEXT,
    pure_metal           varchar(255),
    quality              varchar(255),
    release_date         timestamp(6),
    reverse              TEXT,
    thickness            varchar(255),
    weight               varchar(255),
    width                varchar(255),
    id_coinage           integer,
    id_material          integer,
    id_nominal           integer,
    id_series            integer,
    primary key (id)
);

create table coinage
(
    id_coinage integer not null,
    coinage    varchar(255),
    primary key (id_coinage)
);

create table coins_users
(
    coin_id integer not null,
    user_id integer not null,
    count   integer not null,
    primary key (coin_id, user_id)
);

create table material
(
    id       integer not null,
    material varchar(255),
    primary key (id)
);

create table nominal
(
    id      integer not null,
    nominal varchar(255),
    primary key (id)
);

create table series
(
    id_series         integer not null,
    id_in_bank        integer,
    link_to_bank_page TEXT,
    series_name       varchar(255),
    primary key (id_series)
);

create table user_role
(
    user_id integer not null,
    roles   varchar(255)
);

create table users
(
    id              integer not null,
    activation_code varchar(255),
    active          boolean not null,
    email           varchar(255),
    password        varchar(255),
    username        varchar(255),
    primary key (id)
);

alter table if exists coin
    add constraint FKslvqa4xivg94nmplk3a7pehuy
    foreign key (id_coinage) references coinage;

alter table if exists coin
    add constraint FKqnmuikdldbolbv6jp3rcx4ama
    foreign key (id_material) references material;

alter table if exists coin
    add constraint FKsuc6ruo506qv36y9lewettuln
    foreign key (id_nominal) references nominal;

alter table if exists coin
    add constraint FKruvpm2t5k76nn679e7qr7ha5k
    foreign key (id_series) references series;

alter table if exists coins_users
    add constraint FK896sd90vqre66gm872uegwnu9
    foreign key (coin_id) references coin;

alter table if exists coins_users
    add constraint FKp3t3f6frgaqbgxkt0nc3gr9e5
    foreign key (user_id) references users;

alter table if exists user_role
    add constraint FKj345gk1bovqvfame88rcx7yyx
    foreign key (user_id) references users;