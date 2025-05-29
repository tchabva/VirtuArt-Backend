package uk.techreturners.VirtuArt.service;

import org.junit.jupiter.api.BeforeEach;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import uk.techreturners.VirtuArt.model.aicapi.AicApiPagination;
import uk.techreturners.VirtuArt.model.aicapi.AicApiSearchArtwork;
import uk.techreturners.VirtuArt.model.aicapi.AicApiSearchResult;
import uk.techreturners.VirtuArt.repository.AicApiDAO;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;

class ArtworkServiceImplTest {

    @Mock
    private AicApiDAO mockAicApiDAO;

    @InjectMocks
    private ArtworkServiceImpl artworkService;


    private AicApiSearchResult mockApiSearchResult;
    private AicApiSearchArtwork mockApiSearchArtwork;

    @BeforeEach
    void setUp() {
        // Arrange: Common setup for mock DAO responses
        mockApiSearchArtwork = new AicApiSearchArtwork(
                123,
                "Mona Lisa",
                "1503-1506",
                "Leonardo da Vinci"
        );

        AicApiPagination mockPagination = new AicApiPagination(
                100,
                10,
                10,
                1
        );

        mockApiSearchResult = new AicApiSearchResult(
                mockPagination,
                Collections.singletonList(mockApiSearchArtwork)
        );
    }

}