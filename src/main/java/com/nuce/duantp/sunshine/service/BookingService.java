package com.nuce.duantp.sunshine.service;


import com.nuce.duantp.sunshine.config.TimeUtils;
import com.nuce.duantp.sunshine.dto.request.*;
import com.nuce.duantp.sunshine.dto.response.MessageResponse;
import com.nuce.duantp.sunshine.enums.EnumResponseStatusCode;
import com.nuce.duantp.sunshine.model.*;
import com.nuce.duantp.sunshine.repository.*;
import com.nuce.duantp.sunshine.security.jwt.AuthTokenFilter;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BookingService {
    //    @Autowired
    private final BookingRepository bookingRepository;
    //    @Autowired
    private final ResponseStatusCodeRepo responseStatusCodeRepo;
    //    @Autowired
    private final AuthTokenFilter authTokenFilter;
    //    @Autowired
    private final TableRepo tableRepo;
    //    @Autowired
    private final BillRepo billRepo;
    //    @Autowired
    private final SaleRepo saleRepo;
    //    @Autowired
    private final AccountRepo accountRepo;
    //    @Autowired
    private final DepositRepo depositRepo;
    //    @Autowired
    private final PointsRepo pointsRepo;
    //    @Autowired
    private final BillInfoRepo billInfoRepo;
    private final CustomerRepo customerRepo;
    private Logger LOGGER = LoggerFactory.getLogger(BookingService.class);

    public ResponseEntity<?> bookingTable(BookingReq bookingReq, HttpServletRequest req) {
        Optional<tbl_Customer> customer = authTokenFilter.whoami(req);

        String tableName = null;
        List<tbl_Table> tableList = tableRepo.findBySeatGreaterThanEqualOrderBySeatAsc(bookingReq.getTotalSeats());
        for (tbl_Table table : tableList) {
            Date date1 = TimeUtils.minusDate(Timestamp.valueOf(bookingReq.getBookingTime()), -3, "HOUR");
            Date date2 = TimeUtils.minusDate(Timestamp.valueOf(bookingReq.getBookingTime()), 3, "HOUR");
            List<tbl_Booking> bookingList = bookingRepository.findByBookingStatusAndTableNameAndBookingTimeBetween(0, table.getTablename(), date1, date2);
            if (bookingList.size() < table.getStillEmpty()) {
                tableName = table.getTablename();
                break;
            }
        }

        MessageResponse messageResponse = new MessageResponse();
        tbl_ResponseStatusCode responseStatusCode = new tbl_ResponseStatusCode();
        if (tableName == null) {
            responseStatusCode = responseStatusCodeRepo.findByResponseStatusCode(String.valueOf(EnumResponseStatusCode.TABLE_OFF));
            messageResponse.setMessage(responseStatusCode.getResponseStatusMessage());
            messageResponse.setStatus(EnumResponseStatusCode.valueOf(responseStatusCode.getResponseStatusCode()));
            return new ResponseEntity<>(messageResponse, HttpStatus.OK);
        }
        String str = null;
        tbl_BankAccount accNo = accountRepo.findByAccountNo(bookingReq.getAccountNo());

        tbl_Deposit deposit = depositRepo.findTopByTotalPersonsLessThanEqualOrderByTotalPersonsAscCreatedDesc(bookingReq.getTotalSeats());
        if (accNo == null || deposit == null) {
            MessageResponse response = new MessageResponse(EnumResponseStatusCode.BAD_REQUEST);
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }

        Date date = new Date();
        String bookingId = String.valueOf(date.getTime());
        try {
            date = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").parse(bookingReq.getBookingTime());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        if (date.getHours() >= 8 && date.getHours() < 23) {
            tbl_Booking booking = new tbl_Booking(bookingId, customer.get().getEmail(), date, bookingReq.getTotalSeats(), deposit.getDepositId(), 0, tableName, 0L);
            tbl_BankAccount acc1 = accountRepo.findByAccountNo(accNo.getAccountNo());
            acc1.setBalance(accNo.getBalance() - deposit.getDeposit());
            tbl_BankAccount acc2 = accountRepo.findByAccountNo("sunshine87lethanhnghi@gmail.com");
            acc2.setBalance(acc2.getBalance() + deposit.getDeposit());
            tbl_Bill bill = new tbl_Bill(bookingId, pointsRepo.findByPointId(0L).getPointId(), bookingId, 0L, 0);
            MessageResponse response = new MessageResponse(EnumResponseStatusCode.SUCCESS);
            return new ResponseEntity<>(response, HttpStatus.OK);

        } else {
            MessageResponse response = new MessageResponse(EnumResponseStatusCode.TIME_INVALID);
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);

        }

    }


    public ResponseEntity<?> orderFood(OrderFoodReq orderFoodReq, String email) {
        tbl_Customer customer=customerRepo.findCustomerByEmail(email);
        tbl_Bill bill = billRepo.findByBookingId(orderFoodReq.getBookingId());
        if (bill == null) {
            MessageResponse response = new MessageResponse(EnumResponseStatusCode.BILL_NOT_EXIST);
            return new ResponseEntity<>(response, HttpStatus.OK);
        }
        for (FoodReq data : orderFoodReq.getFoodList()) {
            tbl_BillInfo billInfo = new tbl_BillInfo(bill.getBillId(), data.getQuantity(), data.getFoodId());
            billInfoRepo.save(billInfo);
        }
        LOGGER.warn("Booking table success by " + customer.getEmail() + "\n" + orderFoodReq, BookingService.class);
        MessageResponse response = new MessageResponse(EnumResponseStatusCode.ADD_FOOD_SUCCESS);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }


    public ResponseEntity<?> pay(PayReq payReq, HttpServletRequest req) {
        Optional<tbl_Customer> customer = authTokenFilter.whoami(req);
        tbl_Sale sale = saleRepo.findBySaleId(payReq.getSaleId());

        Long totalPoint = customer.get().getTotalMoney();
        tbl_Points points = pointsRepo.findTopByPriceGreaterThanEqualOrderByPriceAscCreatedDesc(totalPoint);
        tbl_BankAccount bankAcc = accountRepo.findByAccountNo(payReq.getAccountNo());
        tbl_Bill bill = billRepo.findByBookingId(payReq.getBookingId());
        float totalMoneyBill = bookingRepository.sumTotalMoneyBill();
        tbl_Booking booking = bookingRepository.findByBookingId(payReq.getBookingId());
        tbl_Deposit deposit = depositRepo.findByDepositId(booking.getDepositId());
        if (bill == null || booking == null || deposit == null || points == null) {
            MessageResponse response = new MessageResponse(EnumResponseStatusCode.PAY_FAILED);
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }

        float moneyPay = totalMoneyBill - deposit.getDeposit() - payReq.getDiscount();
        if (moneyPay > 0) {
            if (payReq.getDiscount() != 0) {
                customer.get().setTotalMoney(0L);
                bill.setDiscount(payReq.getDiscount());
                customerRepo.save(customer.get());
                billRepo.save(bill);
            }
            bankAcc.setBalance((long) (bankAcc.getBalance() - moneyPay));
            tbl_BankAccount acc = accountRepo.getBankAcc("sunshine87lethanhnghi@gmail.com");
            acc.setBalance((long) (acc.getBalance() + moneyPay));
            accountRepo.save(bankAcc);
            accountRepo.save(acc);
        } else {
            float pay_ = Math.abs(totalMoneyBill - deposit.getDeposit());
            if (payReq.getDiscount() != 0) {
                customer.get().setTotalMoney((long) (customer.get().getTotalMoney() - pay_));
                bill.setDiscount((long) pay_);
                customerRepo.save(customer.get());
                billRepo.save(bill);
            }
            tbl_BankAccount acc = accountRepo.getBankAcc("sunshine87lethanhnghi@gmail.com");
            acc.setBalance((long) (acc.getBalance() + pay_));
            accountRepo.save(acc);
        }
        bill.setBillStatus(1);
        bill.setPayDate(new Date());
        bill.setPointId(points.getPointId());
        billRepo.save(bill);
        booking.setBookingStatus(1);
        booking.setSaleId(sale.getSaleId());
        bookingRepository.save(booking);
        customer.get().setTotalMoney((long) (customer.get().getTotalMoney() + points.getPointPercent() * totalMoneyBill));
        customerRepo.save(customer.get());

        LOGGER.warn("Pay bill success by " + customer.get().getEmail() + "\n" + payReq, BookingService.class);
        MessageResponse response = new MessageResponse(EnumResponseStatusCode.PAY_SUCCESS);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }


    public ResponseEntity<?> cancelBooking(CancelBookingReq cancelBookingReq, HttpServletRequest req) {
        Optional<tbl_Customer> customer = authTokenFilter.whoami(req);
        tbl_Booking booking=bookingRepository.findByBookingId(cancelBookingReq.getBookingId());
        tbl_Deposit deposit=depositRepo.findByDepositId(booking.getDepositId());
        tbl_Bill bill=billRepo.findByBookingId(cancelBookingReq.getBookingId());
        List<tbl_BillInfo> billInfoList=billInfoRepo.findAllByBillId(bill.getBillId());
        if(deposit==null||billInfoList==null){
            MessageResponse response = new MessageResponse(EnumResponseStatusCode.BAD_REQUEST);
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
        else if(billInfoList.size()==0){
            bill.setBillStatus(2);
            booking.setBookingStatus(2);
            customer.get().setTotalMoney((long) (customer.get().getTotalMoney()+deposit.getDeposit()*0.3));
            tbl_BankAccount acc = accountRepo.getBankAcc("sunshine87lethanhnghi@gmail.com");
            acc.setBalance((long) (acc.getBalance() -deposit.getDeposit()*0.3));
            accountRepo.save(acc);
            customerRepo.save(customer.get());
            bookingRepository.save(booking);
            billRepo.save(bill);
            LOGGER.warn("Cancel booking success by " + customer.get().getEmail() + "\n" + cancelBookingReq, BookingService.class);
            MessageResponse response = new MessageResponse(EnumResponseStatusCode.CANCEL_BOOKING_SUCCESS);
            return new ResponseEntity<>(response, HttpStatus.OK);
        }
        else {
            MessageResponse response = new MessageResponse(EnumResponseStatusCode.EMPLOYEE_CANCEL_BOOKING_SUCCESS);
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
        /*
         * TODO
         *  xử lý phần nhân viên xác nhận bằng thread
         * */
    }


    public ResponseEntity<?> cancelBookingAdmin(CancelBookingReq cancelBookingReq, String email) {
        tbl_Booking booking=bookingRepository.findByBookingId(cancelBookingReq.getBookingId());
        tbl_Deposit deposit=depositRepo.findByDepositId(booking.getDepositId());
        tbl_Bill bill=billRepo.findByBookingId(cancelBookingReq.getBookingId());
        List<tbl_BillInfo> billInfoList=billInfoRepo.findAllByBillId(bill.getBillId());
        tbl_Customer customer=customerRepo.findCustomerByEmail(booking.getEmail());
        if(deposit==null||billInfoList==null){
            MessageResponse response = new MessageResponse(EnumResponseStatusCode.BAD_REQUEST);
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
        else {
            bill.setBillStatus(2);
            booking.setBookingStatus(2);
            customer.setTotalMoney((long) (customer.getTotalMoney()+deposit.getDeposit()*0.3));
            tbl_BankAccount acc = accountRepo.getBankAcc("sunshine87lethanhnghi@gmail.com");
            acc.setBalance((long) (acc.getBalance() -deposit.getDeposit()*0.3));
            accountRepo.save(acc);
            customerRepo.save(customer);
            bookingRepository.save(booking);
            billRepo.save(bill);
            LOGGER.warn("Cancel booking success by " + customer.getEmail() + "\n" + cancelBookingReq, BookingService.class);
            MessageResponse response = new MessageResponse(EnumResponseStatusCode.CANCEL_BOOKING_SUCCESS);
            return new ResponseEntity<>(response, HttpStatus.OK);
        }

    }
}
