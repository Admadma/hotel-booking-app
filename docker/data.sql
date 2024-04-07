CREATE DATABASE IF NOT EXISTS hotel_db;
CREATE USER 'springuser'@'%' identified by 'ThePassword';
GRANT ALL ON hotel_db.* TO 'springuser'@'%';
