drop database if exists love_ledger_test;
create database love_ledger_test;
use love_ledger_test;

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

create table reminder (
	reminder_id int primary key auto_increment,
    `name` varchar(100) not null,
    `date` date not null,
    `description` text,
    user_id int not null,
    constraint fk_reminder_user_id
		foreign key (user_id)
        references `user`(user_id)
);

delimiter //
create procedure set_known_good_state()
begin

	delete from reminder;
    alter table reminder auto_increment = 1;
    delete from relationship_milestone;
    delete from outing;
    alter table outing auto_increment = 1;
    delete from communication;
    alter table communication auto_increment = 1;
    delete from relationship;
    alter table relationship auto_increment = 1;
    delete from love_interest;
    alter table love_interest auto_increment = 1;
    delete from milestone;
    alter table milestone auto_increment = 1;
    delete from user_role;
    alter table user_role auto_increment = 1;
    delete from `role`;
    alter table `role` auto_increment = 1;
    delete from `user`;
    alter table `user` auto_increment = 1;
    
    insert into `user`(user_id, username, password_hash, email, disabled, fname, lname) values
		(1, 'JohnD', 'TEST', 'john.doe@email.com', true, 'John', 'Doe'),
        (2, 'JaneD', 'TEST', 'jane.doe@email.com', true, 'Jane', 'Doe');
	
    insert into `role`(role_id, `name`) values
		(1, 'ADMIN'),
        (2, 'USER');
        
	insert into user_role(user_id, role_id) values
		(1, 2),
        (2, 1);
	
    insert into milestone(milestone_id, `name`, `description`) values
		(1, 'New Relationship', 'The start of something new hopefully this one goes somewhere.'),
        (2, '1 Month', 'We lasted a month! hopefully we keep going.');
	
    insert into love_interest(love_interest_id, nickname, fname, lname, gender, birthday, hobbies, likes, dislikes, user_id) values
		(1, 'Princess', 'Fiona', 'Ogre', 'FEMALE', '1996-04-28', 'fighting;singing;dancing', 'color green;birds;fights', 'short people;color pink', 1),
        (2, 'Hasanabi', 'Hasan', 'Piker', 'MALE', '1986-09-11', 'talking;arguing;basketball', 'politics', 'liberals;conservatives', 2),
        (3, null, 'Ash', 'Kechum', 'MALE', '2000-04-13', 'animals; animal abuse', 'pokemon', 'team rocket', 2);
        
	insert into relationship(relationship_id, start_date, end_date, is_official, labels, user_id, love_interest_id) values
		(1, '2024-05-01', null, true, '', 1, 1);

	insert into communication(communication_id, `date`, `type`, `description`, mood_score, relationship_id) values
		(1,'2024-6-10', 'TEXT', 'Talked about fighting', 4, 1);
        
end //
delimiter ;