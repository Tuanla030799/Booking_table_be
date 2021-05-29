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
//    private final AccountRepo accountRepo;
    //    @Autowired
    private final DepositRepo depositRepo;
    //    @Autowired
    private final PointsRepo pointsRepo;
    //    @Autowired
    private final BillInfoRepo billInfoRepo;
    private final CustomerRepo customerRepo;
    private final FoodRepo foodRepo;
    private Logger LOGGER = LoggerFactory.getLogger(BookingService.class);

    public ResponseEntity<?> bookingTable(BookingReq bookingReq, HttpServletRequest req) {
        Optional<tbl_Customer> customer = authTokenFilter.whoami(req);
        String tableName = null;
        List<tbl_Table> tableList = tableRepo.findBySeatGreaterThanEqualOrderBySeatAsc(bookingReq.getTotalSeats());
        for (tbl_Table table : tableList) {
            Date date1 = TimeUtils.minusDate(Timestamp.valueOf(bookingReq.getBookingTime()), -7, "HOUR");
            Date date2 = TimeUtils.minusDate(Timestamp.valueOf(bookingReq.getBookingTime()), 7, "HOUR");
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
            messageResponse.setStatusCode(EnumResponseStatusCode.valueOf(responseStatusCode.getResponseStatusCode()));
            return new ResponseEntity<>(messageResponse, HttpStatus.OK);
        }
//        tbl_BankAccount accNo = accountRepo.findByAccountNo(bookingReq.getAccountNo());

        tbl_Deposit deposit = depositRepo.findTopByTotalPersonsLessThanEqualOrderByTotalPersonsAscCreatedDesc(bookingReq.getTotalSeats());
        if (deposit == null) {
            MessageResponse response = new MessageResponse(EnumResponseStatusCode.BAD_REQUEST);
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
        if (customer.get().getTotalMoney()<deposit.getDeposit()) {
            MessageResponse response = new MessageResponse(EnumResponseStatusCode.PAY_FAILED);
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
            tbl_Booking booking = new tbl_Booking
                    (bookingId, customer.get().getEmail(), date, bookingReq.getTotalSeats(), deposit.getDepositId(), 0, tableName);
            List<tbl_Booking> data92 = bookingRepository.findAll();

            bookingRepository.save(booking);
//            tbl_BankAccount acc1 = accountRepo.findByAccountNo(accNo.getAccountNo());
//            acc1.setBalance(accNo.getBalance() - deposit.getDeposit());
//            tbl_BankAccount acc2 = accountRepo.findByAccountNo("8686868686868");
//            acc2.setBalance(acc2.getBalance() + deposit.getDeposit());
            Long money=customer.get().getTotalMoney();
            customer.get().setTotalMoney(money-deposit.getDeposit());
            tbl_Customer admin=customerRepo.findCustomerByEmail("sunshine87lethanhnghi@gmail.com");
            money=admin.getTotalMoney()+deposit.getDeposit();

//            bookingRepository.save(booking);
            tbl_Bill bill = new tbl_Bill(bookingId,1L, bookingId,  0);
            billRepo.save(bill);
//            List<tbl_Booking> data2 = bookingRepository.findAll();
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

//        Long totalPoint = customer.get().getTotalMoney();
//        tbl_Points points = pointsRepo.findTopByPriceGreaterThanEqualOrderByPriceAscCreatedDesc(totalPoint);
        tbl_Customer admin=customerRepo.findCustomerByEmail("sunshine87lethanhnghi@gmail.com");
//        tbl_BankAccount bankAcc = accountRepo.findByAccountNo(payReq.getAccountNo());
        tbl_Bill bill = billRepo.findByBookingId(payReq.getBookingId());
        float totalMoneyBill = 0L; //lấy ra tổng số tiền cho đặt món
        List<tbl_BillInfo> billInfoList=billInfoRepo.findAllByBillId(bill.getBillId());
        for(tbl_BillInfo data: billInfoList){
            tbl_Food food=foodRepo.findByFoodId(data.getFoodId());
            totalMoneyBill+=food.getFoodPrice()*data.getQuantity();
        }

        tbl_Booking booking = bookingRepository.findByBookingId(payReq.getBookingId());
        tbl_Deposit deposit = depositRepo.findByDepositId(booking.getDepositId());
        if (bill == null || booking == null || deposit == null ) {
            MessageResponse response = new MessageResponse(EnumResponseStatusCode.NULL_POINTER);
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }

        float percentDiscount=1L;
        tbl_Sale sales = saleRepo.findBySaleId(payReq.getSaleId());
        if(sales!=null){
            percentDiscount=sales.getPercentDiscount();
        }
        float moneyPay = totalMoneyBill*percentDiscount- deposit.getDeposit();
        Long money=customer.get().getTotalMoney();
        customer.get().setTotalMoney((long) (money-moneyPay));
        tbl_Points point= pointsRepo.findTopByPriceGreaterThanEqualOrderByPriceAscCreatedDesc((long) moneyPay);
        money=admin.getTotalMoney();
        admin.setTotalMoney((long) (money+moneyPay));

//        if (moneyPay > 0) {
////            if (payReq.getDiscount() != 0) {
////                customer.get().setTotalMoney(0L);
////                bill.setDiscount(payReq.getDiscount());
////                customerRepo.save(customer.get());
////                billRepo.save(bill);
////            }
//            Long money=customer.get().getTotalMoney();
//            customer.get().setTotalMoney((long) (money-moneyPay));
//
//            money=admin.getTotalMoney();
//            admin.setTotalMoney((long) (money+moneyPay));
////            customerRepo.save(customer.get());
////            customerRepo.save(admin);
////            bankAcc.setBalance((long) (bankAcc.getBalance() - moneyPay));
////            tbl_BankAccount acc = accountRepo.findByAccountNo("8686868686868");
////            acc.setBalance((long) (acc.getBalance() + moneyPay));
////            accountRepo.save(bankAcc);
////            accountRepo.save(acc);
//        } else {
//             moneyPay = Math.abs(moneyPay);
////            if (payReq.getDiscount() != 0) {
////                customer.get().setTotalMoney((long) (customer.get().getTotalMoney() - pay_));
////                bill.setDiscount((long) pay_);
////                customerRepo.save(customer.get());
////                billRepo.save(bill);
////            }
//            admin.setTotalMoney((long) (admin.getTotalMoney()-moneyPay));
//            tbl_BankAccount acc = accountRepo.findByAccountNo("8686868686868");
//            acc.setBalance((long) (acc.getBalance() + pay_));
//            accountRepo.save(acc);
//        }
        bill.setBillStatus(1);
        bill.setPayDate(new Date());
        bill.setPointId(point.getPointId());
        billRepo.save(bill);

        booking.setBookingStatus(1);
        bookingRepository.save(booking);

//        customer.get().setTotalMoney((long) (customer.get().getTotalMoney() + points.getPointPercent() * totalMoneyBill));
//        customerRepo.save(customer.get());
//        customerRepo.save(admin);
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
            MessageResponse response = new MessageResponse(EnumResponseStatusCode.NULL_POINTER);
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
        else if(billInfoList.size()==0){
            bill.setBillStatus(2);
            booking.setBookingStatus(2);
            customer.get().setTotalMoney((long) (customer.get().getTotalMoney()+deposit.getDeposit()*0.3));
            tbl_Customer admin=customerRepo.findCustomerByEmail("sunshine87lethanhnghi@gmail.com");
            admin.setTotalMoney((long) (admin.getTotalMoney()-deposit.getDeposit()*0.3));
//            tbl_BankAccount acc = accountRepo.findByAccountNo("8686868686868");
//            acc.setBalance((long) (acc.getBalance() -deposit.getDeposit()*0.3));
//            accountRepo.save(acc);
            customerRepo.save(admin);
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
            tbl_Customer admin=customerRepo.findCustomerByEmail("sunshine87lethanhnghi@gmail.com");
            admin.setTotalMoney((long) (admin.getTotalMoney()-deposit.getDeposit()*0.3));
            customerRepo.save(admin);
//            tbl_BankAccount acc = accountRepo.findByAccountNo("8686868686868");
//            acc.setBalance((long) (acc.getBalance() -deposit.getDeposit()*0.3));
//            accountRepo.save(acc);
            customerRepo.save(customer);
            bookingRepository.save(booking);
            billRepo.save(bill);
            LOGGER.warn("Cancel booking success by " + customer.getEmail() + "\n" + cancelBookingReq, BookingService.class);
            MessageResponse response = new MessageResponse(EnumResponseStatusCode.CANCEL_BOOKING_SUCCESS);
            return new ResponseEntity<>(response, HttpStatus.OK);
        }

    }
}
