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

CREATE TABLE Customer_Rep(
AccountID int(11) NOT NULL,
PRIMARY KEY(AccountID),
FOREIGN KEY(AccountID) references Account(AccountID)
);

INSERT INTO Customer_Rep Values(5);

CREATE TABLE Item(
ModelNumber int(11) NOT NULL,
  A1 varchar(200),
  A2 varchar(200),
  A3 varchar(200),
  B1 varchar(200),
  B2 varchar(200),
  B3 varchar(200),
  C1 varchar(200),
  C2 varchar(200),
  C3 varchar(200),
PRIMARY KEY(ModelNumber)
);

CREATE TABLE Auction(
AuctionID int(11) NOT NULL AUTO_INCREMENT,
SellerID int(11) NOT NULL,
ItemID int(11) NOT NULL,
StartTime timestamp NOT NULL DEFAULT '1970-01-01 00:00:01',
EndTime timestamp NOT NULL DEFAULT '2037-01-19 03:14:07',
StartingPrice float(7, 2) NOT NULL DEFAULT 0.01,
Reserve float(7, 2),
Description varchar(255),
HasEnded tinyint(1) DEFAULT 0,
PRIMARY KEY(AuctionID),
FOREIGN KEY(SellerID) references Account(AccountID),
FOREIGN KEY(ItemID) references Item(ModelNumber)
);

CREATE INDEX ix_Model ON Item(ModelNumber);

CREATE TABLE Alert(
AccountID int(11) NOT NULL,
ModelNumber int(11) NOT NULL,
PRIMARY KEY(AccountID, ModelNumber),
FOREIGN KEY(AccountID) references Account(AccountID),
FOREIGN KEY(ModelNumber) references Item(ModelNumber)
);

CREATE TABLE Bid(
Time timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
Amount float(7, 2) NOT NULL,
BidderID int(11) NOT NULL,
AuctionID int(11) NOT NULL,
IsAuto tinyint(1) NOT NULL,
PRIMARY KEY(BidderID, AuctionID, Amount),
FOREIGN KEY(BidderID) references Account(AccountID),
FOREIGN KEY(AuctionID) references Auction(AuctionID)
);

CREATE TABLE Messsage(
MessageID int(15) NOT NULL AUTO_INCREMENT,
SenderID int(11) NOT NULL DEFAULT -1,
ReceiverID int(11) NOT NULL,
Contents varchar(255) NOT NULL,
ReadStatus tinyint(1) NOT NULL DEFAULT 0,
TimeSent timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
PRIMARY KEY(MessageID),
FOREIGN KEY(SenderID) references Account(AccountID),
FOREIGN KEY(ReceiverID) references Account(AccountID)
);