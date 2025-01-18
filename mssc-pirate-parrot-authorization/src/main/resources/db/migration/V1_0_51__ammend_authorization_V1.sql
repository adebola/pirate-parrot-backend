alter table authorizations modify column `access_token_metadata` TEXT;
alter table authorizations modify column `refresh_token_value` varchar(1024);