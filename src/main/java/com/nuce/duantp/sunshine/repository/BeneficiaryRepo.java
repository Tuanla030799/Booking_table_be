package com.nuce.duantp.sunshine.repository;

import com.nuce.duantp.sunshine.dto.model.tbl_Beneficiary;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BeneficiaryRepo extends CrudRepository<tbl_Beneficiary, Long> {
        tbl_Beneficiary findTop1ByTotalBillLessThanEqualOrderByTotalBillDesc(Long totalBill);
}

