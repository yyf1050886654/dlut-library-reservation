package org.dlut.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
@Data
@ConfigurationProperties(prefix = "dlut")
public class LibraryConfig {

    private Map<String,LibraryMap> library;
    private List<String> wantedSeats;
    private Map<String,User> reserveUser;

    @Data
    public static class LibraryMap {
        private String code;
        private Map<String,String> rooms;
    }
    @Data
    public static class User {
        private String username;
        private String password;
    }
}
