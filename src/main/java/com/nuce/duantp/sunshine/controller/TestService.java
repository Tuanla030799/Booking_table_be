package com.nuce.duantp.sunshine.controller;

import com.nuce.duantp.sunshine.dto.model.tbl_Bill;
import com.nuce.duantp.sunshine.repository.BillRepo;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

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
