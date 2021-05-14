package com.nuce.duantp.sunshine.service;

import com.nuce.duantp.sunshine.config.TimeUtils;
import com.nuce.duantp.sunshine.dto.request.AddAccReq;
import com.nuce.duantp.sunshine.dto.response.BookingHistoryDetailRes;
import com.nuce.duantp.sunshine.dto.response.BookingHistoryRes;
import com.nuce.duantp.sunshine.dto.response.MessageResponse;
import com.nuce.duantp.sunshine.dto.response.PointHistoryRes;
import com.nuce.duantp.sunshine.enums.EnumResponseStatusCode;
import com.nuce.duantp.sunshine.model.*;
import com.nuce.duantp.sunshine.repository.*;
import com.nuce.duantp.sunshine.security.jwt.AuthTokenFilter;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
@RequiredArgsConstructor
public class CustomerService {
    private final CustomerRepo customerRepo;
    private final AuthTokenFilter authTokenFilter;
    private final BookingRepository bookingRepository;
    private final BillRepo billRepo;
    private ResponseStatusCodeRepo responseStatusCodeRepo;
    private final SunShineService sunShineService;
    private final SaleRepo saleRepo;
    private final AccountRepo accountRepo;
    private final DepositRepo depositRepo;
    private final PointsRepo pointsRepo;
    private Logger LOGGER = LoggerFactory.getLogger(CustomerService.class);

    public List<PointHistoryRes> viewHistoryPointUse(HttpServletRequest req) {
        Optional<tbl_Customer> customer = authTokenFilter.whoami(req);
        List<tbl_Booking> bookingList = bookingRepository.findAllByEmailAndBookingStatus(customer.get().getEmail(), 1);
        List<PointHistoryRes> pointHistoryResList = new ArrayList<>();
        int stt = 1;
        for (tbl_Booking data : bookingList) {
            tbl_Bill bill = billRepo.findAllByBookingIdAndDiscountGreaterThan(data.getBookingId(), 0L);
            if (bill != null) {
                Date date1 = TimeUtils.minusDate(Timestamp.valueOf(String.valueOf(bill.getPayDate())), 7, "HOUR");
                PointHistoryRes pointHistoryRes = new PointHistoryRes(date1, bill.getDiscount(), bill.getBookingId(), stt);
                pointHistoryResList.add(pointHistoryRes);
                stt++;
            }
        }
        return pointHistoryResList;
    }

    public List<BookingHistoryRes> viewBookingHistory(HttpServletRequest req) {
        Optional<tbl_Customer> customer = authTokenFilter.whoami(req);
        List<tbl_Booking> bookingList = bookingRepository.findByEmail(customer.get().getEmail());
        List<BookingHistoryRes> data = new ArrayList<>();
        int stt = 1;
        for (tbl_Booking booking : bookingList) {
            float money = 0L;
            if (booking.getBookingStatus() == 1) {
                money = sunShineService.moneyPay(booking.getBookingId());
            }
            tbl_Deposit deposit = depositRepo.findByDepositId(booking.getDepositId());
            Date date = TimeUtils.minusDate(Timestamp.valueOf(String.valueOf(booking.getBookingTime())), 7, "HOUR");
            String status = booking.getBookingStatus() == 1 ? "Đã thanh toán!" : "Chưa thanh toán!";
            BookingHistoryRes data1 = new BookingHistoryRes(date, deposit.getDeposit(), status, money, stt, booking.getBookingId());
            data.add(data1);
            stt++;
        }
        return data;
    }

    public BookingHistoryDetailRes viewBookingHistoryDetail(String bookingId) {
        tbl_Booking booking = bookingRepository.findByBookingId(bookingId);
        float money = 0L;
        if (booking.getBookingStatus() == 1) {
            money = sunShineService.moneyPay(booking.getBookingId());
        }
        tbl_Bill bill = billRepo.findByBookingId(booking.getBookingId());
        tbl_Deposit deposit = depositRepo.findByDepositId(booking.getDepositId());
        tbl_Points points = pointsRepo.findByPointId(bill.getPointId());
        Long point=null;
        if(points!=null){
            point=points.getPrice();
        }
        String status = booking.getBookingStatus() == 1 ? "Đã thanh toán!" : "Chưa thanh toán!";
        Date date = TimeUtils.minusDate(Timestamp.valueOf(String.valueOf(booking.getBookingTime())), 7, "HOUR");
        Date date1 = null;
        if (bill.getPayDate() != null) {
            date1 = TimeUtils.minusDate(Timestamp.valueOf(String.valueOf(bill.getPayDate())), 7, "HOUR");
        }
        BookingHistoryDetailRes data = new BookingHistoryDetailRes(date, deposit.getDeposit(), status,
                booking.getTotalSeats(), booking.getTableName(), point, money, date1, bill.getDiscount(),bookingId);
        return data;
    }

    public ResponseEntity<?> addAccount(AddAccReq addAccReq, HttpServletRequest req) {
        Optional<tbl_Customer> customer = authTokenFilter.whoami(req);
        tbl_BankAccount account = accountRepo.findByAccountNo(addAccReq.getAccountNo());
        if (account == null) {
            List<tbl_BankAccount> bankAccountList = accountRepo.findByEmail(customer.get().getEmail());
            for (tbl_BankAccount data : bankAccountList) {
                data.setStatus(0);
                accountRepo.save(data);
            }
            tbl_BankAccount acc = new tbl_BankAccount(addAccReq.getAccountNo(), 1000000L, 1, customer.get().getEmail());
            accountRepo.save(acc);
            LOGGER.warn("Add AccountAno by " + customer.get().getEmail() + "\n" + addAccReq, CustomerService.class);
            MessageResponse response = new MessageResponse(EnumResponseStatusCode.ADD_ACCOUNT_SUCCESS);
            return new ResponseEntity<>(response, HttpStatus.OK);
        }

        MessageResponse response = new MessageResponse(EnumResponseStatusCode.ACCOUNT_EXISTED);
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);

    }

    public List<tbl_Sale> getListSaleForUser(String email) {
        tbl_Customer customer = customerRepo.findCustomerByEmail(email);
        List<tbl_Sale> saleList = saleRepo.findBySaleStatusAndBeneficiary(1, customer.getBeneficiary());
        return saleList;
    }
}
