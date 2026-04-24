package uk.techreturners.VirtuArt.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ActiveProfiles;
import uk.techreturners.VirtuArt.exception.IllegalSourceException;
import uk.techreturners.VirtuArt.exception.ItemNotFoundException;
import uk.techreturners.VirtuArt.model.aicapi.*;
import uk.techreturners.VirtuArt.model.cmaapi.*;
import uk.techreturners.VirtuArt.model.cmaapi.CmaApiPagination.CmaApiPaginationParameters;
import uk.techreturners.VirtuArt.model.cmaapi.CmaApiSearchResult.CmaApiSearchArtwork;
import uk.techreturners.VirtuArt.model.dto.ArtworkDTO;
import uk.techreturners.VirtuArt.model.dto.PaginatedArtworkResultsDTO;
import uk.techreturners.VirtuArt.repository.AicApiDAO;
import uk.techreturners.VirtuArt.repository.CmaApiDAO;

import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@ActiveProfiles("dev")
class ArtworkServiceImplTest {

    @Mock
    private AicApiDAO mockAicApiDAO;

    @Mock
    private CmaApiDAO mockCmaApiDAO;

    @Spy // For verifying the method calls for the DTOMapper Interface
    @InjectMocks
    private ArtworkServiceImpl artworkService;


    private AicApiSearchResult mockAicApiSearchResult;
    private AicApiSearchArtwork mockAicApiSearchArtwork;

    private CmaApiSearchResult mockCmaApiSearchResult;
    private CmaApiSearchArtwork mockCmaApiSearchArtwork;

    @BeforeEach
    void setUp() {
        // AIC Mock Data
        mockAicApiSearchArtwork = new AicApiSearchArtwork(
                123,
                "Mona Lisa",
                "1503-1506",
                "Leonardo da Vinci",
                "https://www.image.com/imageId"
        );

        AicApiPagination mockAicPagination = new AicApiPagination(
                100,
                10,
                10,
                1
        );

        mockAicApiSearchResult = new AicApiSearchResult(
                mockAicPagination,
                List.of(mockAicApiSearchArtwork)
        );

        // CMA Mock Data
        mockCmaApiSearchArtwork = new CmaApiSearchArtwork(
                123L,
                "Starry Night",
                "1889",
                new CmaApiImages(new CmaApiImages.CmaApiWeb("https://www.image.com/imageId")),
                List.of(new CmaApiCreators("https://www.image.com/imageId"))
        );

        CmaApiPagination mockCmaPagination = new CmaApiPagination(
                1000,
                new CmaApiPaginationParameters(20, 100)
        );

        mockCmaApiSearchResult = new CmaApiSearchResult(
                mockCmaPagination,
                List.of(mockCmaApiSearchArtwork)
        );
    }

    /**
     * Source Routing via getArtworks()
     */

    @Nested
    @DisplayName("getArtworks source routing")
    class GetArtworksRouting {

        @Test
        @DisplayName("Throws IllegalSourceException when provided invalid source")
        void invalidSourceThrowsException() {
            // Arrange
            String limit = "10";
            String page = "1";
            String source = "abc";

            // Act & Assert
            assertThatThrownBy(() -> artworkService.getArtworks(source, limit, page))
                    .isInstanceOf(IllegalSourceException.class)
                    .hasMessageContaining("Invalid data source: " + source);

            verify(mockAicApiDAO, never()).getArtworks(any(), any());
        }

        @Test
        @DisplayName("getArtworks with 'aic' source delegates to getAicArtworks")
        void testGetArtworksRoutesToAic() {
            // Arrange
            String limit = "10";
            String page = "1";
            String source = "aic";
            PaginatedArtworkResultsDTO expectedDTO = mock(PaginatedArtworkResultsDTO.class);
            doReturn(expectedDTO).when(artworkService).getAicArtworks(limit, page);

            // Act
            PaginatedArtworkResultsDTO result = artworkService.getArtworks(source, limit, page);

            // Assert
            assertSame(expectedDTO, result);
            verify(artworkService, times(1)).getAicArtworks(limit, page);
            verify(artworkService, never()).getCmaArtworks(any(), any());
        }

