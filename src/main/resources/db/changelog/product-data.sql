-- Sample product data
INSERT INTO product (id, description) VALUES (1, 'Laptop Computer');
INSERT INTO product (id, description) VALUES (2, 'Wireless Mouse');
INSERT INTO product (id, description) VALUES (3, 'USB-C Cable');
INSERT INTO product (id, description) VALUES (4, 'Monitor Stand');
INSERT INTO product (id, description) VALUES (5, 'Keyboard Mechanical');
INSERT INTO product (id, description) VALUES (6, 'Desk Lamp LED');
INSERT INTO product (id, description) VALUES (7, 'Phone Case');
INSERT INTO product (id, description) VALUES (8, 'Screen Protector');
INSERT INTO product (id, description) VALUES (9, 'Charger Fast');
INSERT INTO product (id, description) VALUES (10, 'Power Bank');

-- Link products to orders (each order can have multiple products)
INSERT INTO order_product (order_id, product_id) VALUES (1, 1);
INSERT INTO order_product (order_id, product_id) VALUES (1, 2);
INSERT INTO order_product (order_id, product_id) VALUES (2, 3);
INSERT INTO order_product (order_id, product_id) VALUES (2, 4);
INSERT INTO order_product (order_id, product_id) VALUES (3, 5);
INSERT INTO order_product (order_id, product_id) VALUES (4, 1);
INSERT INTO order_product (order_id, product_id) VALUES (4, 6);
INSERT INTO order_product (order_id, product_id) VALUES (5, 7);
INSERT INTO order_product (order_id, product_id) VALUES (5, 8);
INSERT INTO order_product (order_id, product_id) VALUES (6, 9);
INSERT INTO order_product (order_id, product_id) VALUES (6, 10);
INSERT INTO order_product (order_id, product_id) VALUES (7, 2);
INSERT INTO order_product (order_id, product_id) VALUES (8, 3);
INSERT INTO order_product (order_id, product_id) VALUES (9, 5);
INSERT INTO order_product (order_id, product_id) VALUES (10, 1);

