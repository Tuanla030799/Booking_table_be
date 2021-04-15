package com.nuce.duantp.sunshine.service;

import com.nuce.duantp.sunshine.dto.request.DepositReq;
import com.nuce.duantp.sunshine.dto.request.FoodReq;
import com.nuce.duantp.sunshine.dto.request.OrderFoodReq;
import com.nuce.duantp.sunshine.dto.request.PointReq;
import com.nuce.duantp.sunshine.dto.response.MessageResponse;
import com.nuce.duantp.sunshine.enums.EnumResponseStatusCode;
import com.nuce.duantp.sunshine.model.*;
import com.nuce.duantp.sunshine.repository.*;
import com.nuce.duantp.sunshine.security.jwt.AuthTokenFilter;
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

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class AdminService {
    @Autowired
    private CustomerRepo repository;
    @Autowired
    private DepositRepo depositRepo;
    @Autowired
    private PointsRepo pointsRepo;
    @Autowired
    AuthTokenFilter authTokenFilter;
    @Autowired
    private BookingRepository bookingRepository;
    @Autowired
    private ResponseStatusCodeRepo responseStatusCodeRepo;
    @Autowired
    BillRepo billRepo;
    @Autowired
    BillInfoRepo billInfoRepo;
    private Logger LOGGER = LoggerFactory.getLogger(AdminService.class);

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


    public ResponseEntity<?> addFoodInBooking(OrderFoodReq orderFoodReq, String email) {
        tbl_Bill bill = billRepo.findByBookingId(orderFoodReq.getBookingId());
        String str = "ADD_FOOD_SUCCESS";
        for (FoodReq data : orderFoodReq.getFoodList()) {
            tbl_BillInfo billInfo = billInfoRepo.findByBillIdAndFoodId(bill.getBillId(), data.getFoodId());
            if (billInfo == null) {
                str = bookingRepository.orderFood(orderFoodReq.getBookingId(), bill.getBillId(), data.getQuantity(), data.getFoodId());
            } else {
                int quantity = billInfo.getQuantity() + data.getQuantity();
                billInfo.setQuantity(quantity);
                billInfoRepo.save(billInfo);
            }
        }
        LOGGER.warn("add food by " + email + "\n" + orderFoodReq, AdminService.class);

        tbl_ResponseStatusCode responseStatusCode = responseStatusCodeRepo.findByResponseStatusCode(str);
        MessageResponse response = new MessageResponse(EnumResponseStatusCode.valueOf(responseStatusCode.getResponseStatusCode()), responseStatusCode.getResponseStatusMessage());
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
