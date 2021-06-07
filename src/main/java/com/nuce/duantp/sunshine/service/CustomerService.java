package com.nuce.duantp.sunshine.service;

import com.nuce.duantp.sunshine.config.TimeUtils;
import com.nuce.duantp.sunshine.config.format.FormatMoney;
import com.nuce.duantp.sunshine.config.format.Validate;
import com.nuce.duantp.sunshine.dto.response.BookingHistoryDetailRes;
import com.nuce.duantp.sunshine.dto.response.BookingHistoryRes;
import com.nuce.duantp.sunshine.dto.model.*;
import com.nuce.duantp.sunshine.repository.*;
import com.nuce.duantp.sunshine.security.jwt.AuthTokenFilter;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CustomerService {
    private final CustomerRepo customerRepo;
    private final AuthTokenFilter authTokenFilter;
    private final BookingRepository bookingRepository;
    private final BillRepo billRepo;
    private final SunShineService sunShineService;
    private final SaleRepo saleRepo;
    private final DepositRepo depositRepo;
    private final PointsRepo pointsRepo;
    private Logger LOGGER = LoggerFactory.getLogger(CustomerService.class);

    public List<BookingHistoryRes> viewBookingHistory(HttpServletRequest req) {
        Optional<tbl_Customer> customer = authTokenFilter.whoami(req);
        List<tbl_Booking> bookingList = bookingRepository.findAllByEmailOrderByBookingTimeDesc(customer.get().getEmail());
        List<BookingHistoryRes> data = new ArrayList<>();
        int stt = 1;
        for (tbl_Booking booking : bookingList) {
            float money = 0L;
            if (booking.getBookingStatus() == 1) {
                money = sunShineService.moneyPay(booking.getBookingId());
            }
            tbl_Deposit deposit = depositRepo.findByDepositId(booking.getDepositId());
            float refund=deposit.getDeposit()-money;
            if(refund<0) refund=0L;
            if(booking.getBookingStatus()==0){
                refund =0L;
            }
            Date date = TimeUtils.minusDate(Timestamp.valueOf(String.valueOf(booking.getBookingTime())), 7, "HOUR");
            String status = Validate.convertStatusBooking(booking.getBookingStatus());
            BookingHistoryRes data1 = new BookingHistoryRes(date, FormatMoney.formatMoney(String.valueOf(deposit.getDeposit())),
                    status, FormatMoney.formatMoney(String.valueOf(money)), stt, booking.getBookingId(),FormatMoney.formatMoney(String.valueOf(refund)));
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
        Long point = null;
        if (points != null) {
            point = points.getPrice();
        }
        String status = Validate.convertStatusBooking(booking.getBookingStatus());
        Date date = TimeUtils.minusDate(Timestamp.valueOf(String.valueOf(booking.getBookingTime())), 7, "HOUR");
        Date date1 = null;
        if (bill.getPayDate() != null) {
            date1 = TimeUtils.minusDate(Timestamp.valueOf(String.valueOf(bill.getPayDate())), 7, "HOUR");
        }
        BookingHistoryDetailRes data = new BookingHistoryDetailRes(date, FormatMoney.formatMoney(String.valueOf(deposit.getDeposit())), status,
                booking.getTotalSeats(), booking.getTableName(), FormatMoney.formatMoney(String.valueOf(point)),
                FormatMoney.formatMoney(String.valueOf(money)), date1,
                bookingId);
        return data;
    }
    public List<tbl_Sale> getListSaleForUser(String email) {
        tbl_Customer customer = customerRepo.findCustomerByEmail(email);
        List<tbl_Sale> saleList = saleRepo.findBySaleStatusAndBeneficiary(1, customer.getBeneficiary());
        return saleList;
    }
}
