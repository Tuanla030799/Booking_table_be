create database sunshine;
use sunshine;

CREATE TABLE `tbl_deposit`
(
    `DepositId` bigint primary key AUTO_INCREMENT,
    `Deposit` bigint DEFAULT 0,
    `TotalPersons` int DEFAULT 0,
    `Created` DATETIME DEFAULT NOW()
);

CREATE TABLE `tbl_table`
(
    `Id` bigint primary key AUTO_INCREMENT,
    `TableName` NVARCHAR(20) not null unique,
    `Seat` int DEFAULT 0
);

CREATE TABLE `tbl_point`
(
    `PointId` bigint primary key AUTO_INCREMENT,
    `Price` bigint DEFAULT 0,
    `PointPercent` float DEFAULT 0,
    `Created` DATETIME DEFAULT NOW()
);

CREATE TABLE `tbl_customer`
(
    `Id`  bigint PRIMARY KEY AUTO_INCREMENT,
    `Email` NVARCHAR(40) not null unique,
    `PhoneNumber` VARCHAR(15),
    `FullName` NVARCHAR(255) ,
    `PassWord` varchar(255),
    `TotalMoney` bigint DEFAULT 0
);

CREATE TABLE `tbl_bankaccount`
(
    `Id` bigint primary key AUTO_INCREMENT,
    `AccountNo` NVARCHAR(20) not null unique,
    `Balance` bigint DEFAULT 0,
    `Status` int DEFAULT 0,
    `Email` NVARCHAR(40),
    constraint fk_BankaccountCustomer foreign key (Email) references tbl_customer(Email)
);

CREATE TABLE tbl_booking
(
    `id` bigint primary key AUTO_INCREMENT,
    `BookingID` NVARCHAR(255) not null unique,
    `Email` NVARCHAR(40),
    `BookingTime` datetime,
    `TotalSeats` int DEFAULT 0,
    `DepositId` bigint DEFAULT 0,
    `bookingStatus` int DEFAULT 0,
    constraint fk_BookingDeposit foreign key (DepositId) references tbl_deposit(DepositId),
    constraint fk_BookingCustomer foreign key (Email) references tbl_customer(Email)
);

CREATE TABLE tbl_bookinginfo
(
    `Id` bigint primary key AUTO_INCREMENT,
    `BookingID` NVARCHAR(255) ,
    `TableName` NVARCHAR(20),
    constraint fk_BookinginfoBooking foreign key (BookingID) references tbl_booking(BookingID),
    constraint fk_BookinginfoTable foreign key (TableName) references tbl_table(TableName)
);

create table `tbl_bill`
(
    `id`  bigint primary key AUTO_INCREMENT,
    `BillId` NVARCHAR(255) not null unique,
    `PointID` bigint,
    `BookingID` NVARCHAR(255),
    `Discount` BIGINT DEFAULT 0,
    `BillStatus` int default 0,
    `PayDate` datetime default 0,
    constraint fk_BillPoints foreign key (PointID) references tbl_point(PointID),
    constraint fk_BillBooking foreign key (BookingID) references tbl_booking(BookingID)
);

create table tbl_food
(
    `FoodId` bigint primary key AUTO_INCREMENT,
    `FoodName` NVARCHAR(30),
    `FoodImage` text,
    `FoodPrice` bigint DEFAULT 0,
    `FoodStatus` int default 1,
    `Created` DATETIME DEFAULT NOW()
);

create table tbl_billinfo
(
    `Id` bigint primary key AUTO_INCREMENT,
    `BillID` NVARCHAR(255),
    `Quantity` int,
    `FoodID` BIGINT,
    constraint fk_BillInfoBill foreign key (`BillID`) references tbl_bill(`BillID`),
    constraint fk_BillInfoFood foreign key (`FoodID`) references tbl_food(`FoodID`)
);

alter table tbl_food
    add constraint food_price check (`FoodPrice` >= 0);

alter table tbl_deposit
    add constraint check_deposit check (`Deposit` >= 0);

alter table tbl_bankaccount
    add constraint check_balance check (`Balance` >= 0);

alter table tbl_customer
    add constraint check_total_money check (TotalMoney >= 0);

alter table tbl_point
    add constraint check_point_price check (Price >= 0);

alter table tbl_billinfo
    add constraint check_quantity_billinfo check (Quantity >= 0);