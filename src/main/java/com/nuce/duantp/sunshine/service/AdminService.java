package com.nuce.duantp.sunshine.service;

import com.nuce.duantp.sunshine.dto.request.DepositReq;
import com.nuce.duantp.sunshine.dto.request.PointReq;
import com.nuce.duantp.sunshine.dto.response.MessageResponse;
import com.nuce.duantp.sunshine.enums.EnumResponseStatusCode;
import com.nuce.duantp.sunshine.model.tbl_Customer;
import com.nuce.duantp.sunshine.model.tbl_Deposit;
import com.nuce.duantp.sunshine.model.tbl_Points;
import com.nuce.duantp.sunshine.repository.CustomerRepo;
import com.nuce.duantp.sunshine.repository.DepositRepo;
import com.nuce.duantp.sunshine.repository.PointsRepo;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.modelmapper.ModelMapper;
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

    public ResponseEntity<?> changeDeposit(DepositReq depositReq){
        List<tbl_Deposit> tbl_deposit=depositRepo.findAllByTotalPersons(depositReq.getTotalPersons());
        if(tbl_deposit.size()>0){
            for (tbl_Deposit deposit:tbl_deposit){
                deposit.setDepositStatus(0);
                depositRepo.save(deposit);
            }
        }
        ModelMapper modelMap=new ModelMapper();
        tbl_Deposit deposit=modelMap.map(depositReq,tbl_Deposit.class);
        depositRepo.save(deposit);
        MessageResponse messageResponse=new MessageResponse(EnumResponseStatusCode.SUCCESS);
        return new ResponseEntity<>(messageResponse, HttpStatus.OK);
    }

    public ResponseEntity<?> changePoint(PointReq pointReq){
        List<tbl_Points> pointsList=pointsRepo.findAllByPrice(pointReq.getPrice());
        if(pointsList.size()>0){
            for (tbl_Points data:pointsList){
                data.setPointStatus(0);
                pointsRepo.save(data);
            }
        }
        ModelMapper modelMap=new ModelMapper();
        tbl_Points data=modelMap.map(pointReq,tbl_Points.class);
        pointsRepo.save(data);
        MessageResponse messageResponse=new MessageResponse(EnumResponseStatusCode.SUCCESS);
        return new ResponseEntity<>(messageResponse, HttpStatus.OK);
    }

}
