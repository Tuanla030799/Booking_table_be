-- create database sunshine;
use
sunshine;
-- drop database sunshine;
CREATE TABLE `tbl_deposit`
(
    `depositId`     bigint primary key AUTO_INCREMENT,
    `deposit`       bigint   DEFAULT 0,
    `totalPersons`  int      DEFAULT 0,
    `depositStatus` int      default 1,
    `created`       DATETIME DEFAULT NOW()
);


create table tbl_role
(
    `id`       bigint primary key AUTO_INCREMENT,
    `role`     NVARCHAR(20) unique,
    `roleName` NVARCHAR(20)
);

CREATE TABLE `tbl_table`
(
    `Id`         bigint primary key AUTO_INCREMENT,
    `TableName`  NVARCHAR(20) unique,
    `Seat`       int DEFAULT 0,
    `StillEmpty` int
);

CREATE TABLE `tbl_Beneficiary`
(
    `Id`                bigint primary key AUTO_INCREMENT,
    `beneficiaryName`   VARCHAR(20) unique,
    `beneficiaryStatus` int      DEFAULT 1,
    `totalBill`         bigint,
    `Created`           DATETIME DEFAULT NOW()
);


CREATE TABLE `tbl_point`
(
    `PointId`      bigint primary key AUTO_INCREMENT,
    `Price`        bigint   DEFAULT 0,
    `PointPercent` float    DEFAULT 0,
    `pointStatus`  int      Default 1,
    `Created`      DATETIME DEFAULT NOW()
);

CREATE TABLE `tbl_Sale`
(
    `saleId`          bigint primary key AUTO_INCREMENT,
    `beneficiary`     VARCHAR(20),
    `Created`         DATETIME DEFAULT NOW(),
    `percentDiscount` float    DEFAULT 0,
    `saleDetail`      NVARCHAR(10000),
    `saleImage`       NVARCHAR(255),
    `saleStatus`      int      DEFAULT 1,
    `saleTitle`       NVARCHAR(255),
    `totalBill` float    DEFAULT 0,
    constraint tbl_SaleBeneficiary foreign key (beneficiary) references tbl_Beneficiary (beneficiaryName)
);

CREATE TABLE `tbl_News`
(
    `newsId`     bigint primary key AUTO_INCREMENT,
    `created`    DATETIME DEFAULT NOW(),
    `newsDetail` NVARCHAR(10000),
    `newsImage`  NVARCHAR(255),
    `newsStatus` int      DEFAULT 1,
    `newsTitle`  NVARCHAR(255)

);

create table `TokenLiving`
(
    `Id`      bigint primary key AUTO_INCREMENT,
    `Email`   NVARCHAR(40) unique,
    `created` DATETIME DEFAULT NOW(),
    `updated` DATETIME DEFAULT NOW(),
    `token`   NVARCHAR(1000)
);

CREATE TABLE `tbl_customer`
(
    `Id`          bigint primary key AUTO_INCREMENT,
    `Email`       NVARCHAR(40) unique,
    `PhoneNumber` VARCHAR(15),
    `FullName`    NVARCHAR(100),
    `PassWord`    varchar(300),
    `TotalMoney`  bigint DEFAULT 0,
    `beneficiary` VARCHAR(20),
    `role`        NVARCHAR(20),
    `accStatus`   int    default 1,
    `dateOfBirth` DATETIME DEFAULT NOW(),
    `sex`   int   default 1,
    `image`       NVARCHAR(255) default 'https://www.dropbox.com/s/6gkxzppp1g3we6z/1622285305745.jpg?raw=1',
    constraint tbl_customerBeneficiary foreign key (beneficiary) references tbl_Beneficiary (beneficiaryName),
    constraint tbl_customerRole foreign key (role) references tbl_role (role)

);


CREATE TABLE `tbl_booking`
(
    `BookingID`     NVARCHAR(255) unique primary key,
    `bookingStatus` int      DEFAULT 1,
    `BookingTime`   datetime,
    `Created`       DATETIME DEFAULT NOW(),
    `DepositId`     bigint   DEFAULT 0,
    `Email`         NVARCHAR(40),
    `TotalSeats`    int      DEFAULT 0,
    `confirmBooking`    int      DEFAULT 0,
    `saleId`        bigint,
    `note`          NVARCHAR(1000),
    `TableName`     NVARCHAR(20),
    constraint fk_BookingDeposit foreign key (DepositId) references tbl_deposit (DepositId),
    constraint fk_BookingSale foreign key (saleId) references tbl_Sale (saleId),
    constraint fk_BookingCustomer foreign key (Email) references tbl_customer (Email),
    constraint fk_BookingTable foreign key (TableName) references tbl_table (TableName)
);


