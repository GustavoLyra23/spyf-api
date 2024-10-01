INSERT INTO tb_user (email, password) VALUES ('gustavolyra23@gmail.com','$2a$12$N8n3ymn2EigZQUjYpI56S.W4BBeOk3UQ57OIOhwp0qzK8mrxcMp7O');
INSERT INTO tb_user (email, password) VALUES ('david@gmail.com','$2a$12$N8n3ymn2EigZQUjYpI56S.W4BBeOk3UQ57OIOhwp0qzK8mrxcMp7O');

INSERT INTO tb_role (authority) VALUES ('ROLE_USER');

INSERT INTO tb_user_role (user_id,role_id) VALUES (2,1);
INSERT INTO tb_user_role (user_id,role_id) VALUES (1,1);