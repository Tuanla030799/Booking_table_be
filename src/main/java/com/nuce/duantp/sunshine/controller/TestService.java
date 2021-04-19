package com.nuce.duantp.sunshine.controller;

import com.nuce.duantp.sunshine.dto.response.MessageResponse;
import com.nuce.duantp.sunshine.enums.EnumResponseStatusCode;
import com.nuce.duantp.sunshine.model.tbl_Bill;
import com.nuce.duantp.sunshine.model.tbl_Customer;
import com.nuce.duantp.sunshine.model.tbl_Table;
import com.nuce.duantp.sunshine.repository.BillRepo;
import com.nuce.duantp.sunshine.repository.TableRepo;
import com.nuce.duantp.sunshine.repository.CustomerRepo;
import org.modelmapper.ModelMapper;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.logging.Logger;

@Service
@Transactional( propagation = Propagation.SUPPORTS,readOnly = true )
public class TestService {
    //    @Autowired
//    private CustomerRepo customerRepo;
//    @Autowired
//    private TableRepo tableRepo;
//
//    @Transactional(readOnly = false, rollbackFor = IllegalArgumentException.class)
//    public void test(tbl_Customer customer) {
//
//        tbl_Table tbl_table = new tbl_Table("b1", 100);
//        tableRepo.save(tbl_table);
//        customerRepo.save(customer);
//
//    }
    @Autowired
    BillRepo billRepo;

    public void testTransaction(tbl_Bill bill) {
        ModelMapper mapper = new ModelMapper();
        tbl_Bill bill1 = mapper.map(bill, tbl_Bill.class);
        billRepo.save(bill1);
        tbl_Bill bill2 = new tbl_Bill(bill, 12L);
        billRepo.save(bill2);
    }

}
