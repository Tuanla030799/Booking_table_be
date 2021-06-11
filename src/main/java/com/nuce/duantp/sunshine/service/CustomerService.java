package com.nuce.duantp.sunshine.service;

import com.nuce.duantp.sunshine.config.TimeUtils;
import com.nuce.duantp.sunshine.config.format.FormatMoney;
import com.nuce.duantp.sunshine.config.format.Validate;
//import com.nuce.duantp.sunshine.dto.response.BookingHistoryDetailRes;
import com.nuce.duantp.sunshine.dto.response.BookingHistoryDetail;
import com.nuce.duantp.sunshine.dto.response.BookingHistoryRes;
import com.nuce.duantp.sunshine.dto.model.*;
import com.nuce.duantp.sunshine.dto.response.PayDetailResponse;
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
    private final BookingService bookingService;
    private final PointsRepo pointsRepo;
    private Logger LOGGER = LoggerFactory.getLogger(CustomerService.class);

    public List<BookingHistoryRes> viewBookingHistory(HttpServletRequest req) {
        Optional<tbl_Customer> customer = authTokenFilter.whoami(req);
        List<tbl_Booking> bookingList = bookingRepository.getListBookingByEmail(customer.get().getEmail());
        List<BookingHistoryRes> data = new ArrayList<>();
        int stt = 1;
        for (tbl_Booking booking : bookingList) {
            tbl_Customer customer1=customerRepo.findCustomerByEmail(booking.getEmail());
            float money = 0L;
            if (booking.getBookingStatus() == 1) {
                money = sunShineService.moneyPay(booking.getBookingId());
            }
            tbl_Deposit deposit = depositRepo.findByDepositId(booking.getDepositId());
            float refund = deposit.getDeposit() - money;
            if (refund < 0) refund = 0L;
            if (booking.getBookingStatus() == 0) {
                refund = 0L;
            }
            Date date = TimeUtils.minusDate(Timestamp.valueOf(String.valueOf(booking.getBookingTime())), 7, "HOUR");
            String status = Validate.convertStatusBooking(booking.getBookingStatus());
            BookingHistoryRes data1 = new BookingHistoryRes(date,
                    FormatMoney.formatMoney(String.valueOf(deposit.getDeposit())), status,
                    FormatMoney.formatMoney(String.valueOf(money)), stt, booking.getBookingId(),
                    FormatMoney.formatMoney(String.valueOf(refund)),customer1.getEmail(),
                    customer1.getPhoneNumber().replaceFirst("(\\d{3})(\\d{3})(\\d+)", "$1-$2-$3"));
            data.add(data1);
            stt++;

        }
        return data;
    }

    public PayDetailResponse viewBookingHistoryDetail(String bookingId) {
        BookingHistoryDetail bookingHistoryDetail = bookingService.getBillPay(bookingId);
        PayDetailResponse payDetailResponse = new PayDetailResponse(bookingHistoryDetail);
        return payDetailResponse;
    }

    public List<tbl_Sale> getListSaleForUser(String email) {
        tbl_Customer customer = customerRepo.findCustomerByEmail(email);
        List<tbl_Sale> saleList = saleRepo.findBySaleStatusAndBeneficiary(1, customer.getBeneficiary());
        return saleList;
    }
}
