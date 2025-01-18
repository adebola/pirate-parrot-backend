DROP TABLE IF EXISTS `users`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;

CREATE TABLE `users` (
    `id` varchar(36) NOT NULL DEFAULT (UUID()),
    `user_name` varchar(64) NOT NULL,
    `first_name` varchar(64) NOT NULL,
    `last_name` varchar(64) NOT NULL,
    `email` varchar(64) NOT NULL,
    `created_on` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
    `enabled` tinyint(1) DEFAULT '0' NOT NULL,
    `locked` tinyint(1) DEFAULT '0' NOT NULL,
    UNIQUE (user_name),
    UNIQUE (email),
    PRIMARY KEY (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

DROP TABLE IF EXISTS `authorities`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;

CREATE TABLE `authorities` (
    `id` varchar(36) NOT NULL DEFAULT (UUID()),
    `authority` varchar(64) NOT NULL,
    UNIQUE (authority),
    PRIMARY KEY (id)
);

DROP TABLE IF EXISTS `users_authorities`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;

CREATE TABLE `users_authorities` (
    `user_id` varchar(36) NOT NULL,
    `authority_id` varchar(36) NOT NULL,
    FOREIGN KEY (`user_id`) REFERENCES `users`(`id`),
    FOREIGN KEY (`authority_id`) REFERENCES `authorities`(id)
);