        @Test
        @DisplayName("getArtworks with 'cma' source delegates to getCmaArtworks")
        void testGetArtworksRoutesToCma() {
            // Arrange
            String limit = "10";
            String page = "1";
            String source = "cma";
            PaginatedArtworkResultsDTO expectedDTO = mock(PaginatedArtworkResultsDTO.class);
            doReturn(expectedDTO).when(artworkService).getCmaArtworks(limit, page);

            // Act
            PaginatedArtworkResultsDTO result = artworkService.getArtworks(source, limit, page);

            // Assert
            assertSame(expectedDTO, result);
            verify(artworkService, times(1)).getCmaArtworks(limit, page);
            verify(artworkService, never()).getAicArtworks(any(), any());
        }
    }

    /**
     * AIC Artworks
     */

    @Nested
    @DisplayName("AIC Artworks")
    class AicArtworks {

        @Test
        @DisplayName("getAicArtworks calls DAO and maps response correctly")
        void testGetAicArtworks() {
            // Arrange
            String limit = "10";
            String page = "1";
            when(mockAicApiDAO.getArtworks(limit, page)).thenReturn(mockAicApiSearchResult);

            // Act
            PaginatedArtworkResultsDTO resultDTO = artworkService.getAicArtworks(limit, page);

            // Assert
            assertAll(
                    () -> assertNotNull(resultDTO),
                    () -> assertEquals(mockAicApiSearchResult.pagination().total(), resultDTO.totalItems()),
                    () -> assertEquals(mockAicApiSearchResult.pagination().limit(), resultDTO.pageSize()),
                    () -> assertEquals(
                            mockAicApiSearchResult.pagination().totalPages(), resultDTO.totalPages()
                    ),
                    () -> assertEquals(
                            mockAicApiSearchResult.pagination().currentPage(), resultDTO.currentPage()
                    ),
                    () -> assertNotNull(resultDTO.data()),
                    () -> assertEquals(1, resultDTO.data().size()),
                    () -> assertEquals(
                            String.valueOf(mockAicApiSearchArtwork.id()), resultDTO.data().getFirst().id()
                    ),
                    () -> assertEquals(mockAicApiSearchArtwork.title(), resultDTO.data().getFirst().title()),
                    () -> assertEquals(
                            mockAicApiSearchArtwork.artistTitle(), resultDTO.data().getFirst().artistTitle()
                    ),
                    () -> assertEquals(
                            mockAicApiSearchArtwork.dateDisplay(), resultDTO.data().getFirst().date()
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

        @Test
        @DisplayName("getAicArtworks handles null pagination from DAO")
        void testGetAicArtworksHandlesNullPaginationFromDao() {
            // Arrange
            String limit = "10";
            String page = "1";
            AicApiSearchResult nullPaginationResult = new AicApiSearchResult(null, List.of(mockAicApiSearchArtwork));
            when(mockAicApiDAO.getArtworks(limit, page)).thenReturn(nullPaginationResult);
            // Act & Assert
            assertThatThrownBy(() -> artworkService.getAicArtworks(limit, page))
                    .isInstanceOf(ItemNotFoundException.class);

            verify(mockAicApiDAO).getArtworks(limit, page);
        }
    }

    /**
     * CMA Artworks
     */
    @Nested
    @DisplayName("CMA Artworks")
    class CmaArtworks {

        @Test
        @DisplayName("getCmaArtworks calls DAO & maps response correctly")
        void testGetCmaArtworks() {
            // Arrange
            String limit = "10";
            String page = "1";
            when(mockCmaApiDAO.getArtworks(Integer.parseInt(limit), Integer.parseInt(page)))
                    .thenReturn(mockCmaApiSearchResult);

            // Act
            PaginatedArtworkResultsDTO resultDTO = artworkService.getCmaArtworks(limit, page);

            // Assert
            assertAll(
                    () -> assertNotNull(resultDTO),
                    () -> assertEquals(mockCmaApiSearchResult.info().total(), resultDTO.totalItems()),
                    () -> assertEquals(mockCmaApiSearchResult.info().parameters().limit(), resultDTO.pageSize()),
                    () -> assertEquals(
                            mockCmaApiSearchResult.info().parameters().calculateTotalPages(
                                    mockCmaApiSearchResult.info().total()
                            ),
                            resultDTO.totalPages()
                    ),
                    () -> assertEquals(
                            mockCmaApiSearchResult.info().parameters().calculateCurrentPage(),
                            resultDTO.currentPage()
                    ),
                    () -> assertNotNull(resultDTO.data()),
                    () -> assertEquals(1, resultDTO.data().size()),
                    () -> assertEquals(
                            String.valueOf(mockCmaApiSearchArtwork.id()), resultDTO.data().getFirst().id()
                    ),
                    () -> assertEquals(mockCmaApiSearchArtwork.title(), resultDTO.data().getFirst().title()),
                    () -> assertEquals(mockCmaApiSearchArtwork.creationDate(), resultDTO.data().getFirst().date())
            );

            verify(mockCmaApiDAO).getArtworks(Integer.parseInt(limit), Integer.parseInt(page));
            verify(artworkService, times(1))
                    .cmaSearchArtworkResultsResponseMapper(any(CmaApiSearchArtwork.class));
            verify(artworkService, times(1))
                    .cmaPaginatedResponseMapper(any(CmaApiSearchResult.class));
        }

        @Test
        @DisplayName("getCmaArtworks handles null response from DAO")
        void getCmaArtworksHandlesNullDaoResponse() {
            // Arrange
            String limit = "10";
            String page = "1";
            when(mockCmaApiDAO.getArtworks(Integer.parseInt(limit), Integer.parseInt(page)))
                    .thenReturn(null);

            // Act & Assert
            assertThrows(ItemNotFoundException.class, () -> artworkService.getCmaArtworks(limit, page));

            verify(mockCmaApiDAO).getArtworks(Integer.parseInt(limit), Integer.parseInt(page));
        }

        @Test
        @DisplayName("getCmaArtworks handles null info/pagination from DAO")
        void getCmaArtworksHandlesNullInfoDao() {
            // Arrange
            String limit = "10";
            int intLimit = Integer.parseInt(limit);
            String page = "1";
            int intPage = Integer.parseInt(page);
            CmaApiSearchResult nullInfoResult = mock(CmaApiSearchResult.class);
            when(nullInfoResult.info()).thenReturn(null);
            when(mockCmaApiDAO.getArtworks(intLimit, intPage))
                    .thenReturn(nullInfoResult);

            // Act & Assert
            assertThrows(ItemNotFoundException.class, () -> artworkService.getCmaArtworks(limit, page));

            verify(mockCmaApiDAO).getArtworks(intLimit, intPage);
        }

        @Test
        @DisplayName("getCmaArtworks handles null data list from DAO")
        void getCmaArtworksHandlesNullDataListFromDao() {
            // Arrange
            String limit = "10";
            int intLimit = Integer.parseInt(limit);
            String page = "1";
            int intPage = Integer.parseInt(page);
            CmaApiPagination mockPagination = mock(CmaApiPagination.class);
            mockCmaApiSearchResult = new CmaApiSearchResult(mockPagination, null);
            when(mockCmaApiDAO.getArtworks(intLimit, intPage))
                    .thenReturn(mockCmaApiSearchResult);

            // Act & Assert
            assertThrows(ItemNotFoundException.class, () -> artworkService.getCmaArtworks(limit, page));

            verify(mockCmaApiDAO).getArtworks(intLimit, intPage);
        }

        @Test
        @DisplayName("getCmaArtworks handles empty data list from DAO")
        void getCmaArtworksHandlesEmptyDataListFromDao() {
            // Arrange
            String limit = "10";
            int intLimit = Integer.parseInt(limit);
            String page = "1";
            int intPage = Integer.parseInt(page);
            CmaApiPagination mockPagination = mock(CmaApiPagination.class);
            CmaApiPaginationParameters mockPaginationParams = mock(CmaApiPaginationParameters.class);
            mockCmaApiSearchResult = new CmaApiSearchResult(mockPagination, Collections.emptyList());
            when(mockPagination.total()).thenReturn(0);
            when(mockPagination.parameters()).thenReturn(mockPaginationParams);
            when(mockPagination.parameters().calculateTotalPages(0)).thenReturn(0);
            when(mockCmaApiSearchResult.info().parameters().calculateTotalPages(0)).thenReturn(0);
            when(mockCmaApiDAO.getArtworks(intLimit, intPage)).thenReturn(mockCmaApiSearchResult);

            // Act
            PaginatedArtworkResultsDTO resultDTO = artworkService.getCmaArtworks(limit, page);

            // Assert
            assertAll(
                    () -> assertNotNull(resultDTO),
                    () -> assertNotNull(resultDTO.data()),
                    () -> assertEquals(0,resultDTO.data().size()),
                    () -> assertEquals(0,resultDTO.totalItems()),
                    () -> assertEquals(0,resultDTO.totalPages())
            );

            verify(mockCmaApiDAO).getArtworks(intLimit, intPage);
            verify(artworkService, times(0))
                    .cmaSearchArtworkResultsResponseMapper(any(CmaApiSearchArtwork.class));
            verify(artworkService, times(1))
                    .cmaPaginatedResponseMapper(any(CmaApiSearchResult.class));
        }
    }

    /**
     *  Get Artwork by ID
     */
    @Nested
    @DisplayName("Get Artwork By ID")
    class GetArtworkById {
        @Test
        @DisplayName("getArtworkById with 'aic' source calls AIC DAO & maps response")
        void getArtworkByIdWithAicSource() {
            // Arrange
            String artworkId = "123";
            String source = "aic";
            AicApiArtwork aicArtwork = new AicApiArtwork(
                    123,
                    "Mona Lisa",
                    "1503-1506",
                    "Italy",
                    "A famous portrait",
                    "Oil on poplar panel",
                    "Paintings",
                    "Leonardo da Vinci",
                    "imageId123",
                    Collections.emptyList()
            );
            AicApiArtworkResult aicArtworkResult = new AicApiArtworkResult(aicArtwork);
            when(mockAicApiDAO.getArtworkById(artworkId)).thenReturn(aicArtworkResult);

            // Act
            ArtworkDTO resultDTO = artworkService.getArtworkById(source, artworkId);

            // Assert
            assertAll(
                    () -> assertNotNull(resultDTO),
                    () -> assertEquals(aicArtwork.id().toString(), resultDTO.id()),
                    () -> assertEquals(aicArtwork.title(), resultDTO.title()),
                    () -> assertEquals(aicArtwork.artist(), resultDTO.artist()),
                    () -> assertEquals(aicArtwork.dateDisplay(), resultDTO.date()),
                    () -> assertEquals(aicArtwork.displayMedium(), resultDTO.displayMedium()),
                    () -> assertEquals(aicArtwork.description(), resultDTO.description()),
                    () -> assertEquals(aicArtwork.department(), resultDTO.department()),
                    () -> assertEquals(aicArtwork.origin(), resultDTO.origin()),
                    () -> assertEquals("Art Institute of Chicago", resultDTO.sourceMuseum())
            );

            verify(mockAicApiDAO).getArtworkById(artworkId);
        }

        @Test
        @DisplayName("getArtworkById with 'cma' source calls CMA DAO & maps response")
        void getArtworkByIdWithCmaSource() {
            // Arrange
            String artworkId = "456";
            String source = "cma";
            CmaApiArtwork cmaArtwork = new CmaApiArtwork(
                    456L,
                    "Starry Nights",
                    "1889",
                    List.of("France"),
                    "Oil on canvas",
                    "Paintings",
                    "A famous night scene",
                    new CmaApiImages(new CmaApiImages.CmaApiWeb("imageId456")),
                    Collections.emptyList(),
                    List.of(new CmaApiCreators("Vincent van Gogh"))
            );
            CmaApiArtworkResult cmaArtworkResult = new CmaApiArtworkResult(cmaArtwork);
            when(mockCmaApiDAO.getArtworkById(artworkId)).thenReturn(cmaArtworkResult);

            // Act
            ArtworkDTO resultDTO = artworkService.getArtworkById(source, artworkId);

            // Assert
            assertAll(
                    () -> assertNotNull(resultDTO),
                    () -> assertEquals(cmaArtwork.id().toString(), resultDTO.id()),
                    () -> assertEquals(cmaArtwork.title(), resultDTO.title()),
                    () -> assertEquals(cmaArtwork.creators().getFirst().description(), resultDTO.artist()),
                    () -> assertEquals(cmaArtwork.creationDate(), resultDTO.date()),
                    () -> assertEquals(cmaArtwork.technique(), resultDTO.displayMedium()),
                    () -> assertEquals(cmaArtwork.description(), resultDTO.description()),
                    () -> assertEquals(cmaArtwork.department(), resultDTO.department()),
                    () -> assertEquals(cmaArtwork.culture().getFirst(), resultDTO.origin()),
                    () -> assertEquals("Cleveland Museum of Art", resultDTO.sourceMuseum())
            );

            verify(mockCmaApiDAO).getArtworkById(artworkId);
        }

        @Test
        @DisplayName("getArtworkId throws IllegalSourceException for invalid source")
        void getArtworkByIdHandlesNullAicData() {
            // Arrange
            String artworkId = "123";
            String invalidSource = "xyz";

            // Act & Assert
            assertThatThrownBy( () -> artworkService.getArtworkById(invalidSource, artworkId))
                    .isInstanceOf(IllegalSourceException.class)
                    .hasMessageContaining( "Invalid data source: " + invalidSource);

            verify(mockAicApiDAO, never()).getArtworkById(any());
        }

        @Test
        @DisplayName("getArtworkById throws ItemNotFoundException when AIC DAO returns null data")
        void getArtworkByIdHandsNullAicData() {
            // Arrange
            String artworkId = "123";
            String source = "aic";
            AicApiArtworkResult aicArtworkResult = new AicApiArtworkResult(null);
            when(mockAicApiDAO.getArtworkById(artworkId)).thenReturn(aicArtworkResult);

            // Act & Assert
            assertThatThrownBy(() -> artworkService.getArtworkById(source, artworkId))
                    .isInstanceOf(ItemNotFoundException.class)
                    .hasMessageContaining("Null response from Art Institute of Chicago API");

            verify(mockAicApiDAO).getArtworkById(artworkId);
        }

        @Test
        @DisplayName("getArtworkById throws ItemNotFoundException when CMA DAO returns null data")
        void getArtworkByIdHandlesNullCmaData() {
            // Arrange
            String artworkId = "123";
            String source = "cma";
            CmaApiArtworkResult cmaArtworkResult = new CmaApiArtworkResult(null);
            when(mockCmaApiDAO.getArtworkById(artworkId)).thenReturn(cmaArtworkResult);

            // Act & Assert
            assertThatThrownBy(() -> artworkService.getArtworkById(source, artworkId))
                    .isInstanceOf(ItemNotFoundException.class)
                    .hasMessageContaining("Null response from Cleveland Museum of Art API");

            verify(mockCmaApiDAO).getArtworkById(artworkId);
        }
    }
}