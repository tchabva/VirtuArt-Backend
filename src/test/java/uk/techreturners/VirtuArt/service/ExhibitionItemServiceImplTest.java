package uk.techreturners.VirtuArt.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import uk.techreturners.VirtuArt.exception.ItemNotFoundException;
import uk.techreturners.VirtuArt.model.ExhibitionItem;
import uk.techreturners.VirtuArt.model.dto.ArtworkDTO;
import uk.techreturners.VirtuArt.model.request.AddArtworkRequest;
import uk.techreturners.VirtuArt.repository.ExhibitionItemRepository;

import java.util.Collections;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ExhibitionItemServiceImplTest {

    @Mock
    private ExhibitionItemRepository mockExhibitionItemRepository;

    @Mock
    private ArtworkService mockArtworkService;

    @InjectMocks
    private ExhibitionItemServiceImpl exhibitionItemService;

    private ArtworkDTO mockArtworkDTO;
    private ExhibitionItem mockExhibitionItem;
    private AddArtworkRequest mockAddArtworkRequest;

    private static final String API_ID = "123";
    private static final String SOURCE = "aic";

    @BeforeEach
    void setUp() {
        mockArtworkDTO = new ArtworkDTO(
                API_ID,
                "Mona Lisa",
                "Leonardo da Vinci",
                "1503-1506",
                "Oil on poplar panel",
                "https://www.image.com/imageId",
                Collections.emptyList(),
                "A famous portrait",
                "Italy",
                "Paintings",
                "Art Institute of Chicago"
        );

        mockExhibitionItem = ExhibitionItem.builder()
                .id("item_uuid-1")
                .apiId(API_ID)
                .title("Mona Lisa")
                .artistTitle("Leonardo da Vinci")
                .date("1503-1506")
                .imageUrl("https://www.image.com/imageId")
                .source(SOURCE)
                .exhibitions(Collections.emptyList())
                .build();

        mockAddArtworkRequest = new AddArtworkRequest(
                API_ID,
                SOURCE
        );
    }

    /**
     * addNewExhibition
     */
    @Nested
    @DisplayName("Add New Exhibition Item")
    class AddNewExhibitionItem {

        @Test
        @DisplayName("addNewExhibitionItem builds ExhibitionItem for ArtworkDTO & saves it")
        void addNewExhibitionItemMapsFieldsAndSaves() {
            // Arrange
            when(mockExhibitionItemRepository.save(any(ExhibitionItem.class))).thenReturn(mockExhibitionItem);

            // Act
            exhibitionItemService.addNewExhibitionItem(mockArtworkDTO, SOURCE);

            // Assert: capture what was passed to save() to verify field mapping
            ArgumentCaptor<ExhibitionItem> captor = ArgumentCaptor.forClass(ExhibitionItem.class);
            verify(mockExhibitionItemRepository).save(captor.capture());
            ExhibitionItem savedItem = captor.getValue();

            assertAll(
                    () -> assertEquals(mockArtworkDTO.id(), savedItem.getApiId()),
                    () -> assertEquals(mockArtworkDTO.title(), savedItem.getTitle()),
                    () -> assertEquals(mockArtworkDTO.artist(), savedItem.getArtistTitle()),
                    () -> assertEquals(mockArtworkDTO.date(), savedItem.getDate()),
                    () -> assertEquals(mockArtworkDTO.imageUrl(), savedItem.getImageUrl()),
                    () -> assertEquals(SOURCE, savedItem.getSource())
            );
        }

        @Test
        @DisplayName("addNewExhibition returns the save ExhibitionItem from the repository")
        void addNewExhibitionItemReturnsSavedEntity() {
            // Arrange
            when(mockExhibitionItemRepository.save(any(ExhibitionItem.class)))
                    .thenReturn(mockExhibitionItem);
            // Act
            ExhibitionItem result = exhibitionItemService.addNewExhibitionItem(mockArtworkDTO, SOURCE);

            // Assert
            assertAll(
                    () -> assertNotNull(result),
                    () -> assertEquals(mockExhibitionItem.getId(), result.getId()),
                    () -> assertEquals(mockExhibitionItem.getApiId(), result.getApiId()),
                    () -> assertEquals(mockExhibitionItem.getTitle(), result.getTitle()),
                    () -> assertEquals(mockExhibitionItem.getArtistTitle(), result.getArtistTitle()),
                    () -> assertEquals(mockExhibitionItem.getDate(), result.getDate()),
                    () -> assertEquals(mockExhibitionItem.getImageUrl(), result.getImageUrl()),
                    () -> assertEquals(mockExhibitionItem.getSource(), result.getSource())
            );

            verify(mockExhibitionItemRepository, times(1))
                    .save(any(ExhibitionItem.class));
        }

        @Test
        @DisplayName("addNewExhibitionItem does not call artworkService")
        void addNewExhibitionItemDoesNotCallArtworkService() {
            // Arrange
            when(mockExhibitionItemRepository.save(any(ExhibitionItem.class)))
                    .thenReturn(mockExhibitionItem);

            // Act
            exhibitionItemService.addNewExhibitionItem(mockArtworkDTO,SOURCE);

            // Assert
            verify(mockArtworkService, never()).getArtworkById(any(), any());
        }
    }

    /**
     * getExhibition
     */
    @Nested
    @DisplayName("Get Exhibition Item")
    class GetExhibitionItem {

        @Test
        @DisplayName("getExhibitionItem returns the ExhibitionItem when found by apiId & source")
        void getExhibitionItemReturnsItemWhenFound() {
            // Arrange
            when(mockExhibitionItemRepository.findByApiIdAndSource(API_ID, SOURCE))
                    .thenReturn(Optional.ofNullable(mockExhibitionItem));

            // Act
            ExhibitionItem result = exhibitionItemService.getExhibitionItem(API_ID,SOURCE);

            // Assert
            assertAll(
                    () -> assertNotNull(result),
                    () -> assertEquals(mockExhibitionItem.getId(), result.getId()),
                    () -> assertEquals(mockExhibitionItem.getApiId(), result.getApiId()),
                    () -> assertEquals(mockExhibitionItem.getTitle(), result.getTitle()),
                    () -> assertEquals(mockExhibitionItem.getArtistTitle(), result.getArtistTitle()),
                    () -> assertEquals(mockExhibitionItem.getDate(), result.getDate()),
                    () -> assertEquals(mockExhibitionItem.getImageUrl(), result.getImageUrl()),
                    () -> assertEquals(mockExhibitionItem.getSource(), result.getSource())
            );

            verify(mockExhibitionItemRepository, atLeastOnce()).findByApiIdAndSource(API_ID, SOURCE);
        }

        @Test
        @DisplayName("getExhibition throws ItemNotFoundException when no item matches apiId & source")
        void getExhibitionItemThrowsExceptionWhenNotFound() {
            // Arrange
            when(mockExhibitionItemRepository.findByApiIdAndSource(API_ID, SOURCE))
                    .thenReturn(Optional.empty());

            // Act & Assert
            assertThatThrownBy(() -> exhibitionItemService.getExhibitionItem(API_ID,SOURCE))
                    .isInstanceOf(ItemNotFoundException.class)
                    .hasMessageContaining(API_ID)
                    .hasMessageContaining(SOURCE);
        }

        @Test
        @DisplayName("getExhibition does not call artworkService or repository save")
        void getExhibitionItemDoesNotCallArtworkOrSave() {
            // Arrange
            when(mockExhibitionItemRepository.findByApiIdAndSource(API_ID,SOURCE))
                    .thenReturn(Optional.of(mockExhibitionItem));

            // Act
            exhibitionItemService.getExhibitionItem(API_ID, SOURCE);

            // Assert
            verify(mockArtworkService, never()).getArtworkById(any(),any());
            verify(mockExhibitionItemRepository, never()).save(any());
        }

        /**
         * getOrCreateExhibitionItem
         */
        @Nested
        @DisplayName("Get Or Create Exhibition Item")
        class GetOrCreateExhibitionItem {

            @Test
            @DisplayName("getOrCreateExhibitionItem returns existing item when found & does not call artworkService or save")
            void getOrCreateExhibitionItemReturnsExistingItemWhenFound() {
                // Arrange
                when(mockExhibitionItemRepository.findByApiIdAndSource(API_ID, SOURCE))
                        .thenReturn(Optional.of(mockExhibitionItem));

                // Act
                ExhibitionItem result = exhibitionItemService.getOrCreateExhibitionItem(mockAddArtworkRequest);

                // Assert
                assertAll(
                        () -> assertNotNull(result),
                        () -> assertEquals(mockExhibitionItem.getId(), result.getId()),
                        () -> assertEquals(mockExhibitionItem.getApiId(), result.getApiId()),
                        () -> assertEquals(mockExhibitionItem.getSource(), result.getSource())
                );

                verify(mockArtworkService, never()).getArtworkById(any(), any());
                verify(mockExhibitionItemRepository, never()).save(any());
            }

            @Test
            @DisplayName("getOrCreateExhibitionItem creates & saves a new item when not found")
            void getOrCreateExhibitionItemCreatesNewItemWhenNotFound() {
                // Arrange
                when(mockExhibitionItemRepository.findByApiIdAndSource(API_ID, SOURCE))
                        .thenReturn(Optional.empty());
                when(mockArtworkService.getArtworkById(SOURCE,API_ID))
                        .thenReturn(mockArtworkDTO);
                when(mockExhibitionItemRepository.save(any(ExhibitionItem.class)))
                        .thenReturn(mockExhibitionItem);

                // Act
                ExhibitionItem result = exhibitionItemService.getOrCreateExhibitionItem(mockAddArtworkRequest);

                // Assert
                assertAll(
                        () -> assertNotNull(result),
                        () -> assertEquals(mockExhibitionItem.getId(), result.getId()),
                        () -> assertEquals(mockExhibitionItem.getTitle(), result.getTitle()),
                        () -> assertEquals(mockExhibitionItem.getArtistTitle(), result.getArtistTitle()),
                        () -> assertEquals(mockExhibitionItem.getDate(), result.getDate()),
                        () -> assertEquals(mockExhibitionItem.getApiId(), result.getApiId()),
                        () -> assertEquals(mockExhibitionItem.getImageUrl(), result.getImageUrl()),
                        () -> assertEquals(mockExhibitionItem.getSource(), result.getSource())
                );

                verify(mockArtworkService, times(1)).getArtworkById(SOURCE, API_ID);
                verify(mockExhibitionItemRepository, times(1)).save(any(ExhibitionItem.class));
            }
        }
    }
}
