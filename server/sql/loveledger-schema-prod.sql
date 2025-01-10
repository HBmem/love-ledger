drop database if exists love_ledger;
create database love_ledger;
use love_ledger;

create table user_credential (
	id int primary key auto_increment,
    email varchar(250) unique,
    `password` varchar(100) not null,
    phone_number varchar(10),
    is_verified boolean not null default(0),
    is_disabled boolean not null default(0)
);

create table user_profile (
	id int primary key auto_increment,
    first_name varchar(50),
    last_name varchar(50),
    gender varchar(50) not null,
    birthday date not null,
    likes JSON,
    dislikes JSON,
    photo_url text,
    constraint fk_user_credential_profile
		foreign key (id)
        references user_credential(id)
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

create table love_interest (
	id int primary key auto_increment,
    nickname varchar(50),
    first_name varchar(50),
    last_name varchar(50),
    gender varchar(20) not null,
    birthday date,
    likes JSON,
    dislikes JSON,
    photo_url text,
    user_id int not null,
    constraint fk_love_interest_user_id
		foreign key (user_id)
        references user_credential(id)
);

create table relationship (
	id int primary key auto_increment,
	start_date date not null,
    end_date date,
    relationship_status varchar(50) not null,
    importance_level int not null,
    is_official boolean not null default(0),
    user_id int not null,
    love_interest_id int not null,
    constraint fk_relationship_user_id
		foreign key (user_id)
        references user_credential(id),
	constraint fk_relationship_love_interest_id
		foreign key (love_interest_id)
        references love_interest(id)
);

create table milestone (
	id int primary key auto_increment,
    `name` varchar(250) not null,
    `description` text not null,
    `type` varchar(50) not null,
    `condition` JSON not null
);

create table user_milestone (
	user_id int not null,
    milestone_id int not null,
    achieved_at date not null,
    constraint pk_user_milestone
		primary key (user_id, milestone_id),
	constraint fk_user_milestone_user_id
		foreign key (user_id)
        references user_credential(id),
	constraint fk_user_milestone_milestone_id
		foreign key (milestone_id)
        references milestone(id)
);

create table relationship_milestone (
	relationship_id int not null,
    milestone_id int not null,
    achieved_at date not null,
    constraint pk_relationship_milestone
		primary key (relationship_id, milestone_id),
	constraint fk_relationship_milestone_relationship_id
		foreign key (relationship_id)
        references relationship(id),
	constraint fk_relationship_milestone_milestone_id
		foreign key (milestone_id)
        references milestone(id)
);