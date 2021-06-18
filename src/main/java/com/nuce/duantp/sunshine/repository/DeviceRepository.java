package com.nuce.duantp.sunshine.repository;

import com.nuce.duantp.sunshine.dto.model.Device;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DeviceRepository extends CrudRepository<Device, Integer> {
    @Query(value = "select playerId from SrvDevice", nativeQuery = true)
    List<String> findPlayerId();
}
