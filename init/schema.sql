drop table if exists member;
create table member
(
    id          bigint not null auto_increment,
    created_at  datetime(6) not null,
    updated_at  datetime(6) not null,
    email       varchar(255),
    member_type varchar(255),
    password    varchar(255),
    primary key (id)
) engine=InnoDB;
alter table member
    add constraint UK_email unique (email);

drop table if exists taxi_request;
create table taxi_request
(
    id           bigint not null auto_increment,
    created_at   datetime(6) not null,
    updated_at   datetime(6) not null,
    accepted_at  datetime(6),
    address      varchar(100),
    status       varchar(255),
    driver_id    bigint,
    passenger_id bigint not null,
    primary key (id)
) engine=InnoDB;
alter table taxi_request
    add constraint FK_driver_id
        foreign key (driver_id)
            references member (id);
alter table taxi_request
    add constraint FK_passenger_id
        foreign key (passenger_id)
            references member (id);