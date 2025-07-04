package uk.techreturners.VirtuArt.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import uk.techreturners.VirtuArt.exception.ItemNotFoundException;
import uk.techreturners.VirtuArt.model.aicapi.AicApiPagination;
import uk.techreturners.VirtuArt.model.aicapi.AicApiSearchArtwork;
import uk.techreturners.VirtuArt.model.aicapi.AicApiSearchResult;
import uk.techreturners.VirtuArt.model.dto.PaginatedArtworkResultsDTO;
import uk.techreturners.VirtuArt.repository.AicApiDAO;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@DataJpaTest
class ArtworkServiceImplTest {

    @Mock
    private AicApiDAO mockAicApiDAO;

    @Spy // For verifying the method calls for the DTOMapper Interface
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
                "Leonardo da Vinci",
                "https://www.image.com/imageId"
        );

        AicApiPagination mockPagination = new AicApiPagination(
                100,
                10,
                10,
                1
        );

        mockApiSearchResult = new AicApiSearchResult(
                mockPagination,
                List.of(mockApiSearchArtwork)
        );
    }

    @Test
    @DisplayName("getAicArtworks calls DAO and maps response correctly")
    void testGetAicArtworks() {
        // Arrange
        String limit = "10";
        String page = "1";
        when(mockAicApiDAO.getArtworks(limit, page)).thenReturn(mockApiSearchResult);

        // Act
        PaginatedArtworkResultsDTO resultDTO = artworkService.getAicArtworks(limit, page);

        // Assert
        assertAll(
                () -> assertNotNull(resultDTO),
                () -> assertEquals(mockApiSearchResult.pagination().total(), resultDTO.totalItems()),
                () -> assertEquals(mockApiSearchResult.pagination().limit(), resultDTO.pageSize()),
                () -> assertEquals(
                        mockApiSearchResult.pagination().totalPages(), resultDTO.totalPages()
                ),
                () -> assertEquals(
                        mockApiSearchResult.pagination().currentPage(), resultDTO.currentPage()
                ),
                () -> assertNotNull(resultDTO.data()),
                () -> assertEquals(1, resultDTO.data().size()),
                () -> assertEquals(
                        String.valueOf(mockApiSearchArtwork.id()), resultDTO.data().getFirst().id()
                ),
                () -> assertEquals(mockApiSearchArtwork.title(), resultDTO.data().getFirst().title()),
                () -> assertEquals(
                        mockApiSearchArtwork.artistTitle(), resultDTO.data().getFirst().artistTitle()
                ),
                () -> assertEquals(
                        mockApiSearchArtwork.dateDisplay(), resultDTO.data().getFirst().date()
                )
        );

        verify(mockAicApiDAO).getArtworks(limit, page); // Verify DAO method was called
        // Verify that the DTOMapper's methods were involved.
        verify(artworkService, times(1))
                .aicSearchArtworkResultsResponseMapper(any(AicApiSearchArtwork.class));
        verify(artworkService, times(1))
                .aicPaginatedResponseMapper(any(AicApiSearchResult.class));
    }

    @Test
    @DisplayName("getAicArtworks handles null response from DAO")
    void testGetAicArtworksHandlesNullDaoResponse() {
        // Arrange
        String limit = "10";
        String page = "1";
        when(mockAicApiDAO.getArtworks(limit, page)).thenReturn(null);

        // Act & Assert
        assertThrows(ItemNotFoundException.class, () -> artworkService.getAicArtworks(limit, page));

        verify(mockAicApiDAO).getArtworks(limit, page);
    }

    @Test
    @DisplayName("getAicArtworks handles empty data list from DAO")
    void testGetAicArtworksHandlesEmptyDataListFromDao() {
        // Arrange
        String limit = "10";
        String page = "1";
        AicApiPagination mockPagination = new AicApiPagination(
                0, 10, 0, 1
        );
        AicApiSearchResult emptyDataApiResult = new AicApiSearchResult(
                mockPagination,
                Collections.emptyList()
        );
        when(mockAicApiDAO.getArtworks(limit, page)).thenReturn(emptyDataApiResult);

        // Act
        PaginatedArtworkResultsDTO resultDTO = artworkService.getAicArtworks(limit, page);

        // Assert
        assertAll(
                () -> assertNotNull(resultDTO),
                () -> assertNotNull(resultDTO.data()),
                () -> assertEquals(0, resultDTO.data().size()),
                () -> assertEquals(0, resultDTO.totalItems()),
                () -> assertEquals(0, resultDTO.totalPages())
        );

        verify(mockAicApiDAO).getArtworks(limit, page);
        verify(artworkService, times(0)) // Should not be called if the response is empty
                .aicSearchArtworkResultsResponseMapper(any(AicApiSearchArtwork.class));
        verify(artworkService, times(1))
                .aicPaginatedResponseMapper(any(AicApiSearchResult.class));
    }

    @Test
    @DisplayName("getAicArtworks handles null data from DAO")
    void testGetAicArtworksHandlesNullDataFromDao() {
        // Arrange
        String limit = "10";
        String page = "1";
        AicApiPagination mockPagination = new AicApiPagination(
                100, 10, 10, 1
        );
        AicApiSearchResult nullDataResult = new AicApiSearchResult(
                mockPagination,
                null
        );
        when(mockAicApiDAO.getArtworks(limit, page)).thenReturn(nullDataResult);

        // Act & Assert
        assertThrows(ItemNotFoundException.class, () -> artworkService.getAicArtworks(limit, page));

        verify(mockAicApiDAO).getArtworks(limit, page);
    }
}