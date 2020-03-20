package com.slyscrat.impress;

import com.slyscrat.impress.model.entity.GameEntity;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
public class ImpressApplication {
    public static void main(String[] args) {

        SpringApplication.run(ImpressApplication.class, args);
        RestTemplate template = new RestTemplate();
        //String res = template.exchange("http://api.steampowered.com/ISteamApps/GetAppList/v0002/?format=json&l=russian");
        String result = template.getForObject("http://api.steampowered.com/ISteamApps/GetAppList/v0002/?format=json&l=russian", String.class);
        System.out.println(result.substring(0, 100));
        /*GameEntity[] games = template.getForObject("http://api.steampowered.com/ISteamApps/GetAppList/v0002/?format=json&l=russian", GameEntity[].class);
        for (GameEntity game: games) {
            System.out.println(game);
        }*/
    }
}
