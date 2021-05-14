package com.nuce.duantp.sunshine.service;

import com.nuce.duantp.sunshine.dto.response.MoneyPayRes;
import com.nuce.duantp.sunshine.dto.response.PromotionsRes;
import com.nuce.duantp.sunshine.dto.response.SaleRes;
import com.nuce.duantp.sunshine.model.*;
import com.nuce.duantp.sunshine.repository.*;
import com.nuce.duantp.sunshine.security.jwt.AuthTokenFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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
    @Autowired
    SaleRepo saleRepo;
    @Autowired
    NewsRepo newsRepo;
    private Logger LOGGER = LoggerFactory.getLogger(SunShineService.class);

    public List<String> getListCustomer() {
        List<String> list = new ArrayList<>();
        List<tbl_Customer> customerList = customerRepo.findAllByRole("USERS");
        for (tbl_Customer customer : customerList) {
            list.add(customer.getEmail());
        }
        return list;
    }

    //tổng tiền =(tổng món ăn - tiền cọc - điểm)*sale
    public float moneyPay(String bookingId) {
      try{
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
          money=money-deposit.getDeposit();
        tbl_Sale sale = saleRepo.findBySaleId(booking.getSaleId());
          if (sale != null) {
              money = money * sale.getPercentDiscount();
          }
        return money;
      }catch (Exception e){
        return 0L;
      }

    }

    public List<MoneyPayRes> allMoneyPayCustomer(List<String> customerList) {
        List<MoneyPayRes> moneyPayResList = new ArrayList<>();
        for (String str : customerList) {
            float money = 0L;
            List<tbl_Booking> booking = bookingRepo.findAllByEmailAndBookingStatus(str, 1);
            for (tbl_Booking booking1 : booking) {
                float money1 = moneyPay(booking1.getBookingId());
                money += money1;
            }
            MoneyPayRes moneyPayRes = new MoneyPayRes(str, money);
            moneyPayResList.add(moneyPayRes);
        }
        return moneyPayResList;
    }

    public void updateBeneficiary() {
        List<String> customerList = getListCustomer();
        List<MoneyPayRes> list = allMoneyPayCustomer(customerList);
        for (MoneyPayRes data : list) {
            tbl_Beneficiary beneficiary = beneficiaryRepo.findTop1ByTotalBillLessThanEqualOrderByTotalBillDesc((long) data.getMoneyPay());
            Optional<tbl_Customer> customer = customerRepo.findByEmail(data.getEmail());
            if (beneficiary != null && !customer.get().getBeneficiary().equals(beneficiary.getBeneficiaryName())) {
                LOGGER.warn("Job update Beneficiary for email " + customer.get().getEmail() + " from " + customer.get().getBeneficiary() + " to " + beneficiary.getBeneficiaryName() + " with totalBill = " + data.getMoneyPay(), SunShineService.class);
                customer.get().setBeneficiary(beneficiary.getBeneficiaryName());
                customerRepo.save(customer.get());
            }
        }
    }

    public List<SaleRes> getAllSale() {
        List<tbl_Sale> saleList = saleRepo.findBySaleStatus(1);
        List<SaleRes> saleResList = new ArrayList<>();
        for (tbl_Sale sale : saleList) {
            SaleRes saleRes = new SaleRes(sale);
            saleResList.add(saleRes);
        }
        return saleResList;
    }

    public List<PromotionsRes> getAllPromotions() {
        List<tbl_News> promotionsList = newsRepo.findByNewsStatus(1);
        List<PromotionsRes> promotionsResList = new ArrayList<>();
        for (tbl_News data : promotionsList) {
            PromotionsRes promotionsRes = new PromotionsRes(data);
            promotionsResList.add(promotionsRes);
        }
        return promotionsResList;
    }
}
