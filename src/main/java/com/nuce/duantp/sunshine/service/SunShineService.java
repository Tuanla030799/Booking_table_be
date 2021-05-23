package com.nuce.duantp.sunshine.service;

import com.nuce.duantp.sunshine.dto.response.*;
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
//    @Autowired
//    private ImageRepo imageRepo;

    public PageHomeRes pageHome() {
        PageHomeRes pageHomeRes = new PageHomeRes();
        List<tbl_Food> foodList = foodRepo.findAllByFoodStatus(1);
        List<FoodHomeRes> foodHomeList = new ArrayList<>();
        int stt=1;
        for (tbl_Food data : foodList) {
//            Image image = imageRepo.findByName(data.getFoodImage());
            FoodHomeRes foodHomeRes = new FoodHomeRes(data,stt);
            foodHomeList.add(foodHomeRes);
            stt++;
        }

        List<tbl_Sale> saleList = saleRepo.findBySaleStatus(1);
        List<SaleHomeRes> saleHomeRes = new ArrayList<>();
        stt=1;
        for (tbl_Sale data : saleList) {
//            Image image = imageRepo.findByName(data.getSaleImage());
            SaleHomeRes req = new SaleHomeRes(data,stt);
            saleHomeRes.add(req);
            stt++;
        }

        List<tbl_News> newsList = newsRepo.findByNewsStatus(1);
        List<NewsHomeRes> newsHomeRes = new ArrayList<>();
        stt=1;
        for (tbl_News data : newsList) {
//            Image image = imageRepo.findByName(data.getNewsImage());
            NewsHomeRes req = new NewsHomeRes(data,stt);
            newsHomeRes.add(req);
            stt++;
        }
        pageHomeRes.setFoodHomeRes(foodHomeList);
        pageHomeRes.setNewsHomeRes(newsHomeRes);
        pageHomeRes.setSaleHomeRes(saleHomeRes);
        return pageHomeRes;
    }


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
        try {
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
            money = money - deposit.getDeposit();
            tbl_Sale sale = saleRepo.findBySaleId(booking.getSaleId());
            if (sale != null) {
                money = money * sale.getPercentDiscount();
            }
            return money;
        } catch (Exception e) {
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

//    public List<NewsRes> getAllNews() {
//        List<tbl_News> newsList = newsRepo.findByNewsStatus(1);
//        List<NewsRes> newsResList = new ArrayList<>();
//        for (tbl_News data : newsList) {
//            Image image = imageRepo.findByName(data.getNewsImage());
//            NewsRes newsRes = new NewsRes(data, image.getUrl());
//            newsResList.add(newsRes);
//        }
//        return newsResList;
//    }

    public List<FoodHomeRes> getListFood() {
        List<FoodHomeRes> list = new ArrayList<>();
        List<tbl_Food> foodList = foodRepo.findAllByFoodStatus(1);
        int stt=1;
        for (tbl_Food data : foodList) {
//            Image image = imageRepo.findByName(data.getFoodImage());
            FoodHomeRes foodHomeRes = new FoodHomeRes(data,stt);
            list.add(foodHomeRes);
            stt++;
        }
        return list;
    }
}
