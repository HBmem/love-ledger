drop database if exists love_ledger;
create database love_ledger;
use love_ledger;

create table `user` (
	user_id int primary key auto_increment,
    username varchar(50) not null unique,
    password_hash varchar(2048) not null,
    email varchar(250) not null unique,
    disabled boolean not null default(0),
    
    -- personal details
    fname varchar(100),
    lname varchar(100),
    gender varchar(50)
);

create table `role` (
	role_id int primary key auto_increment,
    `name` varchar(50) not null unique
);

create table user_role (
	user_id int not null,
    role_id int not null,
    constraint pk_user_role
		primary key (user_id, role_id),
	constraint fk_user_role_user_id
		foreign key (user_id)
        references `user`(user_id),
	constraint fk_user_role_role_id
		foreign key (role_id)
        references `role`(role_id)
);

create table milestone (
	milestone_id int primary key auto_increment,
    `name` varchar(50) not null,
    `description` text not null
);

create table love_interest (
	love_interest_id int primary key auto_increment,
    nickname varchar(50),
    fname varchar(100),
    lname varchar(100),
    gender varchar(25),
    birthday date,
    hobbies text,
    likes text,
    dislikes text,
    user_id int not null,
    constraint fk_love_interest_user_id
		foreign key (user_id)
        references `user`(user_id)
);

create table relationship (
	relationship_id int primary key auto_increment,
    start_date date not null,
    end_date date null,
    is_official boolean not null default(0),
    labels varchar(30) not null,
    user_id int not null,
    love_interest_id int not null,
    constraint fk_relationship_user_id
		foreign key (user_id)
        references `user`(user_id),
	constraint fk_relationship_love_interest_id
		foreign key (love_interest_id)
        references love_interest(love_interest_id)
);

create table communication (
	communication_id int primary key auto_increment,
    `date` date not null,
    `type` varchar(30) not null,
    `description` text,
    mood_score int check(mood_score >= 1 AND mood_score <= 5),
    relationship_id int not null,
    constraint fk_communication_relationship_id
		foreign key (relationship_id)
        references relationship(relationship_id)
);

create table outing (
	outing_id int primary key auto_increment,
    `name` varchar(100) not null,
    outing_type varchar(30) not null,
    `description` text,
    location varchar(300),
    outcome text,
    start_time datetime not null,
    end_time datetime not null,
    relationship_id int not null,
    constraint fk_outing_relationship_id
		foreign key (relationship_id)
        references relationship(relationship_id)
);

create table relationship_milestone (
	relationship_id int not null,
    milestone_id int not null,
    date_awarded date not null,
    constraint pk_relationship_milestone
		primary key (relationship_id, milestone_id),
	constraint fk_relationship_milestone_relationship_id
		foreign key (relationship_id)
        references relationship(relationship_id),
	constraint fk_relationship_milestone_milestone_id
		foreign key (milestone_id)
        references milestone(milestone_id)
);

create table notable_day (
	notable_day_id int primary key auto_increment,
    `name` varchar(100) not null,
    `description` text,
    `day` int not null,
    `month` int not null
);

create table reminder (
	reminder_id int primary key auto_increment,
    `name` varchar(100) not null,
    `description` text,
    `date` date not null,
    user_id int not null,
    notable_day_id int,
    constraint fk_reminder_user_id
		foreign key (user_id)
        references `user`(user_id),
	constraint fk_reminder_notable_day_id
		foreign key (notable_day_id)
        references notable_day(notable_day_id)
);
