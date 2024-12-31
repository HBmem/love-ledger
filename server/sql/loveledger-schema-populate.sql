use love_ledger;

insert into user_credential(id, username, email, `password`, is_verified, is_disabled) values
	(1, "adam", "adam@email.com", "$2y$12$09myHJmbBR6J4nbT4j8yluvOZwOFfWebVpVldcQhHfpS5xmWrbxH2", false, false); -- password: a@123