CREATE TABLE IF NOT EXISTS java.products (
    id              BIGSERIAL PRIMARY KEY,
    account_number  VARCHAR(50)     NOT NULL UNIQUE,
    balance         NUMERIC(19, 2)  NOT NULL DEFAULT 0,
    product_type    VARCHAR(20)     NOT NULL,
    user_id         BIGINT          NOT NULL,
    CONSTRAINT fk_products_user
        FOREIGN KEY (user_id) REFERENCES java.users (id)
        ON DELETE CASCADE
);

CREATE INDEX IF NOT EXISTS idx_products_user_id ON java.products (user_id);