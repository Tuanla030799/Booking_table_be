package com.nuce.duantp.sunshine.service;


import com.nuce.duantp.sunshine.config.TimeUtils;
import com.nuce.duantp.sunshine.config.format.FormatMoney;
import com.nuce.duantp.sunshine.config.format.Validate;
import com.nuce.duantp.sunshine.dto.request.*;
import com.nuce.duantp.sunshine.dto.response.BookingHistoryDetail;
import com.nuce.duantp.sunshine.dto.response.ListFoodInBooking;
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
import java.text.DateFormat;
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
//        TODO: enable
//        if(date.compareTo(new Date())<0){
//            MessageResponse response = new MessageResponse(EnumResponseStatusCode.MIN_TIME);
//            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
//        }
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
//        TODO:enable
        if(date.compareTo(new Date())<0){
            MessageResponse response = new MessageResponse(EnumResponseStatusCode.MIN_TIME);
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
        if (date.getHours() >= 8 && date.getHours() < 23) {
            tbl_Booking booking = new tbl_Booking
                    (bookingId, customer.get().getEmail(), date, bookingReq.getTotalSeats(), deposit.getDepositId(),
                            1, tableName,bookingReq.getNote());
            List<tbl_Booking> data92 = bookingRepository.findAll();

            bookingRepository.save(booking);
            Long money=customer.get().getTotalMoney();
            customer.get().setTotalMoney(money-deposit.getDeposit());
            tbl_Customer admin=customerRepo.findCustomerByEmail("sunshine87lethanhnghi@gmail.com");
            admin.setTotalMoney(admin.getTotalMoney()+deposit.getDeposit());
            tbl_Bill bill = new tbl_Bill(bookingId,1L, bookingId);
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
            tbl_BillInfo billInfo = billInfoRepo.findByBillIdAndFoodId(bill.getBillId(), data.getFoodId());
            if (billInfo == null) {
                tbl_BillInfo billInfo1 = new tbl_BillInfo(bill.getBillId(), data.getQuantity(), data.getFoodId());
                billInfoRepo.save(billInfo1);
            } else {
                billInfo.setQuantity(data.getQuantity());
                billInfoRepo.save(billInfo);
            }
        }

        LOGGER.warn("Booking table success by " + customer.getEmail() + "\n" + orderFoodReq, BookingService.class);
        MessageResponse response = new MessageResponse(EnumResponseStatusCode.ADD_FOOD_SUCCESS);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
    public ResponseEntity<?> getTotalMoneyPay(PayReq payReq, HttpServletRequest req){
        BookingHistoryDetail bookingHistoryDetail =getBillPay(payReq.getBookingId());

        PayDetailResponse payDetailResponse=new PayDetailResponse(bookingHistoryDetail);
        return new ResponseEntity<>(payDetailResponse, HttpStatus.OK);
    }
    public BookingHistoryDetail getBillPay(String bookingId)  {
            tbl_Bill bill = billRepo.findByBookingId(bookingId);
            List<ListFoodInBooking> listFoodInBookings =new ArrayList<>();
            float totalMoneyFood = 0L; //lấy ra tổng số tiền cho đặt món
            int stt=1;
            List<tbl_BillInfo> billInfoList=billInfoRepo.findAllByBillId(bill.getBillId());
            for(tbl_BillInfo data: billInfoList){
                tbl_Food food=foodRepo.findByFoodId(data.getFoodId());
                ListFoodInBooking listFoodInBooking =new ListFoodInBooking(stt, food.getFoodName(),
                        FormatMoney.formatMoney(String.valueOf(food.getFoodPrice())),
                        FormatMoney.formatMoney(String.valueOf(food.getFoodPrice()* data.getQuantity())),
                        data.getQuantity());
                listFoodInBookings.add(listFoodInBooking);
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

            float totalMoney=totalMoneyFood*(1-percentDiscount)-deposit;
            String status= Validate.convertStatusBooking(booking.getBookingStatus());
            String payDate="Chưa thanh toán.";
            if(bill.getPayDate()!=null){
                DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss aa");
                payDate = dateFormat.format(bill.getPayDate());
            }
            int totalSet=booking.getTotalSeats();
            String tableName=booking.getTableName();
            if(listFoodInBookings.size()==0){
                percentDiscount=0L;
            }
            BookingHistoryDetail bookingHistoryDetail =new BookingHistoryDetail(bookingId,  booking.getBookingTime(), listFoodInBookings,deposit,
                    saleId, totalMoneyFood,totalMoney,status,payDate,tableName,totalSet,percentDiscount);
        System.out.println(bookingId);
            return bookingHistoryDetail;
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
//        TODO: kiểm tra thời gian thanh toán có trước thời gina đặt
//        if(booking.getBookingTime().after(new Date())){
//            MessageResponse response = new MessageResponse(EnumResponseStatusCode.TIME_PAY_FALSE);
//            return new ResponseEntity<>(response, HttpStatus.OK);
//        }
        if(booking.getBookingStatus()==1){
            MessageResponse response = new MessageResponse(EnumResponseStatusCode.PAID);
            return new ResponseEntity<>(response, HttpStatus.OK);
        }
        tbl_Customer customer=customerRepo.findCustomerByEmail(booking.getEmail());
        tbl_Customer admin=customerRepo.findCustomerByEmail("sunshine87lethanhnghi@gmail.com");
        tbl_Bill bill = billRepo.findByBookingId(payReq.getBookingId());
        BookingHistoryDetail bookingHistoryDetail =getBillPay(payReq.getBookingId());
        float moneyPay = bookingHistoryDetail.getTotalMoney();
        Long money=customer.getTotalMoney();
        tbl_Points point= pointsRepo.findTopByPriceGreaterThanEqualOrderByPriceAscCreatedDesc((long) moneyPay);
        float pointPercent=0L;
        if(point!=null) pointPercent=point.getPointPercent();
        customer.setTotalMoney((long) (money-moneyPay+pointPercent));

        money=admin.getTotalMoney();
        admin.setTotalMoney((long) (money+moneyPay));
        booking.setBookingStatus(2);
        booking.setSaleId(bookingHistoryDetail.getSaleId());

//        bill.setBillStatus(2);
        bill.setPayDate(new Date());
        bill.setPointId(point.getPointId());

        billRepo.save(bill);
        bookingRepository.save(booking);

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
//            bill.setBillStatus(3);
            booking.setBookingStatus(3);
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
            booking.setBookingStatus(0);
            bookingRepository.save(booking);
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
//            bill.setBillStatus(2);
            booking.setBookingStatus(3);
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
