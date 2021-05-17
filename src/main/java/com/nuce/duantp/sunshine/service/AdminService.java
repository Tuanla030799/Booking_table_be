package com.nuce.duantp.sunshine.service;

import com.nuce.duantp.sunshine.config.database.LogCodeSql;
import com.nuce.duantp.sunshine.dto.request.*;
import com.nuce.duantp.sunshine.dto.response.BillReport;
import com.nuce.duantp.sunshine.dto.response.JasperReportBill;
import com.nuce.duantp.sunshine.dto.response.MessageResponse;
import com.nuce.duantp.sunshine.enums.BeneficiaryEnum;
import com.nuce.duantp.sunshine.enums.EnumResponseStatusCode;
import com.nuce.duantp.sunshine.enums.ImageType;
import com.nuce.duantp.sunshine.model.*;
import com.nuce.duantp.sunshine.repository.*;
import com.nuce.duantp.sunshine.security.jwt.AuthTokenFilter;
import lombok.RequiredArgsConstructor;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

@RequiredArgsConstructor
@Service
public class AdminService {
    private final CustomerRepo repository;
    private final DepositRepo depositRepo;
    private final PointsRepo pointsRepo;
    private final AuthTokenFilter authTokenFilter;
    private final BookingRepository bookingRepository;
    private final ResponseStatusCodeRepo responseStatusCodeRepo;
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
    public void exportReport(String fileName) throws FileNotFoundException, JRException {
        String path = "./src/main/resources/static";
        List<tbl_Customer> employees = (List<tbl_Customer>) repository.findAll();

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

        List<BillReport> listBillRp = new ArrayList<>();
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
            BillReport billReport = new BillReport(stt, food.getFoodName(), food.getFoodPrice(), food.getFoodPrice() * data.getQuantity(), data.getQuantity());
            listBillRp.add(billReport);
            stt++;
            totalMoney += billReport.getMoney();
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
        JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(reportBill.getBillReports());

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
        String str = "ADD_FOOD_SUCCESS";
        for (FoodReq data : orderFoodReq.getFoodList()) {
            tbl_BillInfo billInfo = billInfoRepo.findByBillIdAndFoodId(bill.getBillId(), data.getFoodId());
            if (billInfo == null) {
                bookingService.orderFood(orderFoodReq, booking.getEmail());
            } else {
                int quantity = billInfo.getQuantity() + data.getQuantity();
                billInfo.setQuantity(quantity);
                billInfoRepo.save(billInfo);
            }
        }
        LOGGER.warn("add food for customer by " + email + "\n" + orderFoodReq, AdminService.class);
        tbl_ResponseStatusCode responseStatusCode = responseStatusCodeRepo.findByResponseStatusCode(str);
        MessageResponse response = new MessageResponse(EnumResponseStatusCode.valueOf(responseStatusCode.getResponseStatusCode()), responseStatusCode.getResponseStatusMessage());
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
        foodRepo.save(food);
        LOGGER.warn("add food by " + email + "\n" + foodName, AdminService.class);
        String query="insert into tbl_Food(foodName,describes,foodImage,foodPrice,foodStatus)" +
                "\n\tvalues (\'"+food.getFoodName()+"\',\'"+food.getDescribes()+"\',\'"+food.getFoodImage()+"\',\'"+food.getFoodPrice()+"\',"+1+");\n";
        String query2="insert into image(name,url,imagePath,description,idParent,type,specifyType)" +
                "\n\tvalues(\'"+image.getName()+"\',\'"+image.getUrl()+"\',\'"+image.getImagePath()+"\',\'"+image.getDescription()+"\',\'"+image.getIdParent()+"\',\'"+image.getType()+"\',\'"+image.getSpecifyType()+"\');\n";
        LogCodeSql.writeCodeSql(query);
        LogCodeSql.writeCodeSql(query2);
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

    public ResponseEntity<?> addNews(String newsTitle, String newsDetail, MultipartFile file, String email) {
        tbl_News news = new tbl_News(newsTitle, newsDetail);
        Image image = new Image();
        image.setName(news.getNewsImage());
        image.setDescription(news.getNewsTitle());
        image.setImagePath("/News/" + news.getNewsImage() + ".jpg");
        image.setType("NEWS");
        image.setSpecifyType("specifyType");
        image.setIdParent("idParent");
        imageService.createImage(image, file);
        newsRepo.save(news);
        LOGGER.warn("add news by " + email + "\n" + newsTitle, AdminService.class);
        String query="insert into tbl_News(newsTitle,newsDetail,newsImage,newsStatus)" +
                "\n\tvalues (\'"+news.getNewsTitle()+"\',\'"+news.getNewsDetail()+"\',\'"+news.getNewsImage()+"\',"+1+");\n";
        String query2="insert into image(name,url,imagePath,description,idParent,type,specifyType)" +
                "\n\tvalues(\'"+image.getName()+"\',\'"+image.getUrl()+"\',\'"+image.getImagePath()+"\',\'"+image.getDescription()+"\',\'"+image.getIdParent()+"\',\'"+image.getType()+"\',\'"+image.getSpecifyType()+"\');\n";
        LogCodeSql.writeCodeSql(query);
        LogCodeSql.writeCodeSql(query2);
        MessageResponse response = new MessageResponse(EnumResponseStatusCode.ADD_NEWS_SUCCESS);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    public ResponseEntity<?> enableNews(List<String> newsIdList, String email) {
        for (String newId : newsIdList) {
            tbl_News news = newsRepo.findByNewsId(Long.valueOf(newId));
            news.setNewsStatus(0);
            newsRepo.save(news);
        }
        LOGGER.warn("enable news by " + email + "\n" + newsIdList, AdminService.class);
        MessageResponse response = new MessageResponse(EnumResponseStatusCode.ENABLE_NEWS_SUCCESS);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    public ResponseEntity<?> addSale(String saleTitle, String saleDetail, String beneficiary, float percentDiscount,
                                     MultipartFile file, String email) {
        tbl_Sale sale = new tbl_Sale(saleTitle, saleDetail, beneficiary, percentDiscount);
        Image image = new Image();
        image.setName(sale.getSaleImage());
        image.setDescription(saleTitle);
        image.setImagePath("/Sale/" + sale.getSaleImage() + ".jpg");
        image.setType("SALE");
        image.setSpecifyType("specifyType");
        image.setIdParent("idParent");
        imageService.createImage(image, file);
        saleRepo.save(sale);
        LOGGER.warn("add sale by " + email + "\n" + saleTitle, AdminService.class);
        String query="insert into tbl_Sale(saleTitle,saleDetail,saleImage,beneficiary,percentDiscount,saleStatus)" +
                "\n\tvalues (\'"+sale.getSaleTitle()+"\',\'"+sale.getSaleDetail()+"\',\'"+sale.getSaleImage()+"\',\'"+sale.getBeneficiary()+"\',"+sale.getPercentDiscount()+","+1+");\n";
        String query2="insert into image(name,url,imagePath,description,idParent,type,specifyType)" +
                "\n\tvalues(\'"+image.getName()+"\',\'"+image.getUrl()+"\',\'"+image.getImagePath()+"\',\'"+image.getDescription()+"\',\'"+image.getIdParent()+"\',\'"+image.getType()+"\',\'"+image.getSpecifyType()+"\');\n";
        LogCodeSql.writeCodeSql(query);
        LogCodeSql.writeCodeSql(query2);
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

}
