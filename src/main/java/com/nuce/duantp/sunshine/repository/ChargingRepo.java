package com.nuce.duantp.sunshine.repository;

import com.nuce.duantp.sunshine.dto.model.Charging;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ChargingRepo extends CrudRepository<Charging, Long> {
    Charging findByCode(String code);
    Optional<Charging> findById(Long id);
}

