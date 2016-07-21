DROP DATABASE IF EXISTS proj2016;
CREATE DATABASE proj2016;
USE proj2016;

CREATE TABLE Account(
AccountID int(11) NOT NULL AUTO_INCREMENT,
Name varchar(255) NOT NULL,
UserName varchar(255) NOT NULL,
DateCreated timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
PRIMARY KEY(AccountID)
);

--
-- Dumping data for table Person --
INSERT INTO Account (Name, UserName) VALUES('Elby Basolis', 'ebasolis');
INSERT INTO Account (Name, UserName) VALUES('Arnold Trakhtenberg', 'atrakh');
INSERT INTO Account (Name, UserName) VALUES('Jon Getahun', 'jget');
INSERT INTO Account (Name, UserName) VALUES('New User', 'nuser');
