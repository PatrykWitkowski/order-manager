insert into products (product_id, user_id, order_col, name, type, description, product_website_url)
values (1, 1, null, 'Product 1', 'Type 1', null, null);

insert into seller_prices (product_product_id, prices, seller_id)
values (1, 10.22, 1);

insert into seller_prices (product_product_id, prices, seller_id)
values (1, 43.10, 2);

insert into seller_prices (product_product_id, prices, seller_id)
values (1, 68, 3);

insert into products (product_id, user_id, order_col, name, type, description, product_website_url)
values (2, 1, null, 'Product 2', 'Type 2', null, null);

insert into seller_prices (product_product_id, prices, seller_id)
values (2, 105.28, 1);

insert into seller_prices (product_product_id, prices, seller_id)
values (2, 8.33, 2);
