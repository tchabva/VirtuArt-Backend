package uk.techreturners.VirtuArt.repository;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfig {

    @Bean("aicApiWebClient")
    public WebClient aicApiWebClient(){
        return WebClient.builder()
                .baseUrl("https://api.artic.edu/api/v1/artworks")
                .defaultHeader("User-Agent", "VirtuArt-Application - tawandachabva@gmail.com")
                .build();
    }
}