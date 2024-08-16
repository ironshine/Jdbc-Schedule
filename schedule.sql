-- auto-generated definition
create table schedule
(
    id       bigint auto_increment
        primary key,
    toDo     varchar(500) not null,
    name     varchar(255) not null,
    password varchar(255) not null,
    dateTime varchar(255) not null
);