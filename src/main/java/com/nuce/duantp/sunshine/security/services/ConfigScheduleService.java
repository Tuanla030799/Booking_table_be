package com.nuce.duantp.sunshine.security.services;


import com.nuce.duantp.sunshine.config.schedule.model.ConfigSchedule;

import java.util.List;

public interface ConfigScheduleService {
    List<ConfigSchedule> findAll();

    ConfigSchedule findByCode(String code);

    ConfigSchedule findByName(String name);

    ConfigSchedule update(ConfigSchedule configSchedule);

    void delete(ConfigSchedule configSchedule);

    ConfigSchedule create(ConfigSchedule configSchedule);
}
