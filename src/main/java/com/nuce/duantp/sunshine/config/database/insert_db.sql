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
    `bookingStatus` int      DEFAULT 0,
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
VALUES (N'Bàn 1', 2, 1);
INSERT INTO tbl_table(TableName, Seat, StillEmpty)
VALUES (N'Bàn 2', 2, 3);
INSERT INTO tbl_table(TableName, Seat, StillEmpty)
VALUES (N'Bàn 3', 4, 3);
INSERT INTO tbl_table(TableName, Seat, StillEmpty)
VALUES (N'Bàn 4', 4, 6);
INSERT INTO tbl_table(TableName, Seat, StillEmpty)
VALUES (N'Bàn 5', 6, 6);
INSERT INTO tbl_table(TableName, Seat, StillEmpty)
VALUES (N'Bàn 6', 6, 1);
INSERT INTO tbl_table(TableName, Seat, StillEmpty)
VALUES (N'Bàn 7', 15, 10);
INSERT INTO tbl_table(TableName, Seat, StillEmpty)
VALUES (N'Bàn 8', 15, 2);
INSERT INTO tbl_table(TableName, Seat, StillEmpty)
VALUES (N'Bàn 9', 16, 10);
INSERT INTO tbl_table(TableName, Seat, StillEmpty)
VALUES (N'Bàn 10', 17, 1);
INSERT INTO tbl_table(TableName, Seat, StillEmpty)
VALUES (N'Bàn 11', 18, 10);
INSERT INTO tbl_table(TableName, Seat, StillEmpty)
VALUES (N'Bàn 12', 25, 10);
INSERT INTO tbl_table(TableName, Seat, StillEmpty)
VALUES (N'Bàn 13', 30, 2);
INSERT INTO tbl_table(TableName, Seat, StillEmpty)
VALUES (N'Bàn 14', 40, 2);


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
values ('USERS', N'Khách Hàng');
insert into tbl_role(role, roleName)
values ('ADMIN', N'Nhân Viên');


INSERT INTO tbl_customer(Email, PhoneNumber, FullName, Password, TotalMoney, role, beneficiary)
VALUES ('pvminh@gmail.com',
        '0796164361',
        N'Phí Văn Minh', '$2a$10$v.MczrO5SeLbLwKEuIih1OPOfSldACzBd9RUKAExt4ALbtC34e5w2', 10000000, 'USERS', 'CUSTOMER');
INSERT INTO tbl_customer(Email, PhoneNumber, FullName, Password, TotalMoney, role, beneficiary)
VALUES ('sunshine87lethanhnghi@gmail.com', '0999999999', N'Nhà hàng Sunshine',
        '$2a$10$v.MczrO5SeLbLwKEuIih1OPOfSldACzBd9RUKAExt4ALbtC34e5w2', 0, 'ADMIN', 'VIP');
INSERT INTO tbl_customer(Email, PhoneNumber, FullName, Password, TotalMoney, role, beneficiary)
VALUES ('haind1@vimo.vn', '0978675678', N'Nguyễn Đình Hải',
        '$2a$10$v.MczrO5SeLbLwKEuIih1OPOfSldACzBd9RUKAExt4ALbtC34e5w2', 5000000, 'USERS', 'CUSTOMER');

insert into tbl_Food(foodName, describes, foodImage, foodPrice, foodStatus)
values ('Tôm Hùm Úc', 'Set Tôm Hùm Úc ( Tôm hùm úc bỏ lò phomai/ cháy tỏi, Salad rong biển trứng tôm, Nem hải sản...)',
        'https://www.dropbox.com/s/0tsh0ajysptlae4/1621756968541.jpg?raw=1', '100000', 1);
insert into tbl_Food(foodName, describes, foodImage, foodPrice, foodStatus)
values ('Thịt trâu gác bếp', 'Thịt trâu gác bếp không chỉ là đặc sản Lào Cai mà còn là đặc sản Tây Bắc',
        'https://www.dropbox.com/s/q3a48o3ivxn56ls/1621757187545.jpg?raw=1', '100000', 1);
insert into tbl_Food(foodName, describes, foodImage, foodPrice, foodStatus)
values ('Thắng cố ngựa Bắc Hà',
        'Thắng cố đặc sản Tây Bắc được làm từ người Mông. Thắng cố ngựa Bắc Hà đặc biệt vì được làm từ xương ngựa được nuôi ở vùng núi ninh cùng lục phủ ngũ tạng của chúng cúng hạt dổi, thảo quả, quế hồi',
        'https://www.dropbox.com/s/x9pb942lu1ait1f/1621757333846.jpg?raw=1', '200000', 1);
insert into tbl_Food(foodName, describes, foodImage, foodPrice, foodStatus)
values ('Pa pỉnh tộp',
        'Pa pỉnh tộp hay còn gọi là cá suối gập nướng. Đây là một món ăn cổ truyền nổi tiếng và là đặc sản Tây Bắc rất được trân trọng của người Thái, được làm từ cá suối tươi được ướp cùng gừng, sả',
        'https://www.dropbox.com/s/zjfh4tb3ee2zn79/1621757392195.jpg?raw=1', '150000', 1);
insert into tbl_Food(foodName, describes, foodImage, foodPrice, foodStatus)
values ('Lợn cắp nách',
        'Lợn cắp nách hay được gọi là lợn bản, lợn mán, được bà con nuôi thả rông trên đồi núi, chỉ ăn cây cỏ, không ăn thức ăn cây cỏ nên thịt lợn cắp nách rất chắc, nhiều nạc',
        'https://www.dropbox.com/s/0zhh3w0szncgjll/1621757422535.jpg?raw=1', '150000', 1);
