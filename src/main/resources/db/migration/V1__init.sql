CREATE TABLE users (
  id bigint(20) NOT NULL AUTO_INCREMENT,
  user_name varchar(100) NOT NULL,
  password varchar(100) NOT NULL,
  type varchar(8),
  PRIMARY KEY (id)
);
