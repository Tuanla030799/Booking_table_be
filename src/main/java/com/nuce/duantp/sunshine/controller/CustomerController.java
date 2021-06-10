package com.nuce.duantp.sunshine.controller;

//import com.nuce.duantp.sunshine.dto.request.AddAccReq;

//import com.nuce.duantp.sunshine.dto.response.BookingHistoryDetailRes;
import com.nuce.duantp.sunshine.dto.response.BookingHistoryRes;
import com.nuce.duantp.sunshine.dto.model.tbl_Customer;
import com.nuce.duantp.sunshine.dto.model.tbl_Sale;
import com.nuce.duantp.sunshine.dto.response.PayDetailResponse;
import com.nuce.duantp.sunshine.security.jwt.AuthTokenFilter;
import com.nuce.duantp.sunshine.service.CustomerService;
import com.nuce.duantp.sunshine.service.TokenLivingService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Optional;

@RestController
@CrossOrigin(origins = "*", maxAge = 3600, allowedHeaders = "*")
@RequestMapping("/api/customer")
@RequiredArgsConstructor
public class CustomerController {
    
    private final CustomerService customerService;
    private final TokenLivingService tokenLivingService;
    private final  AuthTokenFilter authTokenFilter;


    @GetMapping("/booking-history")
    public List<BookingHistoryRes> viewBookingHistory(HttpServletRequest req) {
        if (tokenLivingService.checkTokenLiving(req)) {
            return customerService.viewBookingHistory(req);
        } else return null;
    }

    @GetMapping("/booking-history-detail/{bookingId}")
    public PayDetailResponse viewBookingHistoryDetail(HttpServletRequest req, @PathVariable String bookingId) {
        if (tokenLivingService.checkTokenLiving(req)) {
            return customerService.viewBookingHistoryDetail(bookingId);
        } else return null;
    }

    @GetMapping("/get-list-sale-for-user")
    public List<tbl_Sale> getListSaleForUser( HttpServletRequest req) {
        if (tokenLivingService.checkTokenLiving(req)) {
            Optional<tbl_Customer> customer = authTokenFilter.whoami(req);
            return customerService.getListSaleForUser(customer.get().getEmail());
        }
        return null;
    }
}