create table `tbl_bill`
(
    `id`         bigint primary key AUTO_INCREMENT,
    `BillId`     NVARCHAR(255) unique,
    `PointID`    bigint,
    `BookingID`  NVARCHAR(255),
    `Discount`   BIGINT DEFAULT 0,
--     `BillStatus` int    default 0,
    `PayDate`    datetime,
    constraint fk_BillPoints foreign key (PointID) references tbl_point (PointID),
    constraint fk_BillBooking foreign key (BookingID) references tbl_booking (BookingID)
);

create table `tbl_food`
(
    `FoodId`     bigint primary key AUTO_INCREMENT,
    `Created`    DATETIME DEFAULT NOW(),
    `describes`  NVARCHAR(10000),
    `FoodName`   NVARCHAR(255),
    `FoodImage`  NVARCHAR(255),
    `FoodPrice`  bigint   DEFAULT 0,
    `FoodStatus` int      default 1
);

create table `tbl_billinfo`
(
    `Id`       bigint primary key AUTO_INCREMENT,
    `BillID`   NVARCHAR(255),
    `Quantity` int,
    `FoodID`   BIGINT,
    constraint fk_BillInfoBill foreign key (`BillID`) references tbl_bill (`BillID`),
    constraint fk_BillInfoFood foreign key (`FoodID`) references tbl_food (`FoodID`)
);


