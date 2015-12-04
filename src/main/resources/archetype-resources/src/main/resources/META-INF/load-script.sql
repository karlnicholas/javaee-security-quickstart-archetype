INSERT INTO role(ID, role) VALUES (1, 'USER');
INSERT INTO role(ID, role) VALUES (2, 'ADMIN');
insert into user( id ,email ,firstName ,lastName ,password ) values ( 1, 'admin', 'Admin', 'Istrator', 'jGl25bVBBBW96Qi9Te4V37Fnqchz/Eu4qB9vKrRIqRg=');
insert into user_role(user_id ,roles_id ) values (1, 1);
insert into user_role(user_id ,roles_id ) values (1, 2);