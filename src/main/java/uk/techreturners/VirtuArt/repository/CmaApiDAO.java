package uk.techreturners.VirtuArt.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Repository;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientRequestException;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import uk.techreturners.VirtuArt.exception.ApiServiceException;
import uk.techreturners.VirtuArt.exception.ItemNotFoundException;
import uk.techreturners.VirtuArt.model.cmaapi.CmaApiSearchResult;

@Repository
public class CmaApiDAO {

    @Autowired
    @Qualifier("clevelandApiWebClient")
    private WebClient webClient;

    public CmaApiSearchResult getArtworks(Integer limit, Integer page){
        try {
            int skip = (page - 1) * limit;
            String params = "/?cc0&fields=id,title,creation_date,creators,images&limit=";

            return webClient
                    .get()
                    .uri(params + limit + "&skip=" + skip)
                    .retrieve()
                    .bodyToMono(CmaApiSearchResult.class)
                    .block();
        }catch (WebClientResponseException e) {
            HttpStatus status = HttpStatus.valueOf(e.getStatusCode().value());
            throw new ApiServiceException(e.getMessage(), status);
        } catch (WebClientRequestException e) {
            throw new ApiServiceException(e.getMessage());
        }
    }
}
