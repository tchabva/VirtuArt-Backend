package uk.techreturners.VirtuArt.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Answers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import uk.techreturners.VirtuArt.model.aicapi.*;

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
    void testGetArtworks_success() {
        // Arrange
        String limit = "10";
        String page = "1";
        String expectedUri = "?fields=id,title,artist_title,date_display&limit=" + limit + "&page=" + page;

        when(mockRequestHeadersUriSpec.uri(expectedUri)).thenReturn(mockRequestHeadersSpec);
        when(mockResponseSpec.bodyToMono(AicApiSearchResult.class)).thenReturn(Mono.just(expectedSearchResult));

        // Act
        AicApiSearchResult actualResult = aicApiDAO.getArtworks(limit, page);

        // Assert
        assertNotNull(actualResult, "Result should not be null.");
        assertEquals(expectedSearchResult, actualResult, "Actual result should match expected result.");
        verify(mockWebClient.get()).uri(expectedUri);
    }
}