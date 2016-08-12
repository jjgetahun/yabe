DROP DATABASE IF EXISTS proj2016;
CREATE DATABASE proj2016;
USE proj2016;

CREATE TABLE Account (
  AccountID     INT(11)      NOT NULL AUTO_INCREMENT,
  Name          VARCHAR(255) NOT NULL,
  UserName      VARCHAR(255) NOT NULL,
  PassWord      VARCHAR(255) NOT NULL,
  isAdmin       TINYINT(1)   NOT NULL DEFAULT 0,
  isCustomerRep TINYINT(1)   NOT NULL DEFAULT 0,
  DateCreated   TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (AccountID)
);

INSERT INTO Account (Name, UserName, PassWord, isAdmin, isCustomerRep) VALUES('Admin', 'admin', 'password', '1', '0');

CREATE TABLE Item (
  ModelNumber  INT(11)      NOT NULL,
  Type         VARCHAR(200) NOT NULL,
  Pockets      VARCHAR(200),
  Material     VARCHAR(200),
  Waterproof   VARCHAR(200),
  Color        VARCHAR(200),
  Capacity     VARCHAR(200),
  SpareParts   VARCHAR(200),
  Battery      VARCHAR(200),
  Rechargeable VARCHAR(200),
  LED          VARCHAR(200),
  PRIMARY KEY (ModelNumber)
);

CREATE TABLE Auction (
  AuctionID     INT(11)     NOT NULL AUTO_INCREMENT,
  Name          VARCHAR(50) NOT NULL,
  SellerID      INT(11)     NOT NULL,
  ItemID        INT(11)     NOT NULL,
  StartTime     TIMESTAMP   NOT NULL DEFAULT '1970-01-01 00:00:01',
  EndTime       TIMESTAMP   NOT NULL DEFAULT '2037-01-19 03:14:07',
  StartingPrice FLOAT(7, 2) NOT NULL DEFAULT 0.01,
  Reserve       FLOAT(7, 2),
  Description   VARCHAR(255),
  HasEnded      TINYINT(1)           DEFAULT 0,
  Cond          VARCHAR(255),
  PRIMARY KEY (AuctionID),
  FOREIGN KEY (SellerID) REFERENCES Account (AccountID)
);

CREATE INDEX ix_Model ON Item(ModelNumber);

CREATE TABLE Alert (
  AccountID   INT(11) NOT NULL,
  ModelNumber INT(11) NOT NULL,
  PRIMARY KEY (AccountID, ModelNumber),
  FOREIGN KEY (AccountID) REFERENCES Account (AccountID)
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
  FOREIGN KEY (PosterID) REFERENCES Account (AccountID),
  FOREIGN KEY (QuestionID) REFERENCES Question (QuestionID)
);