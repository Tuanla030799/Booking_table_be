-- create database sunshine;
use sunshine;
-- drop database sunshine;
CREATE TABLE `tbl_deposit`
(
    `depositId` bigint primary key AUTO_INCREMENT,
    `deposit` bigint DEFAULT 0,
    `totalPersons` int DEFAULT 0,
    `depositStatus` int default 1,
    `created` DATETIME DEFAULT NOW()
);


create table tbl_role
(
    `id` bigint primary key AUTO_INCREMENT,
    `role` NVARCHAR(20)   unique,
    `roleName` NVARCHAR(20)
);

CREATE TABLE `tbl_table`
(
    `Id` bigint primary key AUTO_INCREMENT,
    `TableName` NVARCHAR(20)   unique,
    `Seat` int DEFAULT 0,
    `StillEmpty` int
);

CREATE TABLE `tbl_Beneficiary`
(
    `Id` bigint primary key AUTO_INCREMENT,
    `beneficiaryName` VARCHAR(20)   unique,
    `beneficiaryStatus` int DEFAULT 1,
    `totalBill` bigint  ,
    `Created` DATETIME DEFAULT NOW()
);


CREATE TABLE `tbl_point`
(
    `PointId` bigint primary key AUTO_INCREMENT,
    `Price` bigint DEFAULT 0,
    `PointPercent` float DEFAULT 0,
    `pointStatus` int Default 1,
    `Created` DATETIME DEFAULT NOW()
);

CREATE TABLE `tbl_Sale`
(
    `saleId` bigint primary key AUTO_INCREMENT,
    `beneficiary` VARCHAR(20),
    `Created` DATETIME DEFAULT NOW(),
    `percentDiscount` float DEFAULT 0,
    `saleDetail` NVARCHAR(10000)  ,
    `saleImage` NVARCHAR(255)  ,
    `saleStatus` int DEFAULT 1,
    `saleTitle` NVARCHAR(255),
    constraint tbl_SaleBeneficiary foreign key (beneficiary) references tbl_Beneficiary(beneficiaryName)
);

CREATE TABLE `tbl_News`
(
    `newsId` bigint primary key AUTO_INCREMENT,
    `created` DATETIME DEFAULT NOW(),
    `newsDetail` NVARCHAR(10000)  ,
    `newsImage` NVARCHAR(255)  ,
    `newsStatus` int DEFAULT 1,
    `newsTitle` NVARCHAR(255)

);

create table `TokenLiving`
(
    `Id` bigint primary key AUTO_INCREMENT,
    `Email` NVARCHAR(40)   unique,
    `created` DATETIME DEFAULT NOW(),
    `updated` DATETIME DEFAULT NOW(),
    `token` NVARCHAR(1000)
);

CREATE TABLE `tbl_customer`
(
    `Id`  bigint primary key AUTO_INCREMENT,
    `Email` NVARCHAR(40)   unique,
    `PhoneNumber` VARCHAR(15)  ,
    `FullName` NVARCHAR(100)  ,
    `PassWord` varchar(300)  ,
    `TotalMoney` bigint DEFAULT 0,
    `beneficiary` VARCHAR(20)  ,
    `role` NVARCHAR(20)  ,
    constraint tbl_customerBeneficiary foreign key (beneficiary) references tbl_Beneficiary(beneficiaryName),
    constraint tbl_customerRole foreign key (role) references tbl_role(role)

);


CREATE TABLE `tbl_booking`
(
    `id` bigint primary key AUTO_INCREMENT,
    `BookingID` NVARCHAR(255) unique,
    `bookingStatus` int DEFAULT 0,
    `BookingTime` datetime,
    `Created` DATETIME DEFAULT NOW(),
    `DepositId` bigint DEFAULT 0,
    `Email` NVARCHAR(40) ,
    `TotalSeats` int DEFAULT 0 ,
    `saleId` bigint,
    `TableName` NVARCHAR(20),
    constraint fk_BookingDeposit foreign key (DepositId) references tbl_deposit(DepositId),
    constraint fk_BookingSale foreign key (saleId) references tbl_Sale(saleId),
    constraint fk_BookingCustomer foreign key (Email) references tbl_customer(Email),
    constraint fk_BookingTable foreign key (TableName) references tbl_table(TableName)
);


create table `tbl_bill`
(
    `id`  bigint primary key AUTO_INCREMENT,
    `BillId` NVARCHAR(255)   unique,
    `PointID` bigint,
    `BookingID` NVARCHAR(255),
    `Discount` BIGINT DEFAULT 0,
    `BillStatus` int default 0,
    `PayDate` datetime,
    constraint fk_BillPoints foreign key (PointID) references tbl_point(PointID),
    constraint fk_BillBooking foreign key (BookingID) references tbl_booking(BookingID)
);

create table `tbl_food`
(
    `FoodId` bigint primary key AUTO_INCREMENT  ,
    `Created` DATETIME DEFAULT NOW(),
    `describes` NVARCHAR(10000)  ,
    `FoodName` NVARCHAR(255)  ,
    `FoodImage` NVARCHAR(255),
    `FoodPrice` bigint DEFAULT 0,
    `FoodStatus` int default 1
);

create table `tbl_billinfo`
(
    `Id` bigint primary key AUTO_INCREMENT,
    `BillID` NVARCHAR(255),
    `Quantity` int,
    `FoodID` BIGINT,
    constraint fk_BillInfoBill foreign key (`BillID`) references tbl_bill(`BillID`),
    constraint fk_BillInfoFood foreign key (`FoodID`) references tbl_food(`FoodID`)
);


