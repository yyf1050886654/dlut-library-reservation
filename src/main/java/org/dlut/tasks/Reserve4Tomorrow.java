package org.dlut.tasks;

import io.github.bonigarcia.wdm.WebDriverManager;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.dlut.config.LibraryConfig;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

@Component
@Setter
@Slf4j
public class Reserve4Tomorrow {

    @Autowired
    private LibraryConfig libraryConfig;
    private WebDriver driver;
    private String selectedLibrary;
    private String selectedRoom;
    private Map<String,String> infoMap;

    public void login(){
        //登录统一验证平台
        driver.get("https://sso.dlut.edu.cn/cas/login?service=http://seat.lib.dlut.edu.cn/yanxiujian/client/login.php?redirect=index.php");
        driver.findElement(By.id("un")).sendKeys(libraryConfig.getReserveUser().get("user1").getUsername());
        driver.findElement(By.id("pd")).sendKeys(libraryConfig.getReserveUser().get("user1").getPassword());
        driver.findElement(By.className("login-btn")).click();
        //顺带把输入数据处理一下
        String code = libraryConfig.getLibrary().get(selectedLibrary).getCode();
        String room = libraryConfig.getLibrary().get(selectedLibrary).getRooms().get(selectedRoom);
        infoMap = new HashMap<>();
        infoMap.put("code",code);
        infoMap.put("room",room);
    }
    public void tomorrowReserve() throws InterruptedException {
        WebDriverManager.chromedriver().setup();
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--remote-allow-origins=*");
        driver = new ChromeDriver(options);
        //登录统一验证平台
        login();
        //预约明天的座位
        String tomorrow = LocalDate.now().plusDays(1).format(DateTimeFormatter.ofPattern("yyyy/MM/dd"));
        driver.get("http://seat.lib.dlut.edu.cn/yanxiujian/client/orderSeat.php?method=addSeat&room_id="+infoMap.get("room")+"&areaid="+infoMap.get("code")+"&curdate="+tomorrow);
        Thread.sleep(200);
        try {
            for (String wantedSeat:libraryConfig.getWantedSeats()){
                // 定义显式等待（最多等待10秒）
                WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
                By seatLocator = By.xpath("//table/tbody//tr//td/div[@class='seat-normal']/i[contains(text()," + wantedSeat + ")]");
                WebElement seatElement = wait.until(ExpectedConditions.elementToBeClickable(seatLocator));
                seatElement.click();
                WebElement btnSubmitAddorder = driver.findElement(By.id("btn_submit_addorder"));
                Thread.sleep(200);
                try {
                    btnSubmitAddorder.click();
                }catch (Exception e){
                    log.info("已经有人预约过此座位："+wantedSeat);
                }
            }
        }
        finally {
            //退出并关闭
            driver.get("http://seat.lib.dlut.edu.cn/yanxiujian/client/loginOut.php");
            driver.quit();
        }


    }
}
