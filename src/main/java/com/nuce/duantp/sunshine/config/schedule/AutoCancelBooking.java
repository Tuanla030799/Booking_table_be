package com.nuce.duantp.sunshine.config.schedule;

import com.nuce.duantp.sunshine.config.TimeUtils;
import com.nuce.duantp.sunshine.model.tbl_Booking;
import com.nuce.duantp.sunshine.repository.BookingRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

@Component
public class AutoCancelBooking implements Runnable {
    @Autowired
    BookingRepository bookingRepository;
    private Logger LOGGER = LoggerFactory.getLogger(AutoCancelBooking.class);
    @Override
    public void run() {
        List<tbl_Booking> list=bookingRepository.findByBookingStatusAndBookingTimeLessThan(0,new Date());
        for(tbl_Booking data:list){
            Date date = new Date();
            Date date1 = TimeUtils.minusDate(data.getBookingTime(), 15, "MINUTE");
            if(date.compareTo(date1)>0){
                bookingRepository.cancelBookingAdmin(data.getBookingId());
                LOGGER.warn("Job auto cancel booking with bookingId =  " + data.getBookingId(),
                        AutoCancelBooking.class);

            }
        }
    }
}
