package com.nuce.duantp.sunshine.service;

import com.nuce.duantp.sunshine.dto.response.*;
import com.nuce.duantp.sunshine.dto.model.*;
import com.nuce.duantp.sunshine.repository.*;
import com.nuce.duantp.sunshine.security.jwt.AuthTokenFilter;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class SunShineService {

    private final BookingRepository bookingRepo;
    private final BillInfoRepo billInfoRepo;
    private final BeneficiaryRepo beneficiaryRepo;
    private final AuthTokenFilter authTokenFilter;
    private final CustomerRepo customerRepo;
    private final BillRepo billRepo;
    private final  FoodRepo foodRepo;
    private final DepositRepo depositRepo;
    private final SaleRepo saleRepo;
    private final NewsRepo newsRepo;
    private Logger LOGGER = LoggerFactory.getLogger(SunShineService.class);

    public PageHomeRes pageHome() {
        PageHomeRes pageHomeRes = new PageHomeRes();
        List<tbl_Food> foodList = foodRepo.findAllByFoodStatus(1);
        List<FoodHomeRes> foodHomeList = new ArrayList<>();
        int stt=1;
        for (tbl_Food data : foodList) {
            FoodHomeRes foodHomeRes = new FoodHomeRes(data,stt);
            foodHomeList.add(foodHomeRes);
            stt++;
        }
        List<SaleHomeRes> saleHomeRes = getListSale();
        List<tbl_News> newsList = newsRepo.findByNewsStatus(1);
        List<NewsHomeRes> newsHomeRes = new ArrayList<>();
        stt=1;
        for (tbl_News data : newsList) {
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
//            tbl_Deposit deposit = depositRepo.findByDepositId(booking.getDepositId());
//            money = money - deposit.getDeposit();
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

    public List<FoodHomeRes> getListFood(String bookingId) {
        List<FoodHomeRes> list = new ArrayList<>();
        List<tbl_Food> foodList = foodRepo.findAllByFoodStatus(1);
        int stt=1;
        if (!bookingId.equals(" ")){
            tbl_Bill bill=billRepo.findByBookingId(bookingId);
            if (bill==null){
                return null;
            }
            List<tbl_BillInfo> billInfos=billInfoRepo.findAllByBillId(bill.getBillId());
            for (tbl_Food data : foodList) {
                int quantity=0;
                for (tbl_BillInfo res:billInfos){
                    if(data.getFoodId()==res.getFoodId()){
                        quantity=res.getQuantity();
                    }
                }
                FoodHomeRes foodHomeRes = new FoodHomeRes(data,stt,quantity);
                list.add(foodHomeRes);
                stt++;
            }
        }
        else{
            for (tbl_Food data : foodList) {
                FoodHomeRes foodHomeRes = new FoodHomeRes(data,stt);
                list.add(foodHomeRes);
                stt++;
            }
        }
        return list;
    }

    public List<SaleHomeRes> getListSale(){
        List<tbl_Sale> saleList = saleRepo.findBySaleStatus(1);
        List<SaleHomeRes> saleHomeRes = new ArrayList<>();
        int stt=1;
        for (tbl_Sale data : saleList) {
            SaleHomeRes req = new SaleHomeRes(data,stt);
            saleHomeRes.add(req);
            stt++;
        }
        return saleHomeRes;
    }
}
