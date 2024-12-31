drop database if exists love_ledger;
create database love_ledger;
use love_ledger;

create table user_credential (
	id int primary key auto_increment,
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
    phone_number varchar(10),
    likes JSON,
    disliked JSON,
    photo_url text
);

create table `role` (
	id int primary key auto_increment,
    `name` varchar(20)
);

create table user_role (
	user_id int not null,
    role_id int not null,
    constraint pk_user_role
		primary key (user_id, role_id),
	constraint fk_user_role_user_id
		foreign key (user_id)
        references user_credential(id),
	constraint fk_user_role_role_id
		foreign key (role_id)
        references `role`(id)
);