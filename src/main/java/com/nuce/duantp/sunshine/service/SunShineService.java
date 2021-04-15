package com.nuce.duantp.sunshine.service;

import com.nuce.duantp.sunshine.dto.response.MoneyPayRes;
import com.nuce.duantp.sunshine.model.*;
import com.nuce.duantp.sunshine.repository.*;
import com.nuce.duantp.sunshine.security.jwt.AuthTokenFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.support.ManagedList;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class SunShineService {
    @Autowired
    private BookingRepository bookingRepo;
    @Autowired
    private BillInfoRepo billInfoRepo;
    @Autowired
    private BeneficiaryRepo beneficiaryRepo;
    @Autowired
    private AuthTokenFilter authTokenFilter;
    @Autowired
    private CustomerRepo customerRepo;
    @Autowired
    BillRepo billRepo;
    @Autowired
    FoodRepo foodRepo;
    @Autowired
    DepositRepo depositRepo;

    public List<String> getListCustomer() {
        List<String> list = new ArrayList<>();
        List<tbl_Customer> customerList = customerRepo.findAllByRole("USERS");
        for (tbl_Customer customer : customerList) {
            list.add(customer.getEmail());
        }
        return list;
    }

    public float moneyPay(String bookingId) {
        tbl_Booking booking = bookingRepo.findByBookingId(bookingId);
        tbl_Bill bill = billRepo.findByBookingId(bookingId);
        List<tbl_BillInfo> billInfoList = billInfoRepo.findAllByBillId(bill.getBillId());
        float money = 0L;
        for (tbl_BillInfo billInfo : billInfoList) {
            tbl_Food food = foodRepo.findByFoodId(billInfo.getFoodId());
            Long moneyFood = food.getFoodPrice() * billInfo.getQuantity();
            money += moneyFood;
        }
        tbl_Deposit deposit = depositRepo.findByDepositId(booking.getDepositId());
        money = (money + deposit.getDeposit()) * booking.getPercentDiscount();
        return money;
    }

    public List<MoneyPayRes> allMoneyPayCustomer(String str) {
        List<MoneyPayRes> moneyPayResList = new ArrayList<>();
        List<String> customerList = getListCustomer();
      //  for (String str : customerList) {
            float money = 0L;
            List<tbl_Booking> booking = bookingRepo.findAllByEmailAndBookingStatus(str, 1);
            for (tbl_Booking booking1 : booking) {
                float money1 = moneyPay(booking1.getBookingId());
                money += money1;
            }
            MoneyPayRes moneyPayRes = new MoneyPayRes(str, money);
            moneyPayResList.add(moneyPayRes);
      //  }
        return moneyPayResList;
    }

    public void updateBeneficiary(String str) {
        List<MoneyPayRes> list = allMoneyPayCustomer(str);
        tbl_Beneficiary tbl_beneficiary = beneficiaryRepo.findTop1ByTotalBillLessThanEqualOrderByTotalBillDesc(Long.valueOf(str));
        System.out.println(tbl_beneficiary);
    }
}