insert into tbl_Food(foodName, describes, foodImage, foodPrice, foodStatus)
values ('Khâu nhục',
        'Đây là món ăn truyền thống của người dân xứ Lạng và được coi là món ăn “sang trọng” của người Nùng. Thực chất, đây là món ăn được làm từ thịt ba chỉ',
        'https://www.dropbox.com/s/ildpzupdezxf30i/1621757457619.jpg?raw=1', '150000', 1);
insert into tbl_Food(foodName, describes, foodImage, foodPrice, foodStatus)
values ('Nhộng ong rừng',
        'Nhộng ong rừng khá hiếm, chỉ có vào khoảng từ tháng 4 đến tháng 8 hàng năm thì mới có. Nên để thưởng thức món nhộng ong rừng xào đặc sản ',
        'https://www.dropbox.com/s/bh96tjmltmlkyn6/1621757495803.jpg?raw=1', '100000', 1);
insert into tbl_Food(foodName, describes, foodImage, foodPrice, foodStatus)
values ('Bê chao Mộc Châu',
        'Đây là món ngon Tây Bắc đầu tiên chúng mình muốn kể cho bạn khi đến Sơn La. Vì món này có nguồn gốc từ Mộc Châu - Sơn La và cũng chỉ có Sơn La là ngon nhất thô',
        'https://www.dropbox.com/s/xphic4yfeths3rg/1621757529055.jpg?raw=1', '100000', 1);
insert into tbl_Food(foodName, describes, foodImage, foodPrice, foodStatus)
values (' Hải sâm', 'hải sâm rất tốt cho đàn ông và người cao tuổi',
        'https://www.dropbox.com/s/94wb3drx67v0bd9/1621757571182.jpg?raw=1', '250000', 1);
insert into tbl_Food(foodName, describes, foodImage, foodPrice, foodStatus)
values ('Ghẹ xanh Hàm Ninh Phú Quốc',
        'Ghẹ thì ở đâu cũng có rồi nhưng để có ghẹ chất lượng, ngon và an toàn cho sức khỏe thì đến Phú Quốc,',
        'https://www.dropbox.com/s/lripmz2axpkm2ee/1621757602328.jpg?raw=1', '250000', 1);
insert into tbl_Sale(saleTitle, saleDetail, saleImage, beneficiary, percentDiscount, saleStatus)
values ('GOFOOD SALE OFF 10%',
        'Cứ mỗi dịp 8/3 ghé qua, phái mạnh lại tất bật chuẩn bị COMBO HOA + QUÀ cho chị em. Combo ấy cứ lặp đi lặp lại khiến việc tặng quà ngày 8/3 dần trở thành “nghĩa vụ hằng năm”, việc nhận cũng trở thành cái tặc lưỡi “quyền lợi hằng năm”. Và thế là, 8/3 chẳng còn những hồi hộp những bất ngờ nữa',
        'https://www.dropbox.com/s/h18mmyscbf1cvty/1621758070713.jpg?raw=1', 'CUSTOMER', 0.1, 1);
insert into tbl_Sale(saleTitle, saleDetail, saleImage, beneficiary, percentDiscount, saleStatus)
values ('Đi 4 Tính Tiền 3, 15% OFF',
        'Gửi tặng voucher GIẢM GIÁ 15% BUFFET LẨU TRÊN ĐĨA BAY KHỔNG LỒ – BUFFET LẨU UFO hoặc LẨU 9 TẦNG MÂY hoặc SƯỜN NƯỚNG TẢNG SỐT LAVA cho các khách hàng có sinh nhật trong tháng 1 này!',
        'https://www.dropbox.com/s/wvy9jooyt6qwuo9/1621758127448.jpg?raw=1', 'CUSTOMER', 0.15, 1);
insert into tbl_Sale(saleTitle, saleDetail, saleImage, beneficiary, percentDiscount, saleStatus)
values ('Chương trình KM sẽ bắt đầu từ ngày 3/4 đến hết 30/4 (trừ ngày lễ và CN)',
        'Tháng Tư này HONGDAE chơi lớn KHUYẾN MẠI KHỦNG nghe bà con. Buffet 299k: Giảm 30,4% cho người thứ 3, giảm tiếp 30,4%x2 cho người thứ 4',
        'https://www.dropbox.com/s/ux3v177aj5tgu4a/1621758235141.jpg?raw=1', 'CUSTOMER', 0.15, 1);
insert into tbl_Sale(saleTitle, saleDetail, saleImage, beneficiary, percentDiscount, saleStatus)
values ('Ưu đãi giảm giá 10% với hóa đơn mua hàng từ 400.000đ',
        'Kỳ nghỉ lễ 30/4 - 1/5 luôn là dịp lý tưởng để tổ chức các bữa liên hoan tại nhà cũng như các bữa tiệc picnic, dã ngoại. Để những bữa tiệc liên hoan được hoàn hảo nhất, đừng quên ghé HOMEFARM lựa chọn thực phẩm thơm ngon lại tiện lợi nhé!',
        'https://www.dropbox.com/s/20k4oc4hvyko3es/1621758280647.jpg?raw=1', 'VIP', 0.15, 1);
insert into tbl_Sale(saleTitle, saleDetail, saleImage, beneficiary, percentDiscount, saleStatus)
values ('Ưu đãi 1/6',
        'Kỳ nghỉ lễ 30/4 - 1/5 luôn là dịp lý tưởng để tổ chức các bữa liên hoan tại nhà cũng như các bữa tiệc picnic, dã ngoại. Để những bữa tiệc liên hoan được hoàn hảo nhất, đừng quên ghé HOMEFARM lựa chọn thực phẩm thơm ngon lại tiện lợi nhé!',
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