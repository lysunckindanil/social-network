create table if not exists profiles
(
    id        bigint generated by default as identity primary key,
    email     varchar(255),
    password  varchar(255) not null,
    photo_url varchar(255),
    username  varchar(255) not null
        unique
);

create table if not exists profile_subscriber
(
    profile_id    bigint not null references profiles (id),
    subscriber_id bigint not null references profiles (id),
    primary key (profile_id, subscriber_id)
);