create table `conf_schedules`
(
    `Id`          bigint primary key AUTO_INCREMENT,
    `active`      bit,
    `bean`        nvarchar(100),
    `code`        nvarchar(100),
    `cron`        nvarchar(100),
    `description` nvarchar(100),
    `name`        nvarchar(100)
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


INSERT INTO tbl_deposit(Deposit, TotalPersons, depositStatus)
VALUES (100000, 2, 1);
INSERT INTO tbl_deposit(Deposit, TotalPersons, depositStatus)
VALUES (200000, 4, 1);
INSERT INTO tbl_deposit(Deposit, TotalPersons, depositStatus)
VALUES (300000, 8, 1);
INSERT INTO tbl_deposit(Deposit, TotalPersons, depositStatus)
VALUES (300000, 8, 1);
INSERT INTO tbl_deposit(Deposit, TotalPersons, depositStatus)
VALUES (100000, 1, 1);
INSERT INTO tbl_deposit(Deposit, TotalPersons, depositStatus)
VALUES (500000, 1000, 1);



INSERT INTO tbl_table(TableName, Seat, StillEmpty)
VALUES (N'B??n 1', 2, 1);
INSERT INTO tbl_table(TableName, Seat, StillEmpty)
VALUES (N'B??n 2', 2, 3);
INSERT INTO tbl_table(TableName, Seat, StillEmpty)
VALUES (N'B??n 3', 4, 3);
INSERT INTO tbl_table(TableName, Seat, StillEmpty)
VALUES (N'B??n 4', 4, 6);
INSERT INTO tbl_table(TableName, Seat, StillEmpty)
VALUES (N'B??n 5', 6, 6);
INSERT INTO tbl_table(TableName, Seat, StillEmpty)
VALUES (N'B??n 6', 6, 1);
INSERT INTO tbl_table(TableName, Seat, StillEmpty)
VALUES (N'B??n 7', 15, 10);
INSERT INTO tbl_table(TableName, Seat, StillEmpty)
VALUES (N'B??n 8', 15, 2);
INSERT INTO tbl_table(TableName, Seat, StillEmpty)
VALUES (N'B??n 9', 16, 10);
INSERT INTO tbl_table(TableName, Seat, StillEmpty)
VALUES (N'B??n 10', 17, 1);
INSERT INTO tbl_table(TableName, Seat, StillEmpty)
VALUES (N'B??n 11', 18, 10);
INSERT INTO tbl_table(TableName, Seat, StillEmpty)
VALUES (N'B??n 12', 25, 10);
INSERT INTO tbl_table(TableName, Seat, StillEmpty)
VALUES (N'B??n 13', 30, 2);
INSERT INTO tbl_table(TableName, Seat, StillEmpty)
VALUES (N'B??n 14', 40, 2);


INSERT INTO tbl_point(Price, PointPercent)
VALUES (0, 0);
INSERT INTO tbl_point(Price, PointPercent)
VALUES (1000000, 0.05);
INSERT INTO tbl_point(Price, PointPercent)
VALUES (2000000, 0.1);
INSERT INTO tbl_point(Price, PointPercent)
VALUES (3000000, 0.15);
INSERT INTO tbl_point(Price, PointPercent)
VALUES (3000000, 0.17);
INSERT INTO tbl_point(Price, PointPercent)
VALUES (1000000000, 0.2);


Insert into tbl_Beneficiary(beneficiaryName, beneficiaryStatus, totalBill)
values ('VIP', 1, 100000);
Insert into tbl_Beneficiary(beneficiaryName, beneficiaryStatus, totalBill)
values ('REGULAR', 1, 50000);
Insert into tbl_Beneficiary(beneficiaryName, beneficiaryStatus, totalBill)
values ('CUSTOMER', 1, 10000);


insert into tbl_role(role, roleName)
values ('USERS', N'Kh??ch H??ng');
insert into tbl_role(role, roleName)
values ('ADMIN', N'Nh??n Vi??n');


INSERT INTO tbl_customer(Email, PhoneNumber, FullName, Password, TotalMoney, role, beneficiary)
VALUES ('pvminh@gmail.com',
        '0796164361',
        N'Ph?? V??n Minh', '$2a$10$v.MczrO5SeLbLwKEuIih1OPOfSldACzBd9RUKAExt4ALbtC34e5w2', 10000000, 'USERS', 'CUSTOMER');
INSERT INTO tbl_customer(Email, PhoneNumber, FullName, Password, TotalMoney, role, beneficiary)
VALUES ('sunshine87lethanhnghi@gmail.com', '0999999999', N'Nh?? h??ng Sunshine',
        '$2a$10$v.MczrO5SeLbLwKEuIih1OPOfSldACzBd9RUKAExt4ALbtC34e5w2', 0, 'ADMIN', 'VIP');
INSERT INTO tbl_customer(Email, PhoneNumber, FullName, Password, TotalMoney, role, beneficiary)
VALUES ('haind1@vimo.vn', '0978675678', N'Nguy???n ????nh H???i',
        '$2a$10$v.MczrO5SeLbLwKEuIih1OPOfSldACzBd9RUKAExt4ALbtC34e5w2', 5000000, 'USERS', 'CUSTOMER');

insert into tbl_Food(foodName, describes, foodImage, foodPrice, foodStatus)
values ('T??m H??m ??c', 'Set T??m H??m ??c ( T??m h??m ??c b??? l?? phomai/ ch??y t???i, Salad rong bi???n tr???ng t??m, Nem h???i s???n...)',
        'https://www.dropbox.com/s/0tsh0ajysptlae4/1621756968541.jpg?raw=1', '100000', 1);
insert into tbl_Food(foodName, describes, foodImage, foodPrice, foodStatus)
values ('Th???t tr??u g??c b???p', 'Th???t tr??u g??c b???p kh??ng ch??? l?? ?????c s???n L??o Cai m?? c??n l?? ?????c s???n T??y B???c',
        'https://www.dropbox.com/s/q3a48o3ivxn56ls/1621757187545.jpg?raw=1', '100000', 1);
insert into tbl_Food(foodName, describes, foodImage, foodPrice, foodStatus)
values ('Th???ng c??? ng???a B???c H??',
        'Th???ng c??? ?????c s???n T??y B???c ???????c l??m t??? ng?????i M??ng. Th???ng c??? ng???a B???c H?? ?????c bi???t v?? ???????c l??m t??? x????ng ng???a ???????c nu??i ??? v??ng n??i ninh c??ng l???c ph??? ng?? t???ng c???a ch??ng c??ng h???t d???i, th???o qu???, qu??? h???i',
        'https://www.dropbox.com/s/x9pb942lu1ait1f/1621757333846.jpg?raw=1', '200000', 1);
insert into tbl_Food(foodName, describes, foodImage, foodPrice, foodStatus)
values ('Pa p???nh t???p',
        'Pa p???nh t???p hay c??n g???i l?? c?? su???i g???p n?????ng. ????y l?? m???t m??n ??n c??? truy???n n???i ti???ng v?? l?? ?????c s???n T??y B???c r???t ???????c tr??n tr???ng c???a ng?????i Th??i, ???????c l??m t??? c?? su???i t????i ???????c ?????p c??ng g???ng, s???',
        'https://www.dropbox.com/s/zjfh4tb3ee2zn79/1621757392195.jpg?raw=1', '150000', 1);
insert into tbl_Food(foodName, describes, foodImage, foodPrice, foodStatus)
values ('L???n c???p n??ch',
        'L???n c???p n??ch hay ???????c g???i l?? l???n b???n, l???n m??n, ???????c b?? con nu??i th??? r??ng tr??n ?????i n??i, ch??? ??n c??y c???, kh??ng ??n th???c ??n c??y c??? n??n th???t l???n c???p n??ch r???t ch???c, nhi???u n???c',
        'https://www.dropbox.com/s/0zhh3w0szncgjll/1621757422535.jpg?raw=1', '150000', 1);
insert into tbl_Food(foodName, describes, foodImage, foodPrice, foodStatus)
values ('Kh??u nh???c',
        '????y l?? m??n ??n truy???n th???ng c???a ng?????i d??n x??? L???ng v?? ???????c coi l?? m??n ??n ???sang tr???ng??? c???a ng?????i N??ng. Th???c ch???t, ????y l?? m??n ??n ???????c l??m t??? th???t ba ch???',
        'https://www.dropbox.com/s/ildpzupdezxf30i/1621757457619.jpg?raw=1', '150000', 1);
insert into tbl_Food(foodName, describes, foodImage, foodPrice, foodStatus)
values ('Nh???ng ong r???ng',
        'Nh???ng ong r???ng kh?? hi???m, ch??? c?? v??o kho???ng t??? th??ng 4 ?????n th??ng 8 h??ng n??m th?? m???i c??. N??n ????? th?????ng th???c m??n nh???ng ong r???ng x??o ?????c s???n ',
        'https://www.dropbox.com/s/bh96tjmltmlkyn6/1621757495803.jpg?raw=1', '100000', 1);
insert into tbl_Food(foodName, describes, foodImage, foodPrice, foodStatus)
values ('B?? chao M???c Ch??u',
        '????y l?? m??n ngon T??y B???c ?????u ti??n ch??ng m??nh mu???n k??? cho b???n khi ?????n S??n La. V?? m??n n??y c?? ngu???n g???c t??? M???c Ch??u - S??n La v?? c??ng ch??? c?? S??n La l?? ngon nh???t th??',
        'https://www.dropbox.com/s/xphic4yfeths3rg/1621757529055.jpg?raw=1', '100000', 1);
insert into tbl_Food(foodName, describes, foodImage, foodPrice, foodStatus)
values (' H???i s??m', 'h???i s??m r???t t???t cho ????n ??ng v?? ng?????i cao tu???i',
        'https://www.dropbox.com/s/94wb3drx67v0bd9/1621757571182.jpg?raw=1', '250000', 1);
insert into tbl_Food(foodName, describes, foodImage, foodPrice, foodStatus)
values ('Gh??? xanh H??m Ninh Ph?? Qu???c',
        'Gh??? th?? ??? ????u c??ng c?? r???i nh??ng ????? c?? gh??? ch???t l?????ng, ngon v?? an to??n cho s???c kh???e th?? ?????n Ph?? Qu???c,',
        'https://www.dropbox.com/s/lripmz2axpkm2ee/1621757602328.jpg?raw=1', '250000', 1);
insert into tbl_Sale(saleTitle, saleDetail, saleImage, beneficiary, percentDiscount, saleStatus)
values ('GOFOOD SALE OFF 10%',
        'C??? m???i d???p 8/3 gh?? qua, ph??i m???nh l???i t???t b???t chu???n b??? COMBO HOA + QU?? cho ch??? em. Combo ???y c??? l???p ??i l???p l???i khi???n vi???c t???ng qu?? ng??y 8/3 d???n tr??? th??nh ???ngh??a v??? h???ng n??m???, vi???c nh???n c??ng tr??? th??nh c??i t???c l?????i ???quy???n l???i h???ng n??m???. V?? th??? l??, 8/3 ch???ng c??n nh???ng h???i h???p nh???ng b???t ng??? n???a',
        'https://www.dropbox.com/s/h18mmyscbf1cvty/1621758070713.jpg?raw=1', 'CUSTOMER', 0.1, 1);
insert into tbl_Sale(saleTitle, saleDetail, saleImage, beneficiary, percentDiscount, saleStatus)
values ('??i 4 T??nh Ti???n 3, 15% OFF',
        'G???i t???ng voucher GI???M GI?? 15% BUFFET L???U TR??N ????A BAY KH???NG L??? ??? BUFFET L???U UFO ho???c L???U 9 T???NG M??Y ho???c S?????N N?????NG T???NG S???T LAVA cho c??c kh??ch h??ng c?? sinh nh???t trong th??ng 1 n??y!',
        'https://www.dropbox.com/s/wvy9jooyt6qwuo9/1621758127448.jpg?raw=1', 'CUSTOMER', 0.15, 1);
insert into tbl_Sale(saleTitle, saleDetail, saleImage, beneficiary, percentDiscount, saleStatus)
values ('Ch????ng tr??nh KM s??? b???t ?????u t??? ng??y 3/4 ?????n h???t 30/4 (tr??? ng??y l??? v?? CN)',
        'Th??ng T?? n??y HONGDAE ch??i l???n KHUY???N M???I KH???NG nghe b?? con. Buffet 299k: Gi???m 30,4% cho ng?????i th??? 3, gi???m ti???p 30,4%x2 cho ng?????i th??? 4',
        'https://www.dropbox.com/s/ux3v177aj5tgu4a/1621758235141.jpg?raw=1', 'CUSTOMER', 0.15, 1);
insert into tbl_Sale(saleTitle, saleDetail, saleImage, beneficiary, percentDiscount, saleStatus)
values ('??u ????i gi???m gi?? 10% v???i h??a ????n mua h??ng t??? 400.000??',
        'K??? ngh??? l??? 30/4 - 1/5 lu??n l?? d???p l?? t?????ng ????? t??? ch???c c??c b???a li??n hoan t???i nh?? c??ng nh?? c??c b???a ti???c picnic, d?? ngo???i. ????? nh???ng b???a ti???c li??n hoan ???????c ho??n h???o nh???t, ?????ng qu??n gh?? HOMEFARM l???a ch???n th???c ph???m th??m ngon l???i ti???n l???i nh??!',
        'https://www.dropbox.com/s/20k4oc4hvyko3es/1621758280647.jpg?raw=1', 'VIP', 0.15, 1);
insert into tbl_Sale(saleTitle, saleDetail, saleImage, beneficiary, percentDiscount, saleStatus)
values ('??u ????i 1/6',
        'K??? ngh??? l??? 30/4 - 1/5 lu??n l?? d???p l?? t?????ng ????? t??? ch???c c??c b???a li??n hoan t???i nh?? c??ng nh?? c??c b???a ti???c picnic, d?? ngo???i. ????? nh???ng b???a ti???c li??n hoan ???????c ho??n h???o nh???t, ?????ng qu??n gh?? HOMEFARM l???a ch???n th???c ph???m th??m ngon l???i ti???n l???i nh??!',
        'https://www.dropbox.com/s/1yz7ceat9d6jipd/1621758366843.jpg?raw=1', 'VIP', 0.15, 1);
insert into tbl_News(newsTitle, newsDetail, newsImage, newsStatus)
values ('New1', 'New1', 'https://www.dropbox.com/s/e0hpa6kc2j9giqh/1621758467808.jpg?raw=1', 1);
insert into tbl_News(newsTitle, newsDetail, newsImage, newsStatus)
values ('New12', 'New12', 'https://www.dropbox.com/s/5sppfri3idtbjy0/1621758477159.jpg?raw=1', 1);
insert into tbl_News(newsTitle, newsDetail, newsImage, newsStatus)
values ('New123', 'New123', 'https://www.dropbox.com/s/vd6g49pesbveohw/1621758486786.jpg?raw=1', 1);
insert into tbl_News(newsTitle, newsDetail, newsImage, newsStatus)
values ('New1234', 'New1234', 'https://www.dropbox.com/s/9x3xd39zfzuwd3j/1621758495452.jpg?raw=1', 1);

insert into conf_schedules(active, bean, code, cron, description, name)
values(1,'changeResetPasswordStatus','1','15 0/1 * * * *','test schedule in database','spam email');

insert into conf_schedules(active, bean, code, cron, description, name)
values(1,'removeLiveToken','1','15 0/1 * * * *','test schedule in database','job xoa TokenLiving');

insert into conf_schedules(active, bean, code, cron, description, name)
values(1,'autoCancelBooking','1','15 0/1 * * * *','test schedule in database','job auto cancelbooking');

insert into conf_schedules(active, bean, code, cron, description, name)
values(1,'autoUpdateBeneficiary','1','15 0/1 * * * *','test schedule in database','job auto UpdateBeneficiary');