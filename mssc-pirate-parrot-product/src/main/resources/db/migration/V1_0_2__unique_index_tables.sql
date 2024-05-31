ALTER TABLE product_variant
    ADD CONSTRAINT idx_product_variant_name UNIQUE (name);

ALTER TABLE product_variant_option
    ADD CONSTRAINT idx_product_variant_option_name UNIQUE (name);

ALTER TABLE uom
    ADD CONSTRAINT idx_uom_name UNIQUE (name);