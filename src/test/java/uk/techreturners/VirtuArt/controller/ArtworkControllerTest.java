package uk.techreturners.VirtuArt.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import uk.techreturners.VirtuArt.model.dto.ArtworkResultsDTO;
import uk.techreturners.VirtuArt.model.dto.PaginatedArtworkResultsDTO;
import uk.techreturners.VirtuArt.service.ArtworkService;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@DataJpaTest
class ArtworkControllerTest {

    @Mock
    private ArtworkService mockArtworkService;

    @InjectMocks
    private ArtworkController artworkController;

    private PaginatedArtworkResultsDTO expectedPaginatedResponse;

    @BeforeEach
    void setUp() {
        // Arrange: Common setup for multiple tests
        ArtworkResultsDTO artworkDTO = ArtworkResultsDTO.builder()
                .id("1")
                .title("Test Artwork")
                .artistTitle("Test Artist")
                .date("2023")
                .build();
        List<ArtworkResultsDTO> artworkList = Collections.singletonList(artworkDTO);

        expectedPaginatedResponse = PaginatedArtworkResultsDTO.builder()
                .data(artworkList)
                .totalItems(1)
                .pageSize(10)
                .totalPages(1)
                .currentPage(1)
                .hasNext(false)
                .hasPrevious(false)
                .build();
    }

    @Test
    @DisplayName("getArtworks with default parameters returns PaginatedArtworkResultsDTO and OK status")
    void testGetArtworksWithDefaultParametersReturnsPaginatedResultsAndOk() {
        // Arrange
        String defaultLimit = "50";
        String defaultPage = "1";
        when(mockArtworkService.getAicArtworks(defaultLimit, defaultPage)).thenReturn(expectedPaginatedResponse);

        // Act
        ResponseEntity<PaginatedArtworkResultsDTO> responseEntity = artworkController.getArtworks(defaultLimit, defaultPage);

        // Assert
        assertAll(
                () -> assertNotNull(responseEntity),
                () -> assertEquals(HttpStatus.OK, responseEntity.getStatusCode()),
                () -> assertEquals(expectedPaginatedResponse, responseEntity.getBody())
        );
        verify(mockArtworkService).getAicArtworks(defaultLimit, defaultPage); // Verify service method was called
    }



}