CREATE TYPE IF NOT EXISTS ProductOption (
    id BIGINT,
    product_id BIGINT,
    name TEXT,
    values list<TEXT>
);
CREATE TYPE IF NOT EXISTS ProductVariant (
    title TEXT,
    id BIGINT,
    product_id BIGINT,
    sku TEXT,
    created_at TEXT,
    updated_at TEXT,
    price TEXT,
    option1 TEXT,
    option2 TEXT,
    option3 TEXT
);
CREATE TYPE IF NOT EXISTS ProductImage (
    id BIGINT,
    position INT,
    product_id BIGINT,
    variant_id BIGINT,
    created_at TEXT,
    updated_at TEXT,
    src TEXT
);
CREATE TABLE IF NOT EXISTS product_tbl_test (
    id BIGINT PRIMARY KEY,
    title TEXT,
    description TEXT,
    created_at TEXT,
    updated_at TEXT,
    vendor TEXT,
    status TEXT,
    options list<FROZEN<ProductOption>>,
    variants list<FROZEN<ProductVariant>>,
    images list<FROZEN<ProductImage>>
);
