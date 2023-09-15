INSERT INTO roles VALUES(1, "ADMIN");
INSERT INTO roles VALUES(2, "USER");
INSERT INTO roles VALUES(3, "INVITED");

INSERT INTO users VALUES(1, "gerson.benito@gmail.com", "$2a$10$S/ebf0ruh7s53vzF6A0XkOSBVyJFv.MHyY5NGMnVFYT2tFNcpP3bW","Gerson Benito");
INSERT INTO users VALUES(2, "susana.montero@gmail.com", "$2a$10$S/ebf0ruh7s53vzF6A0XkOSBVyJFv.MHyY5NGMnVFYT2tFNcpP3bW","Susana Montero");
INSERT INTO users VALUES(3, "diana.soto@gmail.com", "$2a$10$S/ebf0ruh7s53vzF6A0XkOSBVyJFv.MHyY5NGMnVFYT2tFNcpP3bW","Diana Soto");

INSERT INTO users_roles VALUES(1,1);
INSERT INTO users_roles VALUES(2,2);
INSERT INTO users_roles VALUES(3,3);