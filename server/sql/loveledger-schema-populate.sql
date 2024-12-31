use love_ledger;

insert into user_credential(id, email, `password`, is_verified, is_disabled) values
	(1, "adam@email.com", "$2y$12$09myHJmbBR6J4nbT4j8yluvOZwOFfWebVpVldcQhHfpS5xmWrbxH2", false, false), -- password: a@123
    (2, "brit@email.com", "$2y$12$Uj3aXL0vXAFt.AFtyEw19uDs4Jv0jN3oRR0aet.PoC/aTcaRFIoke", false, false), -- password: b@123
    (3, "carl@email.com", "$2y$12$JGt..bPM5UYQG6l.yIngN.frjBICxIKAdsl.cdI3m08zOzoazUjdy", false, false); -- password: c@123
    
insert into user_profile(id, first_name, last_name, gender, birthday, phone_number, likes, disliked, photo_url) values
	(1, "Adam", "Smith", "MALE", "1991-01-04", "1234567890", JSON_ARRAY('Football', 'Traveling', 'Video Games'), JSON_ARRAY('Loud Music', 'Waiting in Line'), ""),
	(2, "Brit", "Taylor", "FEMALE", "1995-01-15", "1122334455", JSON_ARRAY('Photography', 'Cycling', 'Poetry'), JSON_ARRAY('Hot Weather', 'Seafood'), ""),
	(3, "Carl", "Bennet", "NON_BINARY", "1985-08-23", "6677889900", JSON_ARRAY('Dancing', 'Painting', 'Gardening'), JSON_ARRAY('Fast Food', 'Long Meetings'), "");

insert into `role`(id, `name`) values
	(1, "ADMIN"),
	(2, "PREMIUM"),
	(3, "BASIC");

insert into user_role (user_id, role_id) values
	(1, 1),
	(2, 2),
	(3, 3);