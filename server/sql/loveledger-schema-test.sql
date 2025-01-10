drop database if exists love_ledger_test;
create database love_ledger_test;
use love_ledger_test;

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
	id int primary key auto_increment,
	user_id int not null,
    milestone_id int not null,
    achieved_at date not null,
	constraint fk_user_milestone_user_id
		foreign key (user_id)
        references user_credential(id),
	constraint fk_user_milestone_milestone_id
		foreign key (milestone_id)
        references milestone(id)
);

create table relationship_milestone (
	id int primary key auto_increment,
	relationship_id int not null,
    milestone_id int not null,
    achieved_at date not null,
	constraint fk_relationship_milestone_relationship_id
		foreign key (relationship_id)
        references relationship(id),
	constraint fk_relationship_milestone_milestone_id
		foreign key (milestone_id)
        references milestone(id)
);

delimiter //
create procedure set_known_good_state()
begin
	delete from relationship_milestone;
    alter table relationship_milestone auto_increment = 1;
    delete from user_milestone;
    alter table user_milestone auto_increment = 1;
	delete from milestone;
    alter table milestone auto_increment = 1;
	delete from relationship;
    alter table relationship auto_increment = 1;
	delete from love_interest;
    alter table love_interest auto_increment = 1;
	delete from user_role;
    alter table user_role auto_increment = 1;
    delete from `role`;
    alter table `role` auto_increment = 1;
    delete from user_profile;
    alter table user_profile auto_increment = 1;
    delete from user_credential;
    alter table user_credential auto_increment = 1;
    
    insert into user_credential(id, email, `password`, phone_number, is_verified, is_disabled) values
		(1, "adam@email.com", "$2y$12$09myHJmbBR6J4nbT4j8yluvOZwOFfWebVpVldcQhHfpS5xmWrbxH2", "1234567890", false, false), -- password: a@123
		(2, "brit@email.com", "$2y$12$Uj3aXL0vXAFt.AFtyEw19uDs4Jv0jN3oRR0aet.PoC/aTcaRFIoke", "1122334455", false, false), -- password: b@123
		(3, "carl@email.com", "$2y$12$JGt..bPM5UYQG6l.yIngN.frjBICxIKAdsl.cdI3m08zOzoazUjdy", "6677889900", false, false); -- password: c@123
    
    insert into user_profile(id, first_name, last_name, gender, birthday, likes, dislikes, photo_url) values
		(1, "Adam", "Smith", "MALE", "1991-01-04", JSON_ARRAY('Football', 'Traveling', 'Video Games'), JSON_ARRAY('Loud Music', 'Waiting in Line'), ""),
		(2, "Brit", "Taylor", "FEMALE", "1995-01-15", JSON_ARRAY('Photography', 'Cycling', 'Poetry'), JSON_ARRAY('Hot Weather', 'Seafood'), ""),
		(3, "Carl", "Bennet", "NON_BINARY", "1985-08-23", JSON_ARRAY('Dancing', 'Painting', 'Gardening'), JSON_ARRAY('Fast Food', 'Long Meetings'), "");
    
	insert into `role`(id, `name`) values
		(1, "ADMIN"),
		(2, "PREMIUM"),
		(3, "BASIC");
    
	insert into user_role (user_id, role_id) values
		(1, 1),
        (2, 2),
        (3, 3);
        
	insert into love_interest(id, nickname, first_name, last_name, gender, birthday, likes, dislikes, photo_url, user_id) values
		(1, "", "Evan", "Williams", "MALE", "1992-03-10", JSON_ARRAY('Fishing', 'Cooking', 'Running'), JSON_ARRAY('Late Nights', 'Cold Weather'), "", 2),
        (2, "Benny", "", "", "NON_BINARY", "1995-08-23", JSON_ARRAY('Football', 'Traveling', 'Old Video Games'), JSON_ARRAY('Loud Music', 'Waiting in Line'), "", 3),
        (3, "", "Alice", "Johnson", "FEMALE", "1990-05-12", JSON_ARRAY('Reading', 'Hiking', 'Cooking'), JSON_ARRAY('Spicy Food', 'Crowded Places'), "", 1);
	
    insert into relationship(id, start_date, end_date, relationship_status, importance_level, is_official, user_id, love_interest_id) values
		(1, "2023-02-12", null, "ENGAGED", 5, true, 2, 1),
        (2, "2024-06-05", null, "DATING", 3, false, 3, 2),
        (3, "2024-02-06", "2024-12-21", "TALKING", 1, false, 1, 3);
        
	insert into milestone(id, `name`, `description`, `type`, `condition`) values
		(1, "Six Month Anniversary", "Your relationship has lasted 6 months", "RELATIONSHIP", '{"type": "RELATIONSHIP", "condition": {"duration": "6 months"}}'),
        (2, "1 Year Old", "You have had your account for one year", "USER", '{"type": "USER", "condition": {"accountAge": "1 year"}}'),
        (3, "Valentines Day", "You celebrated valentines day with [NAME]", "RELATIONSHIP", '{"type": "RELATIONSHIP", "condition": {"event": "Valentine\'s Day"}}');
end //
delimiter ;