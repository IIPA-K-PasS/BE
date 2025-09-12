package com.k_passs.backend.domain.user;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;

@ConfigurationProperties(prefix = "kakao")
@Component
@Getter
@Setter
public class KakaoProperties {
    private Client client;
    private Redirect redirect;

    @Getter @Setter
    public static class Client {
        private String id;
        private String secret;
        private List<String> allowedIds;
    }

    @Getter @Setter
    public static class Redirect {
        private String uri;
    }
}