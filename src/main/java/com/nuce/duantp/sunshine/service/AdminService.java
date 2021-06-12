package com.nuce.duantp.sunshine.service;

import com.nuce.duantp.sunshine.config.TimeUtils;
import com.nuce.duantp.sunshine.config.format.LogCodeSql;
import com.nuce.duantp.sunshine.config.format.FormatMoney;
import com.nuce.duantp.sunshine.config.format.Validate;
import com.nuce.duantp.sunshine.dto.request.News;
import com.nuce.duantp.sunshine.dto.request.*;
import com.nuce.duantp.sunshine.dto.response.*;
import com.nuce.duantp.sunshine.dto.enums.EnumResponseStatusCode;
import com.nuce.duantp.sunshine.dto.model.*;
import com.nuce.duantp.sunshine.repository.*;
import com.nuce.duantp.sunshine.security.jwt.AuthTokenFilter;
import lombok.RequiredArgsConstructor;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.FileNotFoundException;
import java.sql.Timestamp;
import java.util.*;

@RequiredArgsConstructor
@Service
public class AdminService {
    private final DepositRepo depositRepo;
    private final PointsRepo pointsRepo;
    private final AuthTokenFilter authTokenFilter;
    private final BookingRepository bookingRepository;
    private final BillRepo billRepo;
    private final BillInfoRepo billInfoRepo;
    private final Logger LOGGER = LoggerFactory.getLogger(AdminService.class);
    private final FoodRepo foodRepo;
    private final NewsRepo newsRepo;
    private final SaleRepo saleRepo;
    private final BookingService bookingService;
    private final CustomerRepo customerRepo;
    private final ImageService imageService;
    private final BeneficiaryRepo beneficiaryRepo;
    private final SunShineService sunShineService;
    public void exportReport(String fileName) throws FileNotFoundException, JRException {
        String path = "./src/main/resources/static";
        List<tbl_Customer> employees = (List<tbl_Customer>) customerRepo.findAll();

        File file = ResourceUtils.getFile("classpath:employees.jrxml");
        JasperReport jasperReport = JasperCompileManager.compileReport(file.getAbsolutePath());
        JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(employees);
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("createdBy", "Java Techie");
        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, dataSource);
        JasperExportManager.exportReportToPdfFile(jasperPrint, path + "\\" + fileName + ".pdf");

    }

    public void exportBill(String bookingId) throws FileNotFoundException, JRException {
        String path = "./src/main/resources/static";

        List<ListFoodInBooking> listBillRp = new ArrayList<>();
        tbl_Booking booking = bookingRepository.findByBookingId(bookingId);
        tbl_Customer customer = customerRepo.findCustomerByEmail(booking.getEmail());
        tbl_Bill bill = billRepo.findByBookingId(bookingId);
        tbl_Deposit deposit = depositRepo.findByDepositId(booking.getDepositId());
        tbl_Sale sale = saleRepo.findBySaleId(booking.getSaleId());
        List<tbl_BillInfo> list = billInfoRepo.findAllByBillId(bill.getBillId());
        int stt = 1;
        float totalMoney = 0L;
        for (tbl_BillInfo data : list) {
            tbl_Food food = foodRepo.findByFoodId(data.getFoodId());
            ListFoodInBooking listFoodInBooking = new ListFoodInBooking(stt, food.getFoodName(), FormatMoney.formatMoney(String.valueOf(food.getFoodPrice())),
                    FormatMoney.formatMoney(String.valueOf(food.getFoodPrice() * data.getQuantity())), data.getQuantity());
            listBillRp.add(listFoodInBooking);
            stt++;
            totalMoney += Float.parseFloat(listFoodInBooking.getMoney());
        }
        float sumMoney = 0L;
        float salePr = 0L;
        if (sale != null) {
            sumMoney = totalMoney * sale.getPercentDiscount() - deposit.getDeposit();
            salePr = sale.getPercentDiscount();
        } else {
            sumMoney = totalMoney - deposit.getDeposit();
        }

        JasperReportBill reportBill = new JasperReportBill(bookingId, customer.getFullName(), booking.getBookingTime(), listBillRp, deposit.getDeposit(), salePr, sumMoney, totalMoney);
        reportBill.convertChar();
        File file = ResourceUtils.getFile("classpath:exportBill.jrxml");
        JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(reportBill.getListFoodInBookings());

        JasperReport jasperReport = JasperCompileManager.compileReport(file.getAbsolutePath());
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("bookingId", reportBill.getBookingId());
        parameters.put("customerName", reportBill.getCustomerName());
        parameters.put("bookingTime", reportBill.getBookingTime());
        parameters.put("deposit", reportBill.getDeposit());
        parameters.put("sale", reportBill.getSale());
        parameters.put("sumMoney", reportBill.getSumMoney());
        parameters.put("totalMoney", reportBill.getTotalMoney());
//        jasperReport.setProperty();

        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, dataSource);
        JasperExportManager.exportReportToPdfFile(jasperPrint, path + "\\" + "bill" + reportBill.getBookingId() + ".pdf");

    }

    public ResponseEntity<?> addFoodInBooking(OrderFoodReq orderFoodReq, String email) {
        tbl_Bill bill = billRepo.findByBookingId(orderFoodReq.getBookingId());
        tbl_Booking booking = bookingRepository.findByBookingId(orderFoodReq.getBookingId());
        for (FoodReq data : orderFoodReq.getFoodList()) {
            tbl_BillInfo billInfo = billInfoRepo.findByBillIdAndFoodId(bill.getBillId(), data.getFoodId());
            if (billInfo == null) {
                tbl_BillInfo billInfo1 = new tbl_BillInfo(bill.getBillId(), data.getQuantity(), data.getFoodId());
                billInfoRepo.save(billInfo1);
//                bookingService.orderFood(orderFoodReq, booking.getEmail());
            } else {
                int quantity = billInfo.getQuantity() + data.getQuantity();
                billInfo.setQuantity(quantity);
                billInfoRepo.save(billInfo);
            }
        }
        LOGGER.warn("add food for customer by " + email + "\n" + orderFoodReq, AdminService.class);
        MessageResponse response = new MessageResponse(EnumResponseStatusCode.ADD_FOOD_SUCCESS);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    public ResponseEntity<?> changeDeposit(DepositReq depositReq, String email) {
        List<tbl_Deposit> tbl_deposit = depositRepo.findAllByTotalPersons(depositReq.getTotalPersons());
        if (tbl_deposit.size() > 0) {
            for (tbl_Deposit deposit : tbl_deposit) {
                deposit.setDepositStatus(0);
                depositRepo.save(deposit);
            }
        }
        ModelMapper modelMap = new ModelMapper();
        tbl_Deposit deposit = modelMap.map(depositReq, tbl_Deposit.class);
        depositRepo.save(deposit);
        LOGGER.warn("Change deposit by " + email + "\n" + depositReq, AdminService.class);
        MessageResponse messageResponse = new MessageResponse(EnumResponseStatusCode.SUCCESS);
        return new ResponseEntity<>(messageResponse, HttpStatus.OK);
    }

    public ResponseEntity<?> changePoint(PointReq pointReq, String email) {
        List<tbl_Points> pointsList = pointsRepo.findAllByPrice(pointReq.getPrice());
        if (pointsList.size() > 0) {
            for (tbl_Points data : pointsList) {
                data.setPointStatus(0);
                pointsRepo.save(data);
            }
        }
        ModelMapper modelMap = new ModelMapper();
        tbl_Points data = modelMap.map(pointReq, tbl_Points.class);
        pointsRepo.save(data);
        LOGGER.warn("Change point by " + email + "\n" + pointReq, AdminService.class);

        MessageResponse messageResponse = new MessageResponse(EnumResponseStatusCode.SUCCESS);
        return new ResponseEntity<>(messageResponse, HttpStatus.OK);
    }

    public ResponseEntity<?> addFoodInMenu(String foodName, Long foodPrice, String describe, String email, MultipartFile file) {
        tbl_Food food = new tbl_Food(foodName, foodPrice, describe);
        Image image = new Image();
        image.setName(food.getFoodImage());
        image.setDescription(food.getFoodName());
        image.setImagePath("/Food/" + food.getFoodImage() + ".jpg");
        image.setType("FOOD");
        image.setSpecifyType("specifyType");
        image.setIdParent("idParent");
        imageService.createImage(image, file);
        food.setFoodImage(image.getUrl());
        foodRepo.save(food);
        LOGGER.warn("add food by " + email + "\n" + foodName, AdminService.class);
        String query="insert into tbl_Food(foodName,describes,foodImage,foodPrice,foodStatus)" +
                "\n\tvalues (\'"+food.getFoodName()+"\',\'"+food.getDescribes()+"\',\'"+food.getFoodImage()+"\',\'"+food.getFoodPrice()+"\',"+1+");\n";
        LogCodeSql.writeCodeSql(query);
        MessageResponse response = new MessageResponse(EnumResponseStatusCode.ADD_FOOD_SUCCESS);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    public ResponseEntity<?> enableFood(List<String> foodIdList, String email) {
        for (String foodId : foodIdList) {
            tbl_Food food = foodRepo.findByFoodId(Long.valueOf(foodId));
            food.setFoodStatus(0);
            foodRepo.save(food);
        }
        LOGGER.warn("enable Food by " + email + "\n" + foodIdList, AdminService.class);
        MessageResponse response = new MessageResponse(EnumResponseStatusCode.ENABLE_FOOD_SUCCESS);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    public ResponseEntity<?> addNews(News news,String email) {
        tbl_News tbl_news = new tbl_News(news.getNewsTitle(), news.getNewsDetail());
        Image image = new Image();
        image.setName(tbl_news.getNewsImage());
        image.setDescription(tbl_news.getNewsTitle());
        image.setImagePath("/News/" + tbl_news.getNewsImage() + ".jpg");
        image.setType("NEWS");
        image.setSpecifyType("specifyType");
        image.setIdParent("idParent");
        imageService.createImage(image, news.getFile());
        tbl_news.setNewsImage(image.getUrl());
        newsRepo.save(tbl_news);
        LOGGER.warn("add News by " + email + "\n" + news.getNewsTitle(), AdminService.class);
        String query="insert into tbl_News(newsTitle,newsDetail,newsImage,newsStatus)" +
                "\n\tvalues (\'"+tbl_news.getNewsTitle()+"\',\'"+tbl_news.getNewsDetail()+"\',\'"+tbl_news.getNewsImage()+"\',"+1+");\n";

        LogCodeSql.writeCodeSql(query);
        MessageResponse response = new MessageResponse(EnumResponseStatusCode.ADD_NEWS_SUCCESS);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    public ResponseEntity<?> enableNews(List<String> newsIdList, String email) {
        for (String newId : newsIdList) {
            tbl_News news = newsRepo.findByNewsId(Long.valueOf(newId));
            news.setNewsStatus(0);
            newsRepo.save(news);
        }
        LOGGER.warn("enable News by " + email + "\n" + newsIdList, AdminService.class);
        MessageResponse response = new MessageResponse(EnumResponseStatusCode.ENABLE_NEWS_SUCCESS);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    public ResponseEntity<?> addSale(String saleTitle, String saleDetail, String beneficiary, float percentDiscount,
                                     float totalBill,
                                     MultipartFile file, String email) {
        tbl_Sale sale = new tbl_Sale(saleTitle, saleDetail, beneficiary, percentDiscount,totalBill);
        Image image = new Image();
        image.setName(sale.getSaleImage());
        image.setDescription(saleTitle);
        image.setImagePath("/Sale/" + sale.getSaleImage() + ".jpg");
        image.setType("SALE");
        image.setSpecifyType("specifyType");
        image.setIdParent("idParent");
        imageService.createImage(image, file);
        sale.setSaleImage(image.getUrl());
        saleRepo.save(sale);
        LOGGER.warn("add sale by " + email + "\n" + saleTitle, AdminService.class);
        String query="insert into tbl_Sale(saleTitle,saleDetail,saleImage,beneficiary,percentDiscount,saleStatus)" +
                "\n\tvalues (\'"+sale.getSaleTitle()+"\',\'"+sale.getSaleDetail()+"\',\'"+sale.getSaleImage()+"\',\'"+sale.getBeneficiary()+"\',"+sale.getPercentDiscount()+","+1+");\n";
        LogCodeSql.writeCodeSql(query);
        MessageResponse response = new MessageResponse(EnumResponseStatusCode.ADD_SALE_SUCCESS);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    public ResponseEntity<?> enableSale(List<String> saleIdList, String email) {
        for (String saleId : saleIdList) {
            tbl_Sale sale = saleRepo.findBySaleId(Long.valueOf(saleId));
            saleRepo.save(sale);
        }
        LOGGER.warn("enable sale by " + email + "\n" + saleIdList, AdminService.class);
        MessageResponse response = new MessageResponse(EnumResponseStatusCode.ENABLE_SALE_SUCCESS);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }


    public List<BookingHistoryRes> viewBookingHistory(HttpServletRequest req) {
        Optional<tbl_Customer> customer = authTokenFilter.whoami(req);
        List<tbl_Booking> bookingList = bookingRepository.getListBookingAdmin();
        List<BookingHistoryRes> data = new ArrayList<>();
        int stt = 1;
        for (tbl_Booking booking : bookingList) {

            tbl_Bill bill = billRepo.findByBookingId(booking.getBookingId());
            List<ListFoodInBooking> listFoodInBookings =new ArrayList<>();
            float totalMoneyFood = 0L; //lấy ra tổng số tiền cho đặt món
            int stt1=1;
            List<tbl_BillInfo> billInfoList=billInfoRepo.findAllByBillId(bill.getBillId());
            for(tbl_BillInfo billInfo: billInfoList){
                tbl_Food food=foodRepo.findByFoodId(billInfo.getFoodId());
                ListFoodInBooking listFoodInBooking =new ListFoodInBooking(stt, food.getFoodName(),
                        FormatMoney.formatMoney(String.valueOf(food.getFoodPrice())),
                        FormatMoney.formatMoney(String.valueOf(food.getFoodPrice()* billInfo.getQuantity())),
                        billInfo.getQuantity());
                listFoodInBookings.add(listFoodInBooking);
                stt1++;
                totalMoneyFood+=food.getFoodPrice()*billInfo.getQuantity();
            }

            tbl_Customer customer1=customerRepo.findCustomerByEmail(booking.getEmail());
            float money = 0L;
            if (booking.getBookingStatus() == 1) {
                money = sunShineService.moneyPay(booking.getBookingId());
            }
            tbl_Deposit deposit = depositRepo.findByDepositId(booking.getDepositId());
            float refund=deposit.getDeposit()-money;
            if(refund<0) refund=0L;

            Date date = TimeUtils.minusDate(Timestamp.valueOf(String.valueOf(booking.getBookingTime())), 7, "HOUR");
            String status = Validate.convertStatusBooking(booking.getBookingStatus());
            BookingHistoryRes data1 = new BookingHistoryRes(date, FormatMoney.formatMoney(String.valueOf(deposit.getDeposit())),
                    status, FormatMoney.formatMoney(String.valueOf(money)), stt, booking.getBookingId(),
                    FormatMoney.formatMoney(String.valueOf(refund)),customer1.getEmail(),
                    customer1.getPhoneNumber().replaceFirst("(\\d{3})(\\d{3})(\\d+)", "$1-$2-$3"),listFoodInBookings);
            data.add(data1);
            stt++;
        }
        return data;
    }

    public List<UserDetail> getListUser(){
        List<tbl_Customer> customerList= (List<tbl_Customer>) customerRepo.findAll();
        List<UserDetail> list =new ArrayList<>();
        for(tbl_Customer data: customerList){
            UserDetail userDetail=new UserDetail(data);
            list.add(userDetail);
        }
        return list;
    }

    public UserDetail userDetail(String email){
        tbl_Customer customer=customerRepo.findCustomerByEmail(email);
        UserDetail user=new UserDetail(customer);
        return user;
    }

}
