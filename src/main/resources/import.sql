INSERT INTO tb_user (email, password) VALUES ('gustavolyra23@gmail.com','$2a$10$Tnmybvz0mCSNdtrXJ/4BgOmEB1zk8zCrcqEFYJqsvaY1nvdyZXTIW');
INSERT INTO tb_user (email, password) VALUES ('david@gmail.com','$2a$10$8ndIsmVlA0Q5BGKagILCCeS7LZ3f5DThjaux0ZX.j2fZIZ08AHr7.');

INSERT INTO tb_role (authority) VALUES ('ROLE_USER');

INSERT INTO tb_user_role (user_id,role_id) VALUES (2,1);
INSERT INTO tb_user_role (user_id,role_id) VALUES (1,1);