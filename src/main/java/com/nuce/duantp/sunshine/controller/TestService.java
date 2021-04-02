package com.nuce.duantp.sunshine.controller;

import com.nuce.duantp.sunshine.dto.response.MessageResponse;
import com.nuce.duantp.sunshine.enums.EnumResponseStatusCode;
import com.nuce.duantp.sunshine.model.tbl_Customer;
import com.nuce.duantp.sunshine.model.tbl_Table;
import com.nuce.duantp.sunshine.repository.TableRepo;
import com.nuce.duantp.sunshine.repository.CustomerRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TestService {
    @Autowired
    private CustomerRepo customerRepo;
    @Autowired
    private TableRepo tableRepo;

    @Transactional(readOnly = false, rollbackFor = IllegalArgumentException.class)
    public void test(tbl_Customer customer) {
        tbl_Customer cus = new tbl_Customer(customer, 100);
        tbl_Table tbl_table = new tbl_Table("b1", 100);
        tableRepo.save(tbl_table);
        customerRepo.save(customer);
        customerRepo.save(cus);
    }


}
