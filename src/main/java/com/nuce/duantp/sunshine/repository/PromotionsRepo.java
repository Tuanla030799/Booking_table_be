package com.nuce.duantp.sunshine.repository;

import com.nuce.duantp.sunshine.model.tbl_Promotions;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PromotionsRepo extends CrudRepository<tbl_Promotions, Long> {
        List<tbl_Promotions> findByPromotionsStatus(int status);

        tbl_Promotions findByPromotionsId(Long id);
}

