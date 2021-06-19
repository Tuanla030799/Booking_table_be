package com.nuce.duantp.sunshine.config.schedule;

import com.nuce.duantp.sunshine.config.TimeUtils;
import com.nuce.duantp.sunshine.dto.request.CancelBookingReq;
import com.nuce.duantp.sunshine.dto.model.tbl_Booking;
import com.nuce.duantp.sunshine.repository.BookingRepository;
import com.nuce.duantp.sunshine.service.BookingService;
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
    @Autowired
    BookingService bookingService;
    private Logger LOGGER = LoggerFactory.getLogger(AutoCancelBooking.class);
    @Override
    public void run() {
        /*
        * TODO: kiểm tra xem bàn đã đã đến hay chưa
        *
        * */
        List<tbl_Booking> list=bookingRepository.findByBookingStatusAndBookingTimeLessThan(0,new Date());
        for(tbl_Booking data:list){
            Date date = new Date();
            Date date1 = TimeUtils.minusDate(data.getBookingTime(), 15, "MINUTE");
            if(date.compareTo(date1)>0){
                CancelBookingReq cancelBookingReq=new CancelBookingReq(data.getBookingId());
                bookingService.cancelBookingAdmin(cancelBookingReq,data.getEmail());
                LOGGER.warn("Job auto cancel booking with bookingId =  " + data.getBookingId(),
                        AutoCancelBooking.class);

            }
        }
    }
}
