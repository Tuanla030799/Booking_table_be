package com.nuce.duantp.sunshine.service;


import com.nuce.duantp.sunshine.config.TimeUtils;
import com.nuce.duantp.sunshine.config.format.FormatMoney;
import com.nuce.duantp.sunshine.config.format.Validate;
import com.nuce.duantp.sunshine.dto.request.*;
import com.nuce.duantp.sunshine.dto.response.BillPay;
import com.nuce.duantp.sunshine.dto.response.BillReport;
import com.nuce.duantp.sunshine.dto.response.MessageResponse;
import com.nuce.duantp.sunshine.dto.enums.EnumResponseStatusCode;
import com.nuce.duantp.sunshine.dto.model.*;
import com.nuce.duantp.sunshine.dto.response.PayDetailResponse;
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
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BookingService {
    private final BookingRepository bookingRepository;
    private final AuthTokenFilter authTokenFilter;
    private final TableRepo tableRepo;
    private final BillRepo billRepo;
    private final SaleRepo saleRepo;
    private final DepositRepo depositRepo;
    private final PointsRepo pointsRepo;
    private final BillInfoRepo billInfoRepo;
    private final CustomerRepo customerRepo;
    private final FoodRepo foodRepo;
    private Logger LOGGER = LoggerFactory.getLogger(BookingService.class);

    public ResponseEntity<?> getDeposit(BookingReq bookingReq, HttpServletRequest req) {
        Date date = new Date();
        String bookingId = String.valueOf(date.getTime());
        try {
            date = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").parse(bookingReq.getBookingTime());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        if(date.compareTo(new Date())<0){
            MessageResponse response = new MessageResponse(EnumResponseStatusCode.MIN_TIME);
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
        if(bookingReq.getTotalSeats()<=0){
            MessageResponse response = new MessageResponse(EnumResponseStatusCode.TOTAL_SEAT_FALSE);
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
        if (date.getHours() <8 || date.getHours() >=23){
            MessageResponse response = new MessageResponse(EnumResponseStatusCode.TIME_INVALID);
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
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
        if (tableName == null) {
            MessageResponse response = new MessageResponse(EnumResponseStatusCode.TABLE_OFF);
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
        tbl_Deposit deposit = depositRepo.findTopByTotalPersonsLessThanEqualOrderByTotalPersonsAscCreatedDesc(bookingReq.getTotalSeats());
        if (deposit == null) {
            MessageResponse response = new MessageResponse(EnumResponseStatusCode.NULL_POINTER);
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
        if (customer.get().getTotalMoney()<deposit.getDeposit()) {
            MessageResponse response = new MessageResponse(EnumResponseStatusCode.PAY_FAILED);
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
        MessageResponse response = new MessageResponse(EnumResponseStatusCode.SUCCESS,
                FormatMoney.formatMoney(String.valueOf(deposit.getDeposit())));
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

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
        if (tableName == null) {
            MessageResponse response = new MessageResponse(EnumResponseStatusCode.TABLE_OFF);
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }

        tbl_Deposit deposit = depositRepo.findTopByTotalPersonsLessThanEqualOrderByTotalPersonsAscCreatedDesc(bookingReq.getTotalSeats());
        if (deposit == null) {
            MessageResponse response = new MessageResponse(EnumResponseStatusCode.NULL_POINTER);
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
                    (bookingId, customer.get().getEmail(), date, bookingReq.getTotalSeats(), deposit.getDepositId(),
                            0, tableName,bookingReq.getNote());
            List<tbl_Booking> data92 = bookingRepository.findAll();

            bookingRepository.save(booking);
            Long money=customer.get().getTotalMoney();
            customer.get().setTotalMoney(money-deposit.getDeposit());
            tbl_Customer admin=customerRepo.findCustomerByEmail("sunshine87lethanhnghi@gmail.com");
            admin.setTotalMoney(admin.getTotalMoney()+deposit.getDeposit());
            tbl_Bill bill = new tbl_Bill(bookingId,1L, bookingId,  0);
            billRepo.save(bill);
            customerRepo.save(customer.get());
            customerRepo.save(admin);
            MessageResponse response = new MessageResponse(EnumResponseStatusCode.SUCCESS,bookingId);
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
    public ResponseEntity<?> getTotalMoneyPay(PayReq payReq, HttpServletRequest req){
        BillPay billPay=getBillPay(payReq.getBookingId());
        String saleTitle="Không được khuyến mãi";
        tbl_Sale sale=saleRepo.findBySaleId(billPay.getSaleId());
        if(sale!=null){
            saleTitle=sale.getSaleTitle();
        }
        PayDetailResponse payDetailResponse=new PayDetailResponse(billPay,saleTitle);
        return new ResponseEntity<>(payDetailResponse, HttpStatus.OK);
    }
    public BillPay getBillPay(String bookingId)  {
            tbl_Bill bill = billRepo.findByBookingId(bookingId);
            List<BillReport> billReports=new ArrayList<>();
            float totalMoneyFood = 0L; //lấy ra tổng số tiền cho đặt món
            int stt=1;
            List<tbl_BillInfo> billInfoList=billInfoRepo.findAllByBillId(bill.getBillId());
            for(tbl_BillInfo data: billInfoList){
                tbl_Food food=foodRepo.findByFoodId(data.getFoodId());
                BillReport billReport=new BillReport(stt, food.getFoodName(),
                        FormatMoney.formatMoney(String.valueOf(food.getFoodPrice())),
                        FormatMoney.formatMoney(String.valueOf(food.getFoodPrice()* data.getQuantity())),
                        data.getQuantity());
                billReports.add(billReport);
                stt++;
                totalMoneyFood+=food.getFoodPrice()*data.getQuantity();
            }
            tbl_Booking booking = bookingRepository.findByBookingId(bookingId);
            tbl_Deposit tbl_deposit = depositRepo.findByDepositId(booking.getDepositId());
            tbl_Customer customer=customerRepo.findCustomerByEmail(booking.getEmail());
            Long deposit = 0L;
            if (tbl_deposit != null ) {
                deposit=tbl_deposit.getDeposit();
            }
            float percentDiscount=1L;
            Long saleId=0L;
            tbl_Sale sales =
                    saleRepo.findTopByBeneficiaryAndSaleStatusAndTotalBillLessThanEqualOrderByPercentDiscountDesc(customer.getBeneficiary(),1,totalMoneyFood);
            if(sales!=null){
                percentDiscount=sales.getPercentDiscount();
                saleId=sales.getSaleId();
            }

            float totalMoney=totalMoneyFood*percentDiscount-deposit;
            String status= Validate.convertStatusBooking(booking.getBookingStatus());
//        System.out.println("\n1 "+bookingId);
//        System.out.println("\n2 "+customer.getFullName());
//        System.out.println("\n3 "+booking.getBookingTime());
//        System.out.println("\n4 "+billReports);
//        System.out.println("\n5 "+deposit);
//        System.out.println("\n6 "+sales.getSaleId());
//        System.out.println("\n7 "+totalMoneyFood);
//        System.out.println("\n8 "+totalMoney);


            BillPay billPay=new BillPay(bookingId, customer.getFullName(), booking.getBookingTime(),billReports,deposit,
                    saleId, totalMoneyFood,totalMoney,status);
        System.out.println(bookingId);
            return billPay;
    }

    public boolean checkWho(tbl_Customer customer,String bookingId){
        tbl_Customer customer1=customerRepo.findCustomerByEmail(customer.getEmail());
        tbl_Booking booking=bookingRepository.findByBookingId(bookingId);
        tbl_Customer customer2=customerRepo.findCustomerByEmail(booking.getEmail());
        if(customer1.getRole().equals("ADMIN")) return true;
        if(customer1==customer2) return true;
        return false;
    }

    public ResponseEntity<?> pay(PayReq payReq, HttpServletRequest req) {
        Optional<tbl_Customer> customer1 = authTokenFilter.whoami(req);

        if(!checkWho(customer1.get(), payReq.getBookingId())){
            MessageResponse response = new MessageResponse(EnumResponseStatusCode.TOKEN_DIE);
            return new ResponseEntity<>(response, HttpStatus.OK);
        }
        tbl_Booking booking=bookingRepository.findByBookingId(payReq.getBookingId());
        if(booking.getBookingTime().after(new Date())){
            MessageResponse response = new MessageResponse(EnumResponseStatusCode.TIME_PAY_FALSE);
            return new ResponseEntity<>(response, HttpStatus.OK);
        }
        if(booking.getBookingStatus()==1){
            MessageResponse response = new MessageResponse(EnumResponseStatusCode.PAID);
            return new ResponseEntity<>(response, HttpStatus.OK);
        }
        tbl_Customer customer=customerRepo.findCustomerByEmail(booking.getEmail());
        tbl_Customer admin=customerRepo.findCustomerByEmail("sunshine87lethanhnghi@gmail.com");
        tbl_Bill bill = billRepo.findByBookingId(payReq.getBookingId());
//        float totalMoneyBill = 0L; //lấy ra tổng số tiền cho đặt món
//        List<tbl_BillInfo> billInfoList=billInfoRepo.findAllByBillId(bill.getBillId());
//        for(tbl_BillInfo data: billInfoList){
//            tbl_Food food=foodRepo.findByFoodId(data.getFoodId());
//            totalMoneyBill+=food.getFoodPrice()*data.getQuantity();
//        }
//
//        tbl_Deposit deposit = depositRepo.findByDepositId(booking.getDepositId());
//        if (bill == null || booking == null || deposit == null ) {
//            MessageResponse response = new MessageResponse(EnumResponseStatusCode.NULL_POINTER);
//            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
//        }
//
//        float percentDiscount=1L;
//        tbl_Sale sales =
//                saleRepo.findTopByBeneficiaryAndSaleStatusAndTotalBillLessThanEqualOrderByPercentDiscountDesc(customer.get().getBeneficiary(),1,totalMoneyBill);
//        if(sales!=null){
//            percentDiscount=sales.getPercentDiscount();
//        }
        BillPay billPay=getBillPay(payReq.getBookingId());
        float moneyPay = billPay.getTotalMoney();
        Long money=customer.getTotalMoney();
        customer.setTotalMoney((long) (money-moneyPay));
        tbl_Points point= pointsRepo.findTopByPriceGreaterThanEqualOrderByPriceAscCreatedDesc((long) moneyPay);
        money=admin.getTotalMoney();
        admin.setTotalMoney((long) (money+moneyPay));
        booking.setBookingStatus(1);
        booking.setSaleId(billPay.getSaleId());
//        bookingRepository.save(booking);
        bill.setBillStatus(1);
        bill.setPayDate(new Date());
        bill.setPointId(point.getPointId());



//        billRepo.save(bill);
        LOGGER.warn("Pay bill success by " + customer.getEmail() + "\n" + payReq, BookingService.class);
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
            customerRepo.save(customer);
            bookingRepository.save(booking);
            billRepo.save(bill);
            LOGGER.warn("Cancel booking success by " + customer.getEmail() + "\n" + cancelBookingReq, BookingService.class);
            MessageResponse response = new MessageResponse(EnumResponseStatusCode.CANCEL_BOOKING_SUCCESS);
            return new ResponseEntity<>(response, HttpStatus.OK);
        }

    }
}
