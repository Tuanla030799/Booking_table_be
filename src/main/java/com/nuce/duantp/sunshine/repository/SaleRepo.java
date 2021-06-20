package com.nuce.duantp.sunshine.repository;

import com.nuce.duantp.sunshine.dto.model.tbl_Sale;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SaleRepo extends CrudRepository<tbl_Sale, Long> {
    tbl_Sale findBySaleId(Long saleId);
    @Query("select u from tbl_Sale u")
    List<tbl_Sale> getAllSale();
    List<tbl_Sale> findBySaleStatus(int status);
    List<tbl_Sale>findBySaleStatusAndBeneficiary(int status,String beneficiary);
    tbl_Sale findTopByBeneficiaryAndSaleStatusAndTotalBillLessThanEqualOrderByPercentDiscountDesc(String beneficiary,
                                                                                                  int status,
                                                                                                  float totalBill);
}

