use love_ledger;

insert into user_credential(id, email, `password`, is_verified, is_disabled) values
	(1, "adam@email.com", "$2y$12$09myHJmbBR6J4nbT4j8yluvOZwOFfWebVpVldcQhHfpS5xmWrbxH2", false, false); -- password: a@123
    
insert into `role`(id, `name`) values
	(1, "Admin"),
    (2, "Premium"),
    (3, "Basic");
    
insert into user_role (user_id, role_id) values
	(1, 1);