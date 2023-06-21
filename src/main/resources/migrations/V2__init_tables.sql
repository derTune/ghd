drop table if exists core.booking;
drop table if exists tg.chats;

create table core.booking
(
    id             serial    not null,
    rec_time       timestamp not null default now(),
    is_deleted     boolean            default false,
    applicant_name text,
    arrival_date   timestamp,
    departure_date timestamp,
    adults_count   int,
    children_count int,
    phones         text,
    state          int
);

create table tg.chats
(
    id          bigint not null,
    first_name  text,
    last_name   text,
    username    text,
    description text,
    title       text,
    type        text,
    is_main     boolean default false
);