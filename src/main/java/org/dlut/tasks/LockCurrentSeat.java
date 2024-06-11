package org.dlut.tasks;

import lombok.extern.slf4j.Slf4j;
import org.dlut.config.LibraryConfig;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
@Slf4j
public class LockCurrentSeat {

    @Autowired
    private LibraryConfig libraryConfig;
    private static final String regexSeat = "座位标签：(\\d{3})";
    private static final String regexLibrary = "(.{2})图书馆";
    private static final String regexRoom = "图书馆(\\d{3})";
    private Map<String,String> infoMap;
    private WebDriver driver;

    /**
     * 文本分割
     * @param text tr标签的文本
     * @return 包含seat，code和room的map
     */
    public Map<String,String> regexFind(String text){
        Map<String,String> map = new HashMap<>();
        //正则表达式匹配座位
        Matcher seat = Pattern.compile(regexSeat).matcher(text);
        if (seat.find()) {
            map.put("seat",seat.group(1));
        }
        //正则表达式匹配图书馆
        Matcher libraryName = Pattern.compile(regexLibrary).matcher(text);
        LibraryConfig.LibraryMap libraryMap = new LibraryConfig.LibraryMap();
        if (libraryName.find()){
            String code = "";
            if (libraryName.group(1).equals("伯川")){
                libraryMap =  libraryConfig.getLibrary().get("bochuan");
                code = libraryMap.getCode();
            }
            else if (libraryName.group(1).equals("令希")){
                libraryMap =  libraryConfig.getLibrary().get("lingxi");
                code = libraryMap.getCode();
            }
            map.put("code",code);
        }
        //正则表达式匹配浏览室
        Matcher room = Pattern.compile(regexRoom).matcher(text);
        if (room.find()){
            Map<String, String> rooms = libraryMap.getRooms();
            String roomId = room.group(1);
            String roomCode = rooms.get(roomId);
            map.put("room",roomCode);
        }

        return map;
    }
    public void login(){
        //登录统一验证平台
        driver.get("https://sso.dlut.edu.cn/cas/login?service=http://seat.lib.dlut.edu.cn/yanxiujian/client/login.php?redirect=index.php");
        driver.findElement(By.id("un")).sendKeys(libraryConfig.getReserveUser().get("user1").getUsername());
        driver.findElement(By.id("pd")).sendKeys(libraryConfig.getReserveUser().get("user1").getPassword());
        driver.findElement(By.className("login-btn")).click();
    }
    public void reserveSeat() throws InterruptedException {
        driver = new ChromeDriver();
        //登录统一验证平台
        login();
        //进入我的预约
        driver.get("https://seat.lib.dlut.edu.cn/yanxiujian/client/orderInfo.php");
        try {
            new WebDriverWait(driver,Duration.ofSeconds(10)).until(
                    ExpectedConditions.presenceOfElementLocated(By.id("tb_departments"))
            );
            WebElement table = driver.findElement(By.id("tb_departments"));
            List<WebElement> cells = table.findElements(By.tagName("tr"));
            String target = "签到";
            for (WebElement cell : cells){
                if (cell.getText().contains(target)){
                    infoMap = regexFind(cell.getText());
                    //点击取消按钮
                    WebElement cancleButton = cell.findElement(By.tagName("button"));
                    cancleButton.click();
                    Thread.sleep(500);
                    //确认框
                    List<WebElement> footer = driver.findElements(By.className("modal-footer"));
                    WebElement confirm = footer.get(2).findElements(By.tagName("button")).get(1);
                    confirm.click();
                    Thread.sleep(500);
                    break;
                }
            }
            //跳转到选座页面
            String today = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy/MM/dd"));
            driver.get("http://seat.lib.dlut.edu.cn/yanxiujian/client/orderSeat.php?method=addSeat&room_id="+infoMap.get("room")+"&areaid="+infoMap.get("code")+"&curdate="+today);
            Thread.sleep(500);
            WebElement seat = driver.findElement(By.xpath("//table/tbody//tr//td/div[@class='seat-normal']/i[contains(text()," + infoMap.get("seat") + ")]"));
            seat.click();
            WebElement btnSubmitAddorder = driver.findElement(By.id("btn_submit_addorder"));
            Thread.sleep(500);
            btnSubmitAddorder.click();
            System.out.println("seat has been reserved successfully!");
        }catch (java.lang.NullPointerException e){
            log.debug("已在图书馆或没有进行预约");
        }
        finally {
            //退出并关闭
            driver.get("http://seat.lib.dlut.edu.cn/yanxiujian/client/loginOut.php");
            driver.quit();
        }

    }
}
