package com.nuce.duantp.sunshine.security.services.impl;

import com.nuce.duantp.sunshine.config.schedule.model.ConfigSchedule;
import com.nuce.duantp.sunshine.repository.ConfigScheduleRepository;
import com.nuce.duantp.sunshine.security.services.ConfigScheduleService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;



import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class ConfigScheduleDaoServiceImpl implements ConfigScheduleService {
    private ConfigScheduleRepository repository;

    public List<ConfigSchedule> findAll() {
        List<ConfigSchedule> list = repository.findAll();
        if (list == null) {
            return new ArrayList<>();
        } else {
            return list;
        }
    }

    @Override
    public ConfigSchedule findByCode(String code) {
        ConfigSchedule configSchedule = repository.findByCode(code);
        return configSchedule;
    }

    @Override
    public ConfigSchedule findByName(String name) {
        ConfigSchedule configSchedule = repository.findByName(name);
        return configSchedule;
    }

    @Override
    public ConfigSchedule update(ConfigSchedule configSchedule) {
        return repository.save(configSchedule);
    }

    @Override
    public void delete(ConfigSchedule configSchedule) {
        repository.delete(configSchedule);
    }

    @Override
    public ConfigSchedule create(ConfigSchedule configSchedule) {
        return repository.save(configSchedule);
    }

}
