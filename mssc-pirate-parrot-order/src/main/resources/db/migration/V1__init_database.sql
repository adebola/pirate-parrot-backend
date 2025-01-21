CREATE TABLE `order_shipment` (
    `id` VARCHAR(36) NOT NULL,
    `address` VARCHAR(256),
    `shipment_status` smallint NOT NULL DEFAULT 1,
    PRIMARY KEY (id)
);

CREATE TABLE `order` (
    `id` VARCHAR(36) NOT NULL,
    `order_date` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `order_status` smallint NOT NULL DEFAULT 1,
    `total_price` DECIMAL(13, 2) NOT NULL,
    `user_id` varchar(36),
    `user_name` varchar(64),
    `order_shipment_id` VARCHAR(36) NOT NULL,
    FOREIGN KEY (`order_shipment_id`) REFERENCES `order_shipment`(`id`),
    PRIMARY KEY (`id`)
);

CREATE TABLE `order_item` (
    `id` VARCHAR(36) NOT NULL,
    `product_id` varchar(36) NOT NULL,
    `unit_price` DECIMAL(13,2) NOT NULL,
    `quantity` INTEGER NOT NULL,
    `discount` DECIMAL(13, 2) NOT NULL DEFAULT 0,
    `subtotal_price` DECIMAL(13, 2) NOT NULL,
    `order_id` varchar(36) NOT NULL,
    FOREIGN KEY (`order_id`) REFERENCES `order`(`id`),
    PRIMARY KEY (`id`)
);