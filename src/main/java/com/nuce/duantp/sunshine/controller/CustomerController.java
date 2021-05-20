package com.nuce.duantp.sunshine.controller;

//import com.nuce.duantp.sunshine.dto.request.AddAccReq;

import com.nuce.duantp.sunshine.dto.response.BookingHistoryDetailRes;
import com.nuce.duantp.sunshine.dto.response.BookingHistoryRes;
import com.nuce.duantp.sunshine.dto.response.PointHistoryRes;
import com.nuce.duantp.sunshine.model.tbl_Customer;
import com.nuce.duantp.sunshine.model.tbl_Sale;
import com.nuce.duantp.sunshine.security.jwt.AuthTokenFilter;
import com.nuce.duantp.sunshine.service.CustomerService;
import com.nuce.duantp.sunshine.service.TokenLivingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Optional;

@RestController
@CrossOrigin(origins = "*", maxAge = 3600, allowedHeaders = "*")
@RequestMapping("/api/customer")
public class CustomerController {
    @Autowired
    CustomerService customerService;
    @Autowired
    TokenLivingService tokenLivingService;
//    @Autowired
//    AccountRepo accountRepo;
    @Autowired
    AuthTokenFilter authTokenFilter;

    @GetMapping("/point-used-history")
    public List<PointHistoryRes> viewHistoryPointUse(HttpServletRequest req) {
        if (tokenLivingService.checkTokenLiving(req)) {
            return customerService.viewHistoryPointUse(req);
        } else return null;
    }

    @GetMapping("/booking-history")
    public List<BookingHistoryRes> viewBookingHistory(HttpServletRequest req) {
        if (tokenLivingService.checkTokenLiving(req)) {
            return customerService.viewBookingHistory(req);
        } else return null;
    }

    @GetMapping("/booking-history-detail/{bookingId}")
    public BookingHistoryDetailRes viewBookingHistoryDetail(HttpServletRequest req, @PathVariable String bookingId) {
        if (tokenLivingService.checkTokenLiving(req)) {
            return customerService.viewBookingHistoryDetail(bookingId);
        } else return null;
    }

//    @GetMapping("/get-list-acc")
//    public List<tbl_BankAccount> getLitAcc(HttpServletRequest req) {
//        if (tokenLivingService.checkTokenLiving(req)) {
//            Optional<tbl_Customer> customer = authTokenFilter.whoami(req);
//            List<tbl_BankAccount> list = accountRepo.findByEmail(customer.get().getEmail());
//            return list;
//        } else return null;
//
//    }

//    @PostMapping("/add-bank-acc")
//    public ResponseEntity<?> cancelBooking(@RequestBody AddAccReq addAccReq, HttpServletRequest req) {
//        if (tokenLivingService.checkTokenLiving(req)) {
//            return customerService.addAccount(addAccReq, req);
//        }
//        MessageResponse messageResponse = new MessageResponse(EnumResponseStatusCode.TOKEN_DIE);
//        return new ResponseEntity<>(messageResponse, HttpStatus.BAD_REQUEST);
//    }

    @GetMapping("/get-list-sale-for-user")
    public List<tbl_Sale> getListSaleForUser( HttpServletRequest req) {
        if (tokenLivingService.checkTokenLiving(req)) {
            Optional<tbl_Customer> customer = authTokenFilter.whoami(req);
            return customerService.getListSaleForUser(customer.get().getEmail());
        }
        return null;

    }
}
