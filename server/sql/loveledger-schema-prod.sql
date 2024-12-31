drop database if exists love_ledger;
create database love_ledger;
use love_ledger;

create table user_credential (
	id int primary key auto_increment,
    username varchar(50) not null unique,
    email varchar(250) unique,
    `password` varchar(100) not null,
    is_verified boolean not null default(0),
    is_disabled boolean not null default(0)
);

create table user_profile (
	id int primary key auto_increment,
    first_name varchar(50),
    last_name varchar(50),
    gender varchar(50) not null,
    birthday date not null,
    phone_number int,
    likes JSON,
    disliked JSON,
    photo_url text
);