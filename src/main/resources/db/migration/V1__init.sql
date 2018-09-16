CREATE TABLE users (
  id bigint NOT NULL AUTO_INCREMENT,
  username varchar(255) NOT NULL UNIQUE,
  password varchar(255) NOT NULL,
  type varchar(8),
  PRIMARY KEY (id)
);
