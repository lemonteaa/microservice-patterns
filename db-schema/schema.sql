-- For demo'ing concepts only. Not following any best practise at all.

DROP TABLE IF EXISTS customers;
DROP TABLE IF EXISTS shipping;
DROP TABLE IF EXISTS store_item;
DROP TABLE IF EXISTS order;
DROP TABLE IF EXISTS order_item;
DROP TABLE IF EXISTS transaction_req;
DROP TABLE IF EXISTS outbox;

CREATE TABLE customers(
   customer_id SERIAL PRIMARY KEY,
   customer_name VARCHAR(255) NOT NULL
);

CREATE TABLE shipping(
   shipping_id SERIAL PRIMARY KEY,
   contact_name VARCHAR(255) NOT NULL,
   phone VARCHAR(15),
   email VARCHAR(100),
   shipping_address VARCHAR(500)
);

CREATE TABLE store_item(
    store_item_id SERIAL PRIMARY KEY,
    store_item_name VARCHAR(255) NOT NULL,
    store_item_description VARCHAR(500) NOT NULL
);

CREATE TABLE "order" (
    order_id SERIAL PRIMARY KEY,
    customer_id integer,
    shipping_id integer,
    FOREIGN KEY (customer_id) REFERENCES customers (customer_id),
    FOREIGN KEY (shipping_id) REFERENCES shipping (shipping_id)
);

CREATE TABLE order_item(
    order_item_id SERIAL PRIMARY KEY,
    store_item_id integer,
    quantity INT,
    total_price INT,
    order_id integer,
    FOREIGN KEY(store_item_id) REFERENCES store_item(store_item_id),
    FOREIGN KEY(order_id) REFERENCES "order" (order_id)
);


CREATE TABLE transaction_req(
    transaction_req_id SERIAL PRIMARY KEY,
    order_id integer UNIQUE, -- prevent duplicate trans
    transaction_req_status INT,
    FOREIGN KEY(order_id) REFERENCES "order" (order_id)
);

CREATE TABLE outbox(
    message_id SERIAL PRIMARY KEY,
    task_status INT,
    task_type INT,
    task_data integer
);
