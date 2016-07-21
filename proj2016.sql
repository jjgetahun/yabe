DROP DATABASE IF EXISTS proj2016;
CREATE DATABASE proj2016;
USE proj2016;

CREATE TABLE Account(
AccountID int(11) NOT NULL AUTO_INCREMENT,
Name varchar(255) NOT NULL,
UserName varchar(255) NOT NULL,
PassWord varchar(255) NOT NULL,
DateCreated timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
PRIMARY KEY(AccountID)
);

INSERT INTO Account (Name, UserName, PassWord) VALUES('Elby Basolis', 'ebasolis', 'pass1');
INSERT INTO Account (Name, UserName, PassWord) VALUES('Arnold Trakhtenberg', 'atrakh', 'pass2');
INSERT INTO Account (Name, UserName, PassWord) VALUES('Jon Getahun', 'jget', 'pass3');
INSERT INTO Account (Name, UserName, PassWord) VALUES('New User', 'nuser', 'pass4');
INSERT INTO Account (Name, UserName, PassWord) Values('Customer Rep 1', 'crep1', 'pass5');

CREATE TABLE Admin(
AccountID int(11) NOT NULL,
PRIMARY KEY(AccountID),
FOREIGN KEY(AccountID) references Account(AccountID)
);

INSERT INTO Admin VALUES(1);
INSERT INTO Admin VALUES(2);
INSERT INTO Admin VALUES(3);

CREATE TABLE Buyer(
AccountID int(11) NOT NULL,
PRIMARY KEY(AccountID),
FOREIGN KEY(AccountID) references Account(AccountID)
);

INSERT INTO Buyer VALUES(4);

CREATE TABLE Customer_Rep(
AccountID int(11) NOT NULL,
PRIMARY KEY(AccountID),
FOREIGN KEY(AccountID) references Account(AccountID)
);

INSERT INTO Customer_Rep Values(5);

CREATE TABLE Seller(
AccountID int(11) NOT NULL,
PRIMARY KEY(AccountID),
FOREIGN KEY(AccountID) references Account(AccountID)
);

INSERT INTO Seller Values(4);
