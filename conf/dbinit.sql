CREATE DATABASE WHATDataWarehouse;
CREATE USER 'masteWHAT'@'localhost' IDENTIFIED BY 'whatUP';
GRANT ALL PRIVILEGES ON WHATDataWarehouse.* TO 'masteWHAT'@'localhost';