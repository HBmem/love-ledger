drop database if exists love_ledger_test;
create database love_ledger_test;
use love_ledger_test;

create table `user` (
	user_id int primary key auto_increment,
    username varchar(50) not null unique,
    password_hash varchar(2048) not null,
    email varchar(250) not null unique,
    profile_image_url varchar(255),
    is_disabled boolean not null default(0),
    is_verified boolean not null default(0),
    
    -- personal details
    fname varchar(100),
    lname varchar(100),
    gender varchar(50),
    birthday date
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
    `description` text not null,
    `type` varchar(50) not null
);

create table user_milestone (
	user_id int not null,
    milestone_id int not null,
    date_received date not null,
    constraint pk_user_milestone
		primary key (user_id, milestone_id),
	constraint fk_user_milestone_user_id
		foreign key (user_id)
        references `user` (user_id),
	constraint fk_user_milestone_milestone_id
		foreign key (milestone_id)
        references milestone (milestone_id)
);

create table love_interest (
	love_interest_id int primary key auto_increment,
    nickname varchar(50),
    fname varchar(100),
    lname varchar(100),
    gender varchar(25),
    profile_image_url varchar(255),
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
    end_date date,
    relationship_status varchar(50) not null,
    importance_level int not null check(importance_level >= 1 AND importance_level <= 5),
    is_official boolean not null default(0),
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
    `type` varchar(30) not null,
    `description` text,
    location varchar(300),
    outcome text,
    cost decimal(10,2),
    rating int check(rating >= 1 AND rating <= 5),
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
    `month` int not null,
    `year` int 
);

create table reminder (
	reminder_id int primary key auto_increment,
    `name` varchar(100) not null,
    `description` text,
    `date` date not null,
    user_id int not null,
    relationship_id int,
    notable_day_id int,
    constraint fk_reminder_user_id
		foreign key (user_id)
        references `user`(user_id),
	constraint fk_reminder_relationship_id
		foreign key (relationship_id)
        references relationship(relationship_id),
	constraint fk_reminder_notable_day_id
		foreign key (notable_day_id)
        references notable_day(notable_day_id)
);

delimiter //
create procedure set_known_good_state()
begin

	delete from reminder;
    alter table reminder auto_increment = 1;
    delete from notable_day;
    alter table notable_day auto_increment = 1;
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
    
    insert into `user`(user_id, username, password_hash, email, profile_image_url, is_disabled, is_verified, fname, lname, gender, birthday) values
		(1, 'JohnD', 'TEST', 'john.doe@email.com', '', false, true, 'John', 'Doe', 'MALE', '1997-01-04'),
        (2, 'JaneD', 'TEST', 'jane.doe@email.com', '', false, false, 'Jane', 'Doe', 'FEMALE', '1992-02-09');
	
    insert into `role`(role_id, `name`) values
		(1, 'ADMIN'),
        (2, 'USER');
        
	insert into user_role(user_id, role_id) values
		(1, 2),
        (2, 1);
	
    insert into milestone(milestone_id, `name`, `description`, `type`) values
		(1, 'New Relationship', 'The start of something new hopefully this one goes somewhere.', 'RELATIONSHIP'),
        (2, '1 Month', 'We lasted a month! hopefully we keep going.', 'RELATIONSHIP'),
        (3, 'First Valentine', 'We celebrated our first valentine!', 'RELATIONSHIP');
	
--     insert into user_milestone(user_id, milestone_id, date_received) values
-- 		();
    
    insert into love_interest(love_interest_id, nickname, fname, lname, gender, profile_image_url, birthday, hobbies, likes, dislikes, user_id) values
		(1, 'Princess', 'Fiona', 'Ogre', 'FEMALE', 'IMG/URL', '1996-04-28', 'fighting;singing;dancing', 'color green;birds;fights', 'short people;color pink', 1),
        (2, 'Hasanabi', 'Hasan', 'Piker', 'MALE', 'IMG/URL', '1986-09-11', 'talking;arguing;basketball', 'politics', 'liberals;conservatives', 2),
        (3, null, 'Ash', 'Kechum', 'MALE', 'IMG/URL', '2000-04-13', 'animals; animal abuse', 'pokemon', 'team rocket', 2);
        
	insert into relationship(relationship_id, start_date, end_date, relationship_status, importance_level, is_official, user_id, love_interest_id) values
		(1, '2024-05-01', null, 'DATING', 3, true, 2, 2),
        (2, '2023-11-02', '2024-04-29', 'TALKING', 2, true, 2, 2),
        (3, '2023-12-25', null, 'DATING', 4, true, 1, 1);

	insert into communication(communication_id, `date`, `type`, `description`, mood_score, relationship_id) values
		(1, '2024-06-10', 'TEXT', 'Talked about fighting', 4, 1),
        (2, '2024-05-05', 'TEXT', 'Talked about ideas for a date', 5, 1),
        (3, '2024-05-10', 'PHONE_CALL', 'Talked about their parent\'s resent illness', 3, 1);
        
	insert into outing(outing_id, `name`, `type`, `description`, location, outcome, cost, rating, start_time, end_time, relationship_id) values
		(1, 'Feb 1st Park Date', 'DATE', 'Went to the park with my date. We talked for a while then took a walk together.', 'Overton Park', 'The date was fun', 250.00, 4,'2024-02-01 03:00:00', '2024-02-01 04:00:00', 1),
        (2, 'Feb 5th Car Date', 'DATE', 'Drove to our favourite spot and made out.', 'Overton Park', 'They\'re a great kisser :)', 100.00, 5,'2024-05-01 03:00:00', '2024-05-01 03:30:00', 1),
        (3, 'Michigan Vacation', 'VACATION', 'Traveled to Michigan to see the sights.', 'Michigan', 'It was awful they cheated on me :(', 5.50, 1 ,'2024-04-11 03:00:00', '2024-04-21 03:30:00', 2);
	
    insert into relationship_milestone(relationship_id, milestone_id, date_awarded) values
		(1, 1, '2024-03-11');

	insert into notable_day(notable_day_id, `name`, `description`, `day`, `month`, `year`) values
		(1, 'Valentines Day', 'A day of love celebrated around the world.', 14, 2, -1),
        (2, 'Thanksgiving 2024', 'Time to meet the family.', 28, 11, 2024),
        (3, 'Christmas Day', 'Winter Time! it\'s time to settle with the family.', 25, 12, -1);
        
    insert into reminder(reminder_id, `name`, `date`, `description`, user_id, relationship_id, notable_day_id) values
		(1, 'Birthday', '2024-01-04', 'It\'s your birthday today.', 1, null, null),
        (2, 'Birthday', '2024-05-04', 'I5\'s your birthday today.', 2, 2, null),
        (3, 'Valentines Day', '2024-02-14', 'First Valentines Day!', 1, 1, 1);
        
end //
delimiter ;