package com.nuce.duantp.sunshine.repository;

import com.nuce.duantp.sunshine.dto.model.tbl_Points;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PointsRepo extends CrudRepository<tbl_Points, Long> {
       List<tbl_Points>  findAllByPrice(Long price);
       tbl_Points findByPointId(Long pointId);
       tbl_Points findTopByPriceGreaterThanEqualOrderByPriceAscCreatedDesc(Long price);
}

