package com.nuce.duantp.sunshine.repository;


import com.nuce.duantp.sunshine.config.schedule.model.ConfigSchedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ConfigScheduleRepository extends JpaRepository<ConfigSchedule, Long> {
    ConfigSchedule findByCode(String code);

    ConfigSchedule findByName(String name);
}
