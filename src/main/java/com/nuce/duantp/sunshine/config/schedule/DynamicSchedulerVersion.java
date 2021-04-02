package com.nuce.duantp.sunshine.config.schedule;



import com.nuce.duantp.sunshine.config.schedule.model.ConfigSchedule;
import com.nuce.duantp.sunshine.security.services.ConfigScheduleService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.stereotype.Component;


import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

@Component
@Slf4j
@EnableScheduling
public class DynamicSchedulerVersion implements SchedulingConfigurer, ApplicationContextAware {
    private ApplicationContext applicationContext;


    private ConfigScheduleService configScheduleService;

    public DynamicSchedulerVersion(ConfigScheduleService configScheduleService) {
        this.configScheduleService = configScheduleService;
    }

    List<ConfigSchedule> configSchedules = new ArrayList<>();
    ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();

    @PostConstruct
    public void initDatabase() {
        configSchedules = configScheduleService.findAll();
        if (configSchedules.size() != 0) {
            configSchedules.forEach(configSchedule -> System.out.println(configSchedule.getCron()));
        }
    }

    @Override
    public void configureTasks(ScheduledTaskRegistrar taskRegistrar) {
        ThreadPoolTaskScheduler scheduler = new ThreadPoolTaskScheduler();
        scheduler.setThreadNamePrefix("ThreadPoolTaskScheduler");
        scheduler.setPoolSize(1);
        scheduler.initialize();
        taskRegistrar.setScheduler(scheduler);
        Map<String, Runnable> list = applicationContext.getBeansOfType(Runnable.class);
        for (ConfigSchedule cronPattern : configSchedules) {
            try {
                CronTrigger cronTrigger = new CronTrigger(cronPattern.getCron());
                Runnable runMethod = list.get(cronPattern.getBean());

                if (runMethod == null) {
                    throw new IllegalArgumentException(String.format("Scheduled bean '%s' does not exist", cronPattern.getBean()));
                } else {
                    taskRegistrar.addTriggerTask(runMethod, cronTrigger);
                }
            } catch (Exception e) {
                log.info("Error :{}", e);
            }

        }

    }


    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    public ApplicationContext getApplicationContext() {
        return this.applicationContext;
    }

}