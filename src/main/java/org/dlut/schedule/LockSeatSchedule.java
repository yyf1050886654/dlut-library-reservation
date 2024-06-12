package org.dlut.schedule;

import com.xxl.job.core.handler.annotation.XxlJob;
import lombok.extern.slf4j.Slf4j;
import org.dlut.tasks.LockCurrentSeat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Map;

@Slf4j
@Component
public class LockSeatSchedule {
    @Autowired
    private LockCurrentSeat lockCurrentSeat;
    @XxlJob("LockSeatSchedule")
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
