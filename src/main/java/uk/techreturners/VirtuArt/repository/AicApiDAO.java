package uk.techreturners.VirtuArt.repository;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Repository;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientRequestException;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import uk.techreturners.VirtuArt.exception.ApiServiceException;
import uk.techreturners.VirtuArt.model.aicapi.AicApiSearchResult;
import uk.techreturners.VirtuArt.model.aicapi.AicApiArtwork;
import uk.techreturners.VirtuArt.model.aicapi.AicApiArtworkResult;

@Repository
public class AicApiDAO {
    private final WebClient webClient = WebClient.builder()
            .baseUrl("https://api.artic.edu/api/v1/artworks")
            .build();

    public AicApiSearchResult getArtworks(String limit, String page) {
        try {
            return webClient
                    .get()
                    .uri("?fields=id,title,artist_title,date_display&limit=" + limit + "&page=" + page)
                    .retrieve()
                    .bodyToMono(AicApiSearchResult.class)
                    .block();
        } catch (WebClientResponseException e) {
            HttpStatus status = HttpStatus.valueOf(e.getStatusCode().value());
            throw new ApiServiceException(e.getMessage(), status);
        } catch (WebClientRequestException e) {
            throw new ApiServiceException(e.getMessage());
        }
    }

    public AicApiArtwork getArtworkById(String artworkId) {
        String params = "?fields=id,title,artist_title,image_id,place_of_origin,date_display,description,alt_image_ids,medium_display,department_title";
        try {
            AicApiArtworkResult responseBody = webClient
                    .get()
                    .uri("/" + artworkId + params)
                    .retrieve()
                    .bodyToMono(AicApiArtworkResult.class)
                    .block();
            return responseBody.data();
        } catch (WebClientResponseException e) {
            HttpStatus status = HttpStatus.valueOf(e.getStatusCode().value());
            throw new ApiServiceException(e.getMessage(), status);
        } catch (WebClientRequestException e) {
            throw new ApiServiceException(e.getMessage());
        }
    }
}