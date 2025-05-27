package uk.techreturners.VirtuArt.repository;

import org.springframework.stereotype.Repository;
import org.springframework.web.reactive.function.client.WebClient;
import uk.techreturners.VirtuArt.model.aicapi.AicApiResult;
import uk.techreturners.VirtuArt.model.aicapi.AicArtwork;
import uk.techreturners.VirtuArt.model.aicapi.AicArtworkResult;

@Repository
public class AicApiDAO {
    private final WebClient webClient = WebClient.builder()
            .baseUrl("https://api.artic.edu/api/v1/artworks")
            .build();

    public AicApiResult getArtworks(String limit, String page) {
        try {
            return webClient
                    .get()
                    .uri("?fields=id,title,artist_title,date_display&limit="+ limit +"20&page=" + page)
                    .retrieve()
                    .bodyToMono(AicApiResult.class)
                    .block();
        } catch (Exception e) {
            // TODO: Error handling
            return null;
        }
    }

    public AicArtwork getArtworkById(String artworkId) {
        String params = "?fields=id,title,artist_title,image_id,place_of_origin,date_display,description,alt_image_ids,medium_display,department_title";
        try {
            AicArtworkResult responseBody = webClient
                    .get()
                    .uri("/" + artworkId + params)
                    .retrieve()
                    .bodyToMono(AicArtworkResult.class)
                    .block();
            return responseBody.data();
        } catch (Exception e) {
            // TODO: Error handling in the vein of "ItemNotFoundException"
            return null;
        }
    }
}
