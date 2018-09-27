-- Do not allowed on production!

INSERT INTO users(user_id, username, password, type)
VALUES(20180001, 'db_test_user', 'db_test_password', 'SIMPLE');

INSERT INTO orders(order_id, user_id, title, status, order_date, total_price)
VALUES(20180001, 20180001, 'test_order', 'ORDERED', NOW(), 0.0);

insert into sellers (seller_id, user_id, order_col, nip, name, street, local_number, postal_code, location, description)
values (20180001, 20180001, null,'432-981-77-19', 'db_test seller #1', 'ulica', '22a', '55-200', 'Wroclaw', null);
insert into sellers (seller_id, user_id, order_col, nip, name, street, local_number, postal_code, location, description)
values (20180002, 20180001, null,'123-456-88-18', 'db_test seller #2', 'ulica', '12', '40-250', 'Warszawa', null);
insert into sellers (seller_id, user_id, order_col, nip, name, street, local_number, postal_code, location, description)
values (20180003, 20180001, null,'453-234-32-22', 'db_test seller #3', 'ulica', '2c', '10-312', 'Wroclaw', null);

insert into products (product_id, user_id, order_col, name, type, description, product_website_url)
values (20180001, 20180001, null, 'Test 1', 'db_type 1', null, null);
insert into seller_prices (product_product_id, prices, seller_id)
values (20180001, 10.22, 20180001);
insert into seller_prices (product_product_id, prices, seller_id)
values (20180001, 43.10, 20180002);
insert into seller_prices (product_product_id, prices, seller_id)
values (20180001, 68, 20180003);

insert into products (product_id, user_id, order_col, name, type, description, product_website_url)
values (20180002, 20180001, null, 'Test 2', 'db_type 2', null, null);
insert into seller_prices (product_product_id, prices, seller_id)
values (20180002, 105.28, 20180001);
insert into seller_prices (product_product_id, prices, seller_id)
values (20180002, 8.33, 20180002);

INSERT INTO ordered_products(ordered_product_id, product_id, seller_id, order_id, amount, price)
VALUES(20180001, 20180001, 20180001, 20180001, 5, 300.20);
INSERT INTO ordered_products(ordered_product_id, product_id, seller_id, order_id, amount, price)
VALUES(20180002, 20180002, 20180002, 20180001, 10, 423.11);
