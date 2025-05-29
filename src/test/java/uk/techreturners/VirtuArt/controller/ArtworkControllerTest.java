package uk.techreturners.VirtuArt.controller;

import org.junit.jupiter.api.BeforeEach;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import uk.techreturners.VirtuArt.model.dto.ArtworkResultsDTO;
import uk.techreturners.VirtuArt.model.dto.PaginatedArtworkResultsDTO;
import uk.techreturners.VirtuArt.service.ArtworkService;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

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



}