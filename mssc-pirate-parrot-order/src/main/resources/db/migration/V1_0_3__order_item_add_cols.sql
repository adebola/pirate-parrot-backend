ALTER TABLE
    `order_item` ADD COLUMN variant varchar(64) NOT NULL;

ALTER TABLE
    `order_item` ADD COLUMN variant_option varchar(64) NOT NULL;

ALTER TABLE
    `order_item` ADD COLUMN uom varchar(64) NOT NULL;
