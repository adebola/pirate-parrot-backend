alter table authorizations modify column `refresh_token_value` varchar(160);
alter table authorizations add index `idx_authorizations_refresh_token_value` (`refresh_token_value`);