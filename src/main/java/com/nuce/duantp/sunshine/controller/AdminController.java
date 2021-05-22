package com.nuce.duantp.sunshine.controller;

import com.nuce.duantp.sunshine.dto.News;
import com.nuce.duantp.sunshine.dto.request.*;
import com.nuce.duantp.sunshine.dto.response.BookingHistoryRes;
import com.nuce.duantp.sunshine.dto.response.MessageResponse;
import com.nuce.duantp.sunshine.enums.BeneficiaryEnum;
import com.nuce.duantp.sunshine.enums.EnumResponseStatusCode;
import com.nuce.duantp.sunshine.model.tbl_Booking;
import com.nuce.duantp.sunshine.model.tbl_Customer;
import com.nuce.duantp.sunshine.repository.BookingRepository;
import com.nuce.duantp.sunshine.security.jwt.AuthTokenFilter;
import com.nuce.duantp.sunshine.service.AdminService;
import com.nuce.duantp.sunshine.service.BookingService;
import com.nuce.duantp.sunshine.service.TokenLivingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@CrossOrigin(origins = "*", maxAge = 3600, allowedHeaders = "*")
@RequestMapping("/api/admin")
public class AdminController {
    @Autowired
    AdminService adminService;
    @Autowired
    BookingService bookingService;
    @Autowired
    AuthTokenFilter authTokenFilter;
    @Autowired
    TokenLivingService tokenLivingService;
    @Autowired
    BookingRepository bookingRepository;

    @GetMapping("/export-file")
    public ResponseEntity<?> exportFile(@RequestBody String fileName, HttpServletRequest req) {
        Optional<tbl_Customer> customer = authTokenFilter.whoami(req);
        if (tokenLivingService.checkTokenLiving(req) && customer.get().getRole().equals("ADMIN")) {
            try {
                adminService.exportReport(fileName);
                MessageResponse messageResponse = new MessageResponse(EnumResponseStatusCode.SUCCESS, EnumResponseStatusCode.SUCCESS.label);
                return new ResponseEntity<>(messageResponse, HttpStatus.OK);
            } catch (Exception e) {
                e.printStackTrace();
                MessageResponse messageResponse = new MessageResponse(EnumResponseStatusCode.BAD_REQUEST, EnumResponseStatusCode.BAD_REQUEST.label);
                return new ResponseEntity<>(messageResponse, HttpStatus.BAD_REQUEST);
            }
        }
        MessageResponse messageResponse = new MessageResponse(EnumResponseStatusCode.TOKEN_DIE);
        return new ResponseEntity<>(messageResponse, HttpStatus.BAD_REQUEST);

    }

    @GetMapping("/export-bill/{bookingId}")
    public ResponseEntity<?> exportBill(@PathVariable(name = "bookingId") String bookingId, HttpServletRequest req) {
        Optional<tbl_Customer> customer = authTokenFilter.whoami(req);
        if (tokenLivingService.checkTokenLiving(req) && customer.get().getRole().equals("ADMIN")) {
            try {
                adminService.exportBill(bookingId);
                MessageResponse messageResponse = new MessageResponse(EnumResponseStatusCode.SUCCESS, EnumResponseStatusCode.SUCCESS.label);
                return new ResponseEntity<>(messageResponse, HttpStatus.OK);
            } catch (Exception e) {
                e.printStackTrace();
                MessageResponse messageResponse = new MessageResponse(EnumResponseStatusCode.BAD_REQUEST, EnumResponseStatusCode.BAD_REQUEST.label);
                return new ResponseEntity<>(messageResponse, HttpStatus.BAD_REQUEST);
            }
        }
        MessageResponse messageResponse = new MessageResponse(EnumResponseStatusCode.TOKEN_DIE);
        return new ResponseEntity<>(messageResponse, HttpStatus.BAD_REQUEST);

    }

    @PostMapping("/cancel-booking-admin")
    public ResponseEntity<?> cancelBookingAdmin(@RequestBody CancelBookingReq cancelBookingReq, HttpServletRequest req) {
        Optional<tbl_Customer> customer = authTokenFilter.whoami(req);
        if (tokenLivingService.checkTokenLiving(req) && customer.get().getRole().equals("ADMIN")) {
            return bookingService.cancelBookingAdmin(cancelBookingReq, customer.get().getEmail());
        }
        MessageResponse messageResponse = new MessageResponse(EnumResponseStatusCode.TOKEN_DIE);
        return new ResponseEntity<>(messageResponse, HttpStatus.BAD_REQUEST);

    }

    @PostMapping("/add-deposit")
    public ResponseEntity<?> addDeposit(@RequestBody @Validated DepositReq depositReq, HttpServletRequest req) {
        Optional<tbl_Customer> customer = authTokenFilter.whoami(req);
        if (tokenLivingService.checkTokenLiving(req) && customer.get().getRole().equals("ADMIN")) {
            return adminService.changeDeposit(depositReq, customer.get().getEmail());
        }
        MessageResponse messageResponse = new MessageResponse(EnumResponseStatusCode.TOKEN_DIE);
        return new ResponseEntity<>(messageResponse, HttpStatus.BAD_REQUEST);

    }

    @PostMapping("/add-point")
    public ResponseEntity<?> addPoint(@RequestBody @Validated PointReq pointReq, HttpServletRequest req) {
        Optional<tbl_Customer> customer = authTokenFilter.whoami(req);
        if (tokenLivingService.checkTokenLiving(req) && customer.get().getRole().equals("ADMIN")) {
            return adminService.changePoint(pointReq, customer.get().getEmail());
        }
        MessageResponse messageResponse = new MessageResponse(EnumResponseStatusCode.TOKEN_DIE);
        return new ResponseEntity<>(messageResponse, HttpStatus.BAD_REQUEST);

    }

    @PostMapping("/order-food")
    public ResponseEntity<?> orderFood(@RequestBody OrderFoodReq orderFoodReq, HttpServletRequest req) {
        Optional<tbl_Customer> customer = authTokenFilter.whoami(req);
        if (tokenLivingService.checkTokenLiving(req) && customer.get().getRole().equals("ADMIN")) {
            return bookingService.orderFood(orderFoodReq, customer.get().getEmail());
        }
        MessageResponse messageResponse = new MessageResponse(EnumResponseStatusCode.TOKEN_DIE);
        return new ResponseEntity<>(messageResponse, HttpStatus.BAD_REQUEST);
    }

    @GetMapping("/get-list-booking")
    public List<BookingHistoryRes> getListBooking(HttpServletRequest req) {
        Optional<tbl_Customer> customer = authTokenFilter.whoami(req);
        if (tokenLivingService.checkTokenLiving(req) && customer.get().getRole().equals("ADMIN")) {
            return adminService.viewBookingHistory(req);
        }
        return null;
    }

    @PostMapping("/add-food-in-booking")
    public ResponseEntity<?> addFoodInBooking(@RequestBody OrderFoodReq orderFoodReq, HttpServletRequest req) {
        Optional<tbl_Customer> customer = authTokenFilter.whoami(req);
        if (tokenLivingService.checkTokenLiving(req) && customer.get().getRole().equals("ADMIN")) {
            return adminService.addFoodInBooking(orderFoodReq, customer.get().getEmail());
        }
        MessageResponse messageResponse = new MessageResponse(EnumResponseStatusCode.TOKEN_DIE);
        return new ResponseEntity<>(messageResponse, HttpStatus.BAD_REQUEST);

    }

    @PostMapping("/add-food-in-menu")
    public ResponseEntity<?> addFoodInMenu(@RequestParam(value = "file") MultipartFile file, @RequestParam(value = "foodName") String foodName, @RequestParam("describe") String describe, @RequestParam(value = "foodPrice") Long foodPrice, HttpServletRequest req) {
        Optional<tbl_Customer> customer = authTokenFilter.whoami(req);
        if (tokenLivingService.checkTokenLiving(req) && customer.get().getRole().equals("ADMIN")) {
            return adminService.addFoodInMenu(foodName, foodPrice, describe, customer.get().getEmail(), file);
        }
        MessageResponse messageResponse = new MessageResponse(EnumResponseStatusCode.TOKEN_DIE);
        return new ResponseEntity<>(messageResponse, HttpStatus.BAD_REQUEST);

    }

    @PostMapping("/enable-food")
    public ResponseEntity<?> enableFood(@RequestBody List<String> foodIdList, HttpServletRequest req) {
        Optional<tbl_Customer> customer = authTokenFilter.whoami(req);
        if (tokenLivingService.checkTokenLiving(req) && customer.get().getRole().equals("ADMIN")) {
            return adminService.enableFood(foodIdList, customer.get().getEmail());
        }
        MessageResponse messageResponse = new MessageResponse(EnumResponseStatusCode.TOKEN_DIE);
        return new ResponseEntity<>(messageResponse, HttpStatus.BAD_REQUEST);

    }

    @PostMapping("/add-news")
    public ResponseEntity<?> addNews(@ModelAttribute News news, HttpServletRequest req) {
        Optional<tbl_Customer> customer = authTokenFilter.whoami(req);
        if (tokenLivingService.checkTokenLiving(req) && customer.get().getRole().equals("ADMIN")) {
            return adminService.addNews(news, customer.get().getEmail());
        }
        MessageResponse messageResponse = new MessageResponse(EnumResponseStatusCode.TOKEN_DIE);
        return new ResponseEntity<>(messageResponse, HttpStatus.BAD_REQUEST);

    }

    @PostMapping("/enable-news")
    public ResponseEntity<?> enableNews(@RequestBody List<String> newsIdList, HttpServletRequest req) {
        Optional<tbl_Customer> customer = authTokenFilter.whoami(req);
        if (tokenLivingService.checkTokenLiving(req) && customer.get().getRole().equals("ADMIN")) {
            return adminService.enableNews(newsIdList, customer.get().getEmail());
        }
        MessageResponse messageResponse = new MessageResponse(EnumResponseStatusCode.TOKEN_DIE);
        return new ResponseEntity<>(messageResponse, HttpStatus.BAD_REQUEST);

    }

    @PostMapping("/add-sale")
    public ResponseEntity<?> addSale(@RequestParam(value = "file") MultipartFile file, @RequestParam(value = "saleTitle") String saleTitle, @RequestParam("saleDetail") String saleDetail, @RequestParam("beneficiary") String beneficiary, @RequestParam("percentDiscount") float percentDiscount, HttpServletRequest req) {
        Optional<tbl_Customer> customer = authTokenFilter.whoami(req);
        if (tokenLivingService.checkTokenLiving(req) && customer.get().getRole().equals("ADMIN")) {
            return adminService.addSale(saleTitle, saleDetail, beneficiary, percentDiscount, file, customer.get().getEmail());
        }
        MessageResponse messageResponse = new MessageResponse(EnumResponseStatusCode.TOKEN_DIE);
        return new ResponseEntity<>(messageResponse, HttpStatus.BAD_REQUEST);

    }

    @PostMapping("/enable-sale")
    public ResponseEntity<?> enableSale(@RequestBody List<String> saleIdList, HttpServletRequest req) {
        Optional<tbl_Customer> customer = authTokenFilter.whoami(req);
        if (tokenLivingService.checkTokenLiving(req) && customer.get().getRole().equals("ADMIN")) {
            return adminService.enableSale(saleIdList, customer.get().getEmail());
        }
        MessageResponse messageResponse = new MessageResponse(EnumResponseStatusCode.TOKEN_DIE);
        return new ResponseEntity<>(messageResponse, HttpStatus.BAD_REQUEST);

    }

}