create table `conf_schedules`
(
    `Id` bigint primary key AUTO_INCREMENT,
    `active` bit,
    `bean` nvarchar(100),
    `code` nvarchar(100),
    `cron` nvarchar(100),
    `description` nvarchar(100),
    `name` nvarchar(100)
);

alter table tbl_food
    add constraint food_price check (`FoodPrice` >= 0);

alter table tbl_deposit
    add constraint check_deposit check (`Deposit` >= 0);

alter table tbl_customer
    add constraint check_total_money check (TotalMoney >= 0);

alter table tbl_point
    add constraint check_point_price check (Price >= 0);

alter table tbl_billinfo
    add constraint check_quantity_billinfo check (Quantity > 0);


INSERT INTO tbl_deposit(Deposit, TotalPersons,depositStatus) VALUES (100000, 2,1);
INSERT INTO tbl_deposit(Deposit, TotalPersons,depositStatus) VALUES ( 200000, 4,1);
INSERT INTO tbl_deposit( Deposit, TotalPersons,depositStatus) VALUES ( 300000, 8,1);
INSERT INTO tbl_deposit( Deposit, TotalPersons,depositStatus) VALUES ( 300000, 8,1);
INSERT INTO tbl_deposit(Deposit, TotalPersons,depositStatus) VALUES ( 100000, 1,1);
INSERT INTO tbl_deposit(Deposit, TotalPersons,depositStatus) VALUES ( 500000, 1000,1);



INSERT INTO tbl_table(TableName, Seat,StillEmpty) VALUES (N'Bàn 1', 2,1);
INSERT INTO tbl_table(TableName, Seat,StillEmpty) VALUES (N'Bàn 2', 2,3);
INSERT INTO tbl_table(TableName, Seat,StillEmpty) VALUES (N'Bàn 3', 4,3);
INSERT INTO tbl_table(TableName, Seat,StillEmpty) VALUES (N'Bàn 4', 4,6);
INSERT INTO tbl_table(TableName, Seat,StillEmpty) VALUES (N'Bàn 5', 6,6);
INSERT INTO tbl_table(TableName, Seat,StillEmpty) VALUES (N'Bàn 6', 6,1);
INSERT INTO tbl_table(TableName, Seat,StillEmpty) VALUES (N'Bàn 7', 15,10);
INSERT INTO tbl_table(TableName, Seat,StillEmpty) VALUES (N'Bàn 8', 15,2);
INSERT INTO tbl_table(TableName, Seat,StillEmpty) VALUES (N'Bàn 9', 16,10);
INSERT INTO tbl_table(TableName, Seat,StillEmpty) VALUES (N'Bàn 10', 17,1);
INSERT INTO tbl_table(TableName, Seat,StillEmpty) VALUES (N'Bàn 11', 18,10);
INSERT INTO tbl_table(TableName, Seat,StillEmpty) VALUES (N'Bàn 12', 25,10);
INSERT INTO tbl_table(TableName, Seat,StillEmpty) VALUES (N'Bàn 13', 30,2);
INSERT INTO tbl_table(TableName, Seat,StillEmpty) VALUES (N'Bàn 14', 40,2);


INSERT INTO tbl_point(Price, PointPercent) VALUES (0, 0);
INSERT INTO tbl_point(Price, PointPercent) VALUES (1000000, 0.05);
INSERT INTO tbl_point(Price, PointPercent) VALUES (2000000, 0.1);
INSERT INTO tbl_point(Price, PointPercent) VALUES (3000000, 0.15);
INSERT INTO tbl_point(Price, PointPercent) VALUES (3000000, 0.17);
INSERT INTO tbl_point(Price, PointPercent) VALUES (1000000000, 0.2);


Insert into tbl_Beneficiary(beneficiaryName,beneficiaryStatus,totalBill) values('VIP',1,100000);
Insert into tbl_Beneficiary(beneficiaryName,beneficiaryStatus,totalBill) values('REGULAR',1,50000);
Insert into tbl_Beneficiary(beneficiaryName,beneficiaryStatus,totalBill) values('CUSTOMER',1,10000);


insert into tbl_role(role,roleName) values('USERS',N'Khách Hàng');
insert into tbl_role(role,roleName) values('ADMIN',N'Nhân Viên');


INSERT INTO tbl_customer(Email, PhoneNumber, FullName, Password, TotalMoney,role,beneficiary) VALUES ('pvminh@gmail.com', '0796164361', N'Phí Văn Minh', '$2a$10$v.MczrO5SeLbLwKEuIih1OPOfSldACzBd9RUKAExt4ALbtC34e5w2', 5000,'USERS','CUSTOMER');
INSERT INTO tbl_customer(Email, PhoneNumber, FullName, Password, TotalMoney,role,beneficiary) VALUES ('sunshine87lethanhnghi@gmail.com', '0999999999', N'Nhà hàng Sunshine', '$2a$10$v.MczrO5SeLbLwKEuIih1OPOfSldACzBd9RUKAExt4ALbtC34e5w2', 8000,'ADMIN','VIP');
INSERT INTO tbl_customer(Email, PhoneNumber, FullName, Password, TotalMoney,role,beneficiary) VALUES ('haind1@vimo.vn', '0978675678', N'Nguyễn Đình Hải', '$2a$10$v.MczrO5SeLbLwKEuIih1OPOfSldACzBd9RUKAExt4ALbtC34e5w2', 5000,'USERS','CUSTOMER');
