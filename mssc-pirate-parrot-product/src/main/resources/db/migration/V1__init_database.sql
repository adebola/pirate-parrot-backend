CREATE TABLE `category`
(
    `id`                 varchar(36) NOT NULL DEFAULT (UUID()),
    `category_name`      varchar(64) NOT NULL,
    `category_image_url` varchar(128) NOT NULL,
    `created_on`         timestamp   NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `created_by`         varchar(64) NOT NULL,
    `suspended`          tinyint     NOT NULL DEFAULT 0,
    PRIMARY KEY (`id`)
);

CREATE TABLE `product`
(
    `id`                varchar(36) NOT NULL DEFAULT (UUID()),
    `category_id`       varchar(36) NOT NULL,
    `product_name`      varchar(64) NOT NULL,
    `description`       varchar(512),
    `product_image_url` varchar(128),
    `brand`             varchar(64),
    `supplier_id`       varchar(36),
    `created_on`        timestamp   NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `created_by`        varchar(64) NOT NULL,
    `suspended`         tinyint     NOT NULL DEFAULT 0,
    FOREIGN KEY (`category_id`) REFERENCES category (`id`),
    PRIMARY KEY (`id`)
);

CREATE TABLE `product_variant`
(
    `id`         varchar(36) NOT NULL DEFAULT (UUID()),
    `name`       varchar(64) NOT NULL,
    `created_on` timestamp   NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `created_by` varchar(64) NOT NULL,
    `suspended`  tinyint     NOT NULL DEFAULT 0,
    PRIMARY KEY (`id`)
);

CREATE TABLE `product_variant_option`
(
    `id`         varchar(36) NOT NULL DEFAULT (UUID()),
    `name`       varchar(64) NOT NULL,
    `created_on` timestamp   NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `created_by` varchar(64) NOT NULL,
    `suspended`  tinyint     NOT NULL DEFAULT 0,
    PRIMARY KEY (`id`)
);

CREATE TABLE `uom`
(
    `id`         varchar(36) NOT NULL DEFAULT (UUID()),
    `name`       varchar(64) NOT NULL,
    `created_on` timestamp   NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `created_by` varchar(64) NOT NULL,
    `suspended`  tinyint     NOT NULL DEFAULT 0,
    PRIMARY KEY (`id`)
);

CREATE TABLE `product_variant_option_mapping`
(
    `product_variant_id`        varchar(36)    NOT NULL,
    `product_variant_option_id` varchar(36)    NOT NULL,
    `uom_id`                    varchar(36)    NOT NULL,
    `product_id`                varchar(36)    NOT NULL,
    `price`                     DECIMAL(13, 2) NOT NULL DEFAULT 0,
    `discount`                  DECIMAL(13, 2) NOT NULL DEFAULT 0,
    `quantity`                  INTEGER        NOT NULL DEFAULT 0,
    `new`                       TINYINT        NOT NULL DEFAULT 0,
    `sale`                      TINYINT        NOT NULL DEFAULT 0,
    `created_on`                timestamp      NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `created_by`                varchar(64)    NOT NULL,
    `suspended`                 tinyint        NOT NULL DEFAULT 0,
    FOREIGN KEY (`product_variant_id`) REFERENCES product_variant (`id`),
    FOREIGN KEY (`product_variant_option_id`) REFERENCES product_variant_option (`id`),
    FOREIGN KEY (`uom_id`) REFERENCES uom (`id`),
    FOREIGN KEY (`product_id`) REFERENCES product (`id`)
);