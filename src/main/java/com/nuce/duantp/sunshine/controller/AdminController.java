package com.nuce.duantp.sunshine.controller;

import com.nuce.duantp.sunshine.dto.request.*;
import com.nuce.duantp.sunshine.dto.response.BookingHistoryRes;
import com.nuce.duantp.sunshine.dto.response.MessageResponse;
import com.nuce.duantp.sunshine.dto.response.PayDetailResponse;
import com.nuce.duantp.sunshine.dto.response.UserDetail;
import com.nuce.duantp.sunshine.dto.enums.EnumResponseStatusCode;
import com.nuce.duantp.sunshine.dto.model.tbl_Customer;
import com.nuce.duantp.sunshine.security.jwt.AuthTokenFilter;
import com.nuce.duantp.sunshine.service.AdminService;
import com.nuce.duantp.sunshine.service.BookingService;
import com.nuce.duantp.sunshine.service.TokenLivingService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@CrossOrigin(origins = "*", maxAge = 3600, allowedHeaders = "*")
@RequestMapping("/api/admin")
public class AdminController {

    private final AdminService adminService;
    private final  BookingService bookingService;
    private final  AuthTokenFilter authTokenFilter;
    private final TokenLivingService tokenLivingService;

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
                return adminService.exportBill(bookingId);

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

    @PostMapping("/disable-food/{foodId}")
    public ResponseEntity<?> disableFood(@PathVariable(name = "foodId")Long foodId, HttpServletRequest req) {
        Optional<tbl_Customer> customer = authTokenFilter.whoami(req);
        if (tokenLivingService.checkTokenLiving(req) && customer.get().getRole().equals("ADMIN")) {
            return adminService.disableFood(foodId, customer.get().getEmail());
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

    @PostMapping("/disable-news")
    public ResponseEntity<?> disableNews(@RequestBody Long newsIdList, HttpServletRequest req) {
        Optional<tbl_Customer> customer = authTokenFilter.whoami(req);
        if (tokenLivingService.checkTokenLiving(req) && customer.get().getRole().equals("ADMIN")) {
            return adminService.enableNews(newsIdList, customer.get().getEmail());
        }
        MessageResponse messageResponse = new MessageResponse(EnumResponseStatusCode.TOKEN_DIE);
        return new ResponseEntity<>(messageResponse, HttpStatus.BAD_REQUEST);

    }

    @PostMapping("/add-sale")
    public ResponseEntity<?> addSale(@RequestParam(value = "file") MultipartFile file, @RequestParam(value =
            "saleTitle") String saleTitle, @RequestParam("saleDetail") String saleDetail,
                                     @RequestParam("beneficiary") String beneficiary,
                                     @RequestParam("totalBill") float totalBill,
                                     @RequestParam("percentDiscount") float percentDiscount, HttpServletRequest req) {
        Optional<tbl_Customer> customer = authTokenFilter.whoami(req);
        if (tokenLivingService.checkTokenLiving(req) && customer.get().getRole().equals("ADMIN")) {
            return adminService.addSale(saleTitle, saleDetail, beneficiary, percentDiscount,totalBill, file,
                    customer.get().getEmail());
        }
        MessageResponse messageResponse = new MessageResponse(EnumResponseStatusCode.TOKEN_DIE);
        return new ResponseEntity<>(messageResponse, HttpStatus.BAD_REQUEST);

    }
    @GetMapping("/list-user")
    public List<UserDetail> getListUser(HttpServletRequest req) {
        Optional<tbl_Customer> customer = authTokenFilter.whoami(req);
        if (tokenLivingService.checkTokenLiving(req) && customer.get().getRole().equals("ADMIN")) {
            return adminService.getListUser();
        }
        return null;

    }

    @GetMapping("/user-detail/{email}")
    public UserDetail userDetail(@PathVariable(name = "email") String email,HttpServletRequest req) {
        Optional<tbl_Customer> customer = authTokenFilter.whoami(req);
        if (tokenLivingService.checkTokenLiving(req) && customer.get().getRole().equals("ADMIN")) {
            return adminService.userDetail(email);
        }
        return null;

    }

    @PostMapping("/disable-sale/{saleId}")
    public ResponseEntity<?> disableSale(@PathVariable(name = "saleId") Long saleId, HttpServletRequest req) {
        Optional<tbl_Customer> customer = authTokenFilter.whoami(req);
        if (tokenLivingService.checkTokenLiving(req) && customer.get().getRole().equals("ADMIN")) {
            return adminService.disableSale(saleId, customer.get().getEmail());
        }
        MessageResponse messageResponse = new MessageResponse(EnumResponseStatusCode.TOKEN_DIE);
        return new ResponseEntity<>(messageResponse, HttpStatus.BAD_REQUEST);

    }

    @PostMapping("/pay-bill/{bookingId}")
    public ResponseEntity<?> payBill(@PathVariable(name = "bookingId") String bookingId, HttpServletRequest req){
        Optional<tbl_Customer> customer = authTokenFilter.whoami(req);
        if (tokenLivingService.checkTokenLiving(req) && customer.get().getRole().equals("ADMIN")) {
            return adminService.palBill(bookingId);
        }
        MessageResponse messageResponse = new MessageResponse(EnumResponseStatusCode.TOKEN_DIE);
        return new ResponseEntity<>(messageResponse, HttpStatus.BAD_REQUEST);
    }
    @PostMapping("/Charging")
    public ResponseEntity<?> payBill(@RequestBody ChargingReq chargingReq, HttpServletRequest req){
        Optional<tbl_Customer> customer = authTokenFilter.whoami(req);
        if (tokenLivingService.checkTokenLiving(req) && customer.get().getRole().equals("ADMIN")) {
            return adminService.Charging(chargingReq);
        }
        MessageResponse messageResponse = new MessageResponse(EnumResponseStatusCode.TOKEN_DIE);
        return new ResponseEntity<>(messageResponse, HttpStatus.BAD_REQUEST);
    }

    @PostMapping("/disable-customer/{email}")
    public ResponseEntity<?> disableCustomer(@PathVariable(name = "email") String email, HttpServletRequest req) {
        Optional<tbl_Customer> customer = authTokenFilter.whoami(req);
        if (tokenLivingService.checkTokenLiving(req) && customer.get().getRole().equals("ADMIN")) {
            return adminService.disableAccCustomer(email);
        }
        MessageResponse messageResponse = new MessageResponse(EnumResponseStatusCode.TOKEN_DIE);
        return new ResponseEntity<>(messageResponse, HttpStatus.BAD_REQUEST);

    }

    @GetMapping("/search-customer/{string}")
    public ResponseEntity<?> searchCustomer(@PathVariable(name = "string") String string, HttpServletRequest req) {
        Optional<tbl_Customer> customer = authTokenFilter.whoami(req);
        if (tokenLivingService.checkTokenLiving(req) && customer.get().getRole().equals("ADMIN")) {
            return adminService.searchCustomer(string);
        }
        MessageResponse messageResponse = new MessageResponse(EnumResponseStatusCode.TOKEN_DIE);
        return new ResponseEntity<>(messageResponse, HttpStatus.BAD_REQUEST);
    }

//    @GetMapping("/get-file-report")
//    public ByteArrayResource getFileReport(HttpServletRequest req) {
//        List<BookingHistoryRes> payDetailResponses =adminService.viewBookingHistory(req);
//        return reportServiceImpl.exportReport(vimoRiskTransList);
//    }
//    @GetMapping("/export-excel")
//    public ResponseEntity<byte[]> exportToExcel(@RequestParam(name = "type")String type,
//                                                @RequestParam(name = "date") String date) {
//        VimoRiskTransResponse vimoRiskTransList = vimoRiskTransService.searchRisk(searchRiskReqDTO, PageRequest.of(1, (int) vimoRiskTransService.totalSearchRick(searchRiskReqDTO)));
//        ByteArrayResource resource = reportServiceImpl.exportReport(vimoRiskTransList.getRiskTransList());
//        return ResponseEntity.ok()
//                .contentType(MediaType.parseMediaType("application/vnd.ms-excel"))
//                .body(resource.getByteArray());
//    }
}
