# 大工图书馆预定
基于chromeDriver和selenium简易的写了一个预定程序，通过xxl-job定时任务实现每天6点钟预约下一天的图书馆座位。在此基础上还做了一个锁座功能，每半个小时释放座位再重新预约，就可以实现锁定（虽然这样有点不守规矩了）。
## 基本使用方法
1. 配置xxl-job框架，这里的appname是dlut-library-schedule（注意改yml文件里的xxljob地址）。
2. 设置两个任务，预定下一天的任务JobHandler是ReserveTomorrowSchedule，锁座的任务是LockSeatSchedule，cron分别设置为0 1 6 * * ?和0 0,30 * * * ?。
3. yml文件下配置自己的用户名和密码，以及自己想要的座位
4. 在ReserveTomorrowSchedule下设置自己想去的图书馆和浏览室编号
5. 跑springboot程序就完事了

参考链接：
[大连理工大学图书馆自动预约座位小程序](https://github.com/ShuaichiLi/DLUT-library-auto-reservation)