CREATE TABLE users (
  user_id bigint NOT NULL AUTO_INCREMENT,
  username varchar(255) NOT NULL UNIQUE,
  password varchar(255) NOT NULL,
  type varchar(8),
  PRIMARY KEY (user_id)
);

CREATE TABLE sellers (
  seller_id bigint NOT NULL AUTO_INCREMENT,
  user_id bigint NOT NULL,
  order_col bigint,
  name varchar(255) NOT NULL UNIQUE,
  description varchar(255),
  seller_website_url varchar(255),
  PRIMARY KEY (seller_id),
  CONSTRAINT userhasmanysellers FOREIGN KEY(user_id) REFERENCES users(user_id)
);

CREATE TABLE products (
  product_id bigint NOT NULL AUTO_INCREMENT,
  user_id bigint NOT NULL,
  order_col bigint,
  name varchar(12) NOT NULL,
  type varchar(20) NOT NULL,
  description varchar(255),
  product_website_url varchar(255),
  PRIMARY KEY (product_id),
  CONSTRAINT userhasmanyproducts FOREIGN KEY(user_id) REFERENCES users(user_id)
);

CREATE TABLE seller_prices (
   product_product_id bigint NOT NULL,
   prices double,
   seller_id bigint NOT NULL
);

CREATE TABLE orders(
    order_id bigint NOT NULL AUTO_INCREMENT,
    user_id bigint NOT NULL,
    title varchar(100) NOT NULL,
    status varchar(10) NOT NULL,
    order_date date NOT NULL,
    description varchar(255),
    total_price double,
    counter bigint
);

CREATE TABLE ordered_products(
    ordered_product_id bigint NOT NULL AUTO_INCREMENT,
    product_id bigint NOT NULL,
    seller_id bigint NOT NULL,
    order_id bigint NOT NULL,
    amount bigint NOT NULL,
    price double NOT NULL,
    PRIMARY KEY (ordered_product_id),
    CONSTRAINT producthasorderedproduct FOREIGN KEY(product_id) REFERENCES products(product_id),
    CONSTRAINT sellerhasorderedproduct FOREIGN KEY(seller_id) REFERENCES sellers(seller_id),
    CONSTRAINT orderhasmanyorderedproducts FOREIGN KEY(order_id) REFERENCES orders(order_id)
);