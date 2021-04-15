package com.nuce.duantp.sunshine.repository;

import com.nuce.duantp.sunshine.model.tbl_Food;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FoodRepo extends CrudRepository<tbl_Food, Long> {
       tbl_Food findByFoodId(Long foodId);
}

