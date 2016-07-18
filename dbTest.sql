DROP DATABASE IF EXISTS myDatabase;
CREATE DATABASE myDatabase;
USE myDatabase;

CREATE TABLE Person(
PersonID int(11) NOT NULL,
LastName varchar(255) DEFAULT NULL,
FirstName varchar(255) DEFAULT NULL,
PRIMARY KEY(PersonID)
);

--
-- Dumping data for table Person --
INSERT INTO Person VALUES(100,'Kalokyri','Valia');
INSERT INTO Person VALUES(200,'MirandaGarcia','Antonio');
INSERT INTO Person VALUES(300,'Snow','Jon');
