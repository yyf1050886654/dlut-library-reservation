package org.dlut.schedule;

import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.dlut.tasks.LockCurrentSeat;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Slf4j
@Component
public class LockSeatSchedule {
    @Resource
    private LockCurrentSeat lockCurrentSeat;

    @Scheduled(cron = "0 0,30 * * * ?")
    public void schedule(){
        try {
            int hour = LocalDateTime.now().getHour();
            //每天八点钟开始，晚上八点结束
            if (hour>=8 && hour <= 20){
                lockCurrentSeat.reserveSeat();
            }
            System.out.println(("任务调度成功"));
        }
        catch (InterruptedException e){
            System.out.println(("任务调度失败"));
        }
    }
}
