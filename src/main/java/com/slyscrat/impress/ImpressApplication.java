package com.slyscrat.impress;

import com.slyscrat.impress.model.entity.GameEntity;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
public class ImpressApplication {
    public static void main(String[] args) {
        SpringApplication.run(ImpressApplication.class, args);
    }
}
