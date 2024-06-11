package org.dlut;

import org.dlut.config.LibraryConfig;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@SpringBootTest
@RunWith(SpringRunner.class)
class DlutLibraryReservationApplicationTests {
    @Autowired
    private LibraryConfig libraryConfig;

    @Test
    void contextLoads() {
        System.out.println(libraryConfig.getReserveUser());
    }

}
