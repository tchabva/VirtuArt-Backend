package uk.techreturners.VirtuArt.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Answers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientRequestException;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;
import uk.techreturners.VirtuArt.exception.ApiServiceException;
import uk.techreturners.VirtuArt.model.aicapi.*;

import java.net.URI;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AicApiDAOTest {

    @Mock(answer = Answers.RETURNS_DEEP_STUBS)
    private WebClient mockWebClient;

    @Mock
    private WebClient.RequestHeadersUriSpec mockRequestHeadersUriSpec;

    @Mock
    private WebClient.RequestHeadersSpec mockRequestHeadersSpec;

    @Mock
    private WebClient.ResponseSpec mockResponseSpec;

    @InjectMocks
    private AicApiDAO aicApiDAO;

    private AicApiSearchResult expectedSearchResult;
    private AicApiArtwork expectedAicArtwork;
    private AicApiArtworkResult expectedAicArtworkResult;

    @BeforeEach
    void setUp() {
        // Common setup for WebClient mocking
        when(mockWebClient.get()).thenReturn(mockRequestHeadersUriSpec);
        when(mockRequestHeadersUriSpec.uri(anyString())).thenReturn(mockRequestHeadersSpec);
        when(mockRequestHeadersSpec.retrieve()).thenReturn(mockResponseSpec);

        // Setup for getArtworks
        AicApiSearchArtwork searchArtwork = new AicApiSearchArtwork(1, "Title", "Date", "Artist");
        AicApiPagination pagination = new AicApiPagination(1, 1, 1, 1);
        expectedSearchResult = new AicApiSearchResult(pagination, Collections.singletonList(searchArtwork));

        // Setup for getArtworkById
        expectedAicArtwork = new AicApiArtwork(
                12345, "Artwork Title", "20th Century", "Place of Origin",
                "A detailed description of the artwork.", "Oil on canvas", "Modern Art",
                "Artist Name", "image_id_123", List.of("alt_image_id_1")
        );
        expectedAicArtworkResult = new AicApiArtworkResult(expectedAicArtwork);
    }

    @Test
    @DisplayName("getArtworks successfully retrieves and returns AicApiSearchResult")
    void testGetArtworks() {
        // Arrange
        String limit = "10";
        String page = "1";
        String expectedUri = "?fields=id,title,artist_title,date_display&limit=" + limit + "&page=" + page;

        when(mockRequestHeadersUriSpec.uri(expectedUri)).thenReturn(mockRequestHeadersSpec);
        when(mockResponseSpec.bodyToMono(AicApiSearchResult.class)).thenReturn(Mono.just(expectedSearchResult));

        // Act
        AicApiSearchResult actualResult = aicApiDAO.getArtworks(limit, page);

        // Assert
        assertNotNull(actualResult);
        assertEquals(expectedSearchResult, actualResult);
        verify(mockWebClient.get()).uri(expectedUri);
    }

    @Test
    @DisplayName("getArtworks throws ApiServiceException when WebClientResponseException occurs")
    void testGetArtworksWebClientResponseExceptionThrowsApiServiceException() {
        // Arrange
        String limit = "10";
        String page = "1";
        String expectedUri = "?fields=id,title,artist_title,date_display&limit=" + limit + "&page=" + page;

        WebClientResponseException responseException = WebClientResponseException.create(
                404, "Not Found", null, null, null
        );

        when(mockRequestHeadersUriSpec.uri(expectedUri)).thenReturn(mockRequestHeadersSpec);
        when(mockResponseSpec.bodyToMono(AicApiSearchResult.class)).thenReturn(Mono.error(responseException));

        // Act & Assert
        ApiServiceException exception = assertThrows(ApiServiceException.class,
                () -> aicApiDAO.getArtworks(limit, page));

        assertEquals("404 Not Found", exception.getMessage());
        assertEquals(HttpStatus.NOT_FOUND, exception.getStatus());
        verify(mockWebClient).get();
    }

    @Test
    @DisplayName("getArtworks throws ApiServiceException when WebClientRequestException occurs")
    void testGetArtworksWebClientRequestExceptionThrowsApiServiceException() {
        // Arrange
        String limit = "10";
        String page = "1";
        String expectedUri = "?fields=id,title,artist_title,date_display&limit=" + limit + "&page=" + page;
        HttpHeaders headers = new HttpHeaders();

        WebClientRequestException requestException = new WebClientRequestException(
                new ApiServiceException("Connection failed"),
                HttpMethod.GET,
                URI.create(""),
                headers
        );

        when(mockRequestHeadersUriSpec.uri(expectedUri)).thenReturn(mockRequestHeadersSpec);
        when(mockResponseSpec.bodyToMono(AicApiSearchResult.class)).thenReturn(Mono.error(requestException));

        // Act & Assert
        ApiServiceException exception = assertThrows(ApiServiceException.class,
                () -> aicApiDAO.getArtworks(limit, page));

        assertNotNull(exception.getMessage());
        assertEquals("Connection failed", exception.getMessage());
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, exception.getStatus());
        verify(mockWebClient).get();
    }

    @Test
    @DisplayName("getArtworkById successfully retrieves and returns AicApiArtworkResult")
    void testGetArtworkById() {
        // Arrange
        String artworkId = "12345";
        String expectedParams = "?fields=id,title,artist_title,image_id,place_of_origin,date_display,description,alt_image_ids,medium_display,department_title";
        String expectedUri = "/" + artworkId + expectedParams;

        when(mockRequestHeadersUriSpec.uri(expectedUri)).thenReturn(mockRequestHeadersSpec);
        when(mockResponseSpec.bodyToMono(AicApiArtworkResult.class)).thenReturn(Mono.just(expectedAicArtworkResult));

        // Act
        AicApiArtworkResult actualResult = aicApiDAO.getArtworkById(artworkId);

        // Assert
        assertNotNull(actualResult);
        assertEquals(expectedAicArtworkResult, actualResult);
        verify(mockWebClient).get();
        verify(mockRequestHeadersUriSpec).uri(expectedUri);
    }
}