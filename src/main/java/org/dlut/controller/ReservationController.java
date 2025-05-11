package org.dlut.controller;


import jakarta.annotation.Resource;
import org.dlut.schedule.ReserveTomorrowSchedule;
import org.dlut.tasks.LockCurrentSeat;
import org.dlut.tasks.Reserve4Tomorrow;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/dlut")
public class ReservationController {

    @Resource
    private ReserveTomorrowSchedule reserve4TomorrowSchedule;

    @GetMapping("reservation")
    public String reservationTomorrow(){
        reserve4TomorrowSchedule.reserve4TomorrowSchedule();
        return "true";
    }
}
