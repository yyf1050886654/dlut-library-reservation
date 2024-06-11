package org.dlut.schedule;

import com.xxl.job.core.handler.annotation.XxlJob;
import lombok.extern.slf4j.Slf4j;
import org.dlut.tasks.Reserve4Tomorrow;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class ReserveTomorrowSchedule {
    @Autowired
    private Reserve4Tomorrow reserve4Tomorrow;
    //这里要用伯川就输入bochuan，令希就改成lingxi
    private static final String selectedLibrary = "bochuan";
    private static final String selectedRoom = "504";
    @XxlJob("ReserveTomorrowSchedule")
    public void reserve4TomorrowSchedule(){
        try {
            reserve4Tomorrow.setSelectedLibrary(selectedLibrary);
            reserve4Tomorrow.setSelectedRoom(selectedRoom);
            reserve4Tomorrow.tomorrowReserve();
            log.debug("预定明天座位成功");
        }
        catch (InterruptedException e){
            log.debug("预定明天座位失败");
        }
    }

}
