DROP DATABASE IF EXISTS proj2016;
CREATE DATABASE proj2016;
USE proj2016;

CREATE TABLE Account (
  AccountID   INT(11)      NOT NULL AUTO_INCREMENT,
  Name        VARCHAR(255) NOT NULL,
  UserName    VARCHAR(255) NOT NULL,
  PassWord    VARCHAR(255) NOT NULL,
  DateCreated TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (AccountID)
);

INSERT INTO Account (Name, UserName, PassWord) VALUES('Elby Basolis', 'ebasolis', 'pass1');
INSERT INTO Account (Name, UserName, PassWord) VALUES('Arnold Trakhtenberg', 'atrakh', 'pass2');
INSERT INTO Account (Name, UserName, PassWord) VALUES('Jon Getahun', 'jget', 'pass3');
INSERT INTO Account (Name, UserName, PassWord) VALUES('New User', 'nuser', 'pass4');
INSERT INTO Account (Name, UserName, PassWord) Values('Customer Rep 1', 'crep1', 'pass5');

CREATE TABLE Admin (
  AccountID INT(11) NOT NULL,
  PRIMARY KEY (AccountID),
  FOREIGN KEY (AccountID) REFERENCES Account (AccountID)
);

INSERT INTO Admin VALUES(1);
INSERT INTO Admin VALUES(2);
INSERT INTO Admin VALUES(3);

CREATE TABLE Customer_Rep (
  AccountID INT(11) NOT NULL,
  PRIMARY KEY (AccountID),
  FOREIGN KEY (AccountID) REFERENCES Account (AccountID)
);

INSERT INTO Customer_Rep Values(5);

CREATE TABLE Item (
  ModelNumber INT(11) NOT NULL,
  A1          VARCHAR(200),
  A2          VARCHAR(200),
  A3          VARCHAR(200),
  B1          VARCHAR(200),
  B2          VARCHAR(200),
  B3          VARCHAR(200),
  C1          VARCHAR(200),
  C2          VARCHAR(200),
  C3          VARCHAR(200),
  PRIMARY KEY (ModelNumber)
);

CREATE TABLE Auction (
  AuctionID     INT(11)     NOT NULL AUTO_INCREMENT,
  SellerID      INT(11)     NOT NULL,
  ItemID        INT(11)     NOT NULL,
  StartTime     TIMESTAMP   NOT NULL DEFAULT '1970-01-01 00:00:01',
  EndTime       TIMESTAMP   NOT NULL DEFAULT '2037-01-19 03:14:07',
  StartingPrice FLOAT(7, 2) NOT NULL DEFAULT 0.01,
  Reserve       FLOAT(7, 2),
  Description   VARCHAR(255),
  HasEnded      TINYINT(1)           DEFAULT 0,
  PRIMARY KEY (AuctionID),
  FOREIGN KEY (SellerID) REFERENCES Account (AccountID),
  FOREIGN KEY (ItemID) REFERENCES Item (ModelNumber)
);

CREATE INDEX ix_Model ON Item(ModelNumber);

CREATE TABLE Alert (
  AccountID   INT(11) NOT NULL,
  ModelNumber INT(11) NOT NULL,
  PRIMARY KEY (AccountID, ModelNumber),
  FOREIGN KEY (AccountID) REFERENCES Account (AccountID),
  FOREIGN KEY (ModelNumber) REFERENCES Item (ModelNumber)
);

CREATE TABLE Bid (
  Time      TIMESTAMP   NOT NULL DEFAULT CURRENT_TIMESTAMP,
  Amount    FLOAT(7, 2) NOT NULL,
  BidderID  INT(11)     NOT NULL,
  AuctionID INT(11)     NOT NULL,
  IsAuto    TINYINT(1)  NOT NULL,
  PRIMARY KEY (BidderID, AuctionID, Amount),
  FOREIGN KEY (BidderID) REFERENCES Account (AccountID),
  FOREIGN KEY (AuctionID) REFERENCES Auction (AuctionID)
);

CREATE TABLE Message (
  MessageID  INT(15)      NOT NULL AUTO_INCREMENT,
  SenderID   INT(11)      NOT NULL DEFAULT -1,
  ReceiverID INT(11)      NOT NULL,
  Contents   VARCHAR(255) NOT NULL,
  ReadStatus TINYINT(1)   NOT NULL DEFAULT 0,
  TimeSent   TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (MessageID),
  FOREIGN KEY (SenderID) REFERENCES Account (AccountID),
  FOREIGN KEY (ReceiverID) REFERENCES Account (AccountID)
);

CREATE TABLE Question (
  QuestionID INT(15)      NOT NULL AUTO_INCREMENT,
  PosterID   INT(11)      NOT NULL,
  AuctionID  INT(11)      NOT NULL,
  Header     VARCHAR(50)  NOT NULL,
  Contents   VARCHAR(255) NOT NULL,
  TimePosted TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (QuestionID),
  FOREIGN KEY (PosterID) REFERENCES Account (AccountID),
  FOREIGN KEY (AuctionID) REFERENCES Auction (AuctionID)
);

CREATE TABLE Answer (
  AnswerID   INT(15)      NOT NULL AUTO_INCREMENT,
  PosterID   INT(11)      NOT NULL,
  QuestionID INT(15)      NOT NULL,
  Contents   VARCHAR(255) NOT NULL,
  TimePosted TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (AnswerID),
  FOREIGN KEY (PosterID) REFERENCES Customer_Rep (AccountID),
  FOREIGN KEY (QuestionID) REFERENCES Question (QuestionID)
);