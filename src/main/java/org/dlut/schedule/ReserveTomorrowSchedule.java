package org.dlut.schedule;


import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.dlut.tasks.Reserve4Tomorrow;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class ReserveTomorrowSchedule {
    @Resource
    private Reserve4Tomorrow reserve4Tomorrow;
    //这里要用伯川就输入bochuan，令希就改成lingxi
    private static final String selectedLibrary = "bochuan";
    private static final String selectedRoom = "504";

    @Scheduled(cron = "0 1 6 * * ?")
    public void reserve4TomorrowSchedule(){
        try {
            reserve4Tomorrow.setSelectedLibrary(selectedLibrary);
            reserve4Tomorrow.setSelectedRoom(selectedRoom);
            reserve4Tomorrow.tomorrowReserve();
            log.info("预定明天座位成功");
        }
        catch (InterruptedException e){
            log.error("预定明天座位失败");
        }
    }

}
