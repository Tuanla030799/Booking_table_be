package com.nuce.duantp.sunshine.service;

import com.nuce.duantp.sunshine.config.TimeUtils;
import com.nuce.duantp.sunshine.config.format.FormatMoney;
import com.nuce.duantp.sunshine.config.format.Validate;
//import com.nuce.duantp.sunshine.dto.response.BookingHistoryDetailRes;
import com.nuce.duantp.sunshine.dto.response.BookingHistoryDetail;
import com.nuce.duantp.sunshine.dto.response.BookingHistoryRes;
import com.nuce.duantp.sunshine.dto.model.*;
import com.nuce.duantp.sunshine.dto.response.ListFoodInBooking;
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
    private final BillInfoRepo billInfoRepo;
    private final FoodRepo foodRepo;
    private final PointsRepo pointsRepo;
    private Logger LOGGER = LoggerFactory.getLogger(CustomerService.class);

    public List<BookingHistoryRes> viewBookingHistory(HttpServletRequest req) {
        Optional<tbl_Customer> customer = authTokenFilter.whoami(req);
        List<tbl_Booking> bookingList = bookingRepository.getListBookingByEmail(customer.get().getEmail());
        List<BookingHistoryRes> data = new ArrayList<>();
        int stt = 1;
        for (tbl_Booking booking : bookingList) {

            tbl_Bill bill = billRepo.findByBookingId(booking.getBookingId());
            List<ListFoodInBooking> listFoodInBookings =new ArrayList<>();
            float totalMoneyFood = 0L; //lấy ra tổng số tiền cho đặt món
            int stt1=1;
            List<tbl_BillInfo> billInfoList=billInfoRepo.findAllByBillId(bill.getBillId());
            for(tbl_BillInfo billInfo: billInfoList){
                tbl_Food food=foodRepo.findByFoodId(billInfo.getFoodId());
                ListFoodInBooking listFoodInBooking =new ListFoodInBooking(stt, food.getFoodName(),
                        FormatMoney.formatMoney(String.valueOf(food.getFoodPrice())),
                        FormatMoney.formatMoney(String.valueOf(food.getFoodPrice()* billInfo.getQuantity())),
                        billInfo.getQuantity());
                listFoodInBookings.add(listFoodInBooking);
                stt1++;
                totalMoneyFood+=food.getFoodPrice()*billInfo.getQuantity();
            }
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
                    FormatMoney.formatMoney(String.valueOf(refund)),listFoodInBookings);
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
