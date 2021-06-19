package com.nuce.duantp.sunshine.repository;

import com.nuce.duantp.sunshine.dto.model.tbl_Food;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FoodRepo extends CrudRepository<tbl_Food, Long> {
       tbl_Food findByFoodId(Long foodId);

       List<tbl_Food> findAllByFoodStatus(int status);

       List<tbl_Food> findAllByFoodNameIgnoreCaseContaining(String name);
}

