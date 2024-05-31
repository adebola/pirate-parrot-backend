ALTER TABLE category
    ADD CONSTRAINT idx_category_category_name UNIQUE (category_name);