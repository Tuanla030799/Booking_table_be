create database sunshine;
use sunshine;
drop database sunshine;
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
    `role` NVARCHAR(20) not null unique,
	`roleName` NVARCHAR(20)
);

CREATE TABLE `tbl_table`
(
    `Id` bigint primary key AUTO_INCREMENT,
    `TableName` NVARCHAR(20) not null unique,
    `Seat` int DEFAULT 0,
	`StillEmpty` int not null
);

CREATE TABLE `tbl_Beneficiary`
(
	`Id` bigint primary key AUTO_INCREMENT,
    `beneficiaryName` VARCHAR(20) not null unique,
    `beneficiaryStatus` int DEFAULT 1,
	`totalBill` bigint not null,
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
	`saleDetail` NVARCHAR(255) not null,
	`saleImage` NVARCHAR(50) not null,
	`saleStatus` int DEFAULT 1,
	`saleTitel` NVARCHAR(100),
	constraint tbl_SaleBeneficiary foreign key (beneficiary) references tbl_Beneficiary(beneficiaryName)	  
);

CREATE TABLE `tbl_News`
(
	`newsId` bigint primary key AUTO_INCREMENT,
	`created` DATETIME DEFAULT NOW(),
	`newsDitail` NVARCHAR(255) not null,
	`newsImage` NVARCHAR(50) not null,
    `newsStatus` int DEFAULT 1,
    `newsTitel` NVARCHAR(100) not null
	
);

create table `TokenLiving`
(
	`Id` bigint primary key AUTO_INCREMENT,
	`Email` NVARCHAR(40) not null unique,
	`created` DATETIME DEFAULT NOW(),
	`updated` DATETIME DEFAULT NOW(),
	`token` NVARCHAR(1000) not null	
);

CREATE TABLE `tbl_customer`
(
	  `Id`  bigint primary key AUTO_INCREMENT,
      `Email` NVARCHAR(40) not null unique,
	  `PhoneNumber` VARCHAR(15) not null,
      `FullName` NVARCHAR(100) not null,
      `PassWord` varchar(300) not null,
      `TotalMoney` bigint DEFAULT 0,
	  `beneficiary` VARCHAR(20) not null,
	  `role` NVARCHAR(20) not null,
	  constraint tbl_customerBeneficiary foreign key (beneficiary) references tbl_Beneficiary(beneficiaryName),
	  constraint tbl_customerRole foreign key (role) references tbl_role(role)

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
    `BillId` NVARCHAR(255) not null unique,
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
    `FoodId` bigint primary key AUTO_INCREMENT not null,
	`Created` DATETIME DEFAULT NOW(),
	`describes` NVARCHAR(255) not null,
    `FoodName` NVARCHAR(30) not null,
	`FoodImage` NVARCHAR(30),
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
    
alter table tbl_bankaccount
    add constraint check_balance check (`Balance` >= 0);

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

insert into tbl_sale(saleTitel,saleDetail,saleImage,saleStatus,beneficiary,percentDiscount) values ('saleTitile','detail','image',1,'VIP',0.1);
insert into tbl_sale(saleTitel,saleDetail,saleImage,saleStatus,beneficiary,percentDiscount) values ('saleTitile','detail','image',1,'VIP',0.2);
insert into tbl_sale(saleTitel,saleDetail,saleImage,saleStatus,beneficiary,percentDiscount) values ('saleTitile','detail','image',1,'VIP',0.3);
insert into tbl_sale(saleTitel,saleDetail,saleImage,saleStatus,beneficiary,percentDiscount) values ('saleTitile','detail','image',1,'VIP',0.4);
insert into tbl_sale(saleTitel,saleDetail,saleImage,saleStatus,beneficiary,percentDiscount) values ('saleTitile','detail','image',1,'VIP',0.5);



insert into tbl_role(role,roleName) values('USERS',N'Khách Hàng');
insert into tbl_role(role,roleName) values('ADMIN',N'Nhân Viên');


INSERT INTO tbl_customer(Email, PhoneNumber, FullName, Password, TotalMoney,role,beneficiary) VALUES ('pvminh@gmail.com', '0796164361', N'Phí Văn Minh', '$2a$10$v.MczrO5SeLbLwKEuIih1OPOfSldACzBd9RUKAExt4ALbtC34e5w2', 5000,'USERS','CUSTOMER');
INSERT INTO tbl_customer(Email, PhoneNumber, FullName, Password, TotalMoney,role,beneficiary) VALUES ('sunshine87lethanhnghi@gmail.com', '0999999999', N'Nhà hàng Sunshine', '$2a$10$v.MczrO5SeLbLwKEuIih1OPOfSldACzBd9RUKAExt4ALbtC34e5w2', 8000,'ADMIN','VIP');
INSERT INTO tbl_customer(Email, PhoneNumber, FullName, Password, TotalMoney,role,beneficiary) VALUES ('haind1@vimo.vn', '0978675678', N'Nguyễn Đình Hải', '$2a$10$v.MczrO5SeLbLwKEuIih1OPOfSldACzBd9RUKAExt4ALbtC34e5w2', 5000,'USERS','CUSTOMER');
-- INSERT INTO tbl_customer(Email, PhoneNumber, FullName, Password, TotalMoney,role,beneficiary) VALUES ('tranphuduan@gmail.com', '0978675678', N'Nguyễn Đình Hải', '$2a$10$QldpC9h4Sw4jiR9Nmq5Mt.zKMLZOQxYt8tfZ16n.E935l64tOK4ma', 5000,'USERS','CUSTOMER');
-- update tbl_customer set Password='$2a$10$JtAQTcqWDiKf84h2oF/ytOF3yqouJuUAOdsgjGsBs5ALEQsDNcyLy' where Email='sunshine87lethanhnghi@gmail.com'


-- delete tbl_Customer where email='tranphuduan@gmail.com'

INSERT INTO tbl_BankAccount(AccountNo, Balance, Email, Status) VALUES ('5469879547877', 200000000, 'pvminh@gmail.com', 1);
INSERT INTO tbl_BankAccount(AccountNo, Balance, Email, Status) VALUES ('5469879547869', 10000, 'haind1@vimo.vn', 0);
INSERT INTO tbl_BankAccount(AccountNo, Balance, Email, Status) VALUES ('8686868686868', 0, 'sunshine87lethanhnghi@gmail.com', 1);
 

INSERT INTO tbl_food(FoodName, FoodPrice,describes,FoodImage) VALUES ( N'Cá mập', 50000,N'Cá mập','D:/');
INSERT INTO tbl_food(FoodName, FoodPrice,describes,FoodImage) VALUES ( N'Cá voi', 50000,N'Cá voi','D:/');
INSERT INTO tbl_food(FoodName, FoodPrice,describes,FoodImage) VALUES (N'Cá heo', 100000,N'Cá heo','D:/');
 

insert into conf_schedules(active, bean, code, cron, description, name)
values(1,'changeResetPasswordStatus','1','15 0/1 * * * *','test schedule in database','job chay vao giay thu 15 cua tung phut');

insert into conf_schedules(active, bean, code, cron, description, name)
values(1,'removeLiveToken','1','15 0/1 * * * *','test schedule in database','job xoa TokenLiving');

insert into conf_schedules(active, bean, code, cron, description, name)
values(1,'autoCancelBooking','1','15 0/1 * * * *','test schedule in database','job auto cancelbooking');

insert into conf_schedules(active, bean, code, cron, description, name)
values(1,'autoUpdateBeneficiary','1','15 0/1 * * * *','test schedule in database','job auto UpdateBeneficiary');

