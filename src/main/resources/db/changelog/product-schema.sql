-- Create product table
CREATE TABLE product (
    id BIGSERIAL PRIMARY KEY,
    description VARCHAR(255) NOT NULL
);

-- Create order_product join table
CREATE TABLE order_product (
    order_id BIGINT NOT NULL,
    product_id BIGINT NOT NULL,
    PRIMARY KEY (order_id, product_id),
    CONSTRAINT fk_order_product_order FOREIGN KEY (order_id) REFERENCES "order" (id) ON DELETE CASCADE,
    CONSTRAINT fk_order_product_product FOREIGN KEY (product_id) REFERENCES product (id) ON DELETE CASCADE
);

-- Create indexes for performance optimization
CREATE INDEX idx_product_description ON product (description);
CREATE INDEX idx_order_product_order ON order_product (order_id);
CREATE INDEX idx_order_product_product ON order_product (product_id);
CREATE INDEX idx_customer_name ON customer (name);
CREATE INDEX idx_order_customer_id ON "order" (customer_id);

