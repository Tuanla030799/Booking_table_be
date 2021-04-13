package com.nuce.duantp.sunshine.service;

import com.nuce.duantp.sunshine.dto.response.HistoryPointRes;
import com.nuce.duantp.sunshine.model.tbl_Customer;
import com.nuce.duantp.sunshine.repository.CustomerRepo;
import com.nuce.duantp.sunshine.security.jwt.AuthTokenFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;

@Service
public class CustomerService {
    @Autowired
    CustomerRepo customerRepo;

    @Autowired
    AuthTokenFilter authTokenFilter;

//    public HistoryPointRes viewHistoryPointUse(HttpServletRequest req){
//        Optional<tbl_Customer> customer=authTokenFilter.whoami(req);
//
//    }
}
