package uk.techreturners.VirtuArt.service;

import org.apiguardian.api.API;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import uk.techreturners.VirtuArt.model.ExhibitionItem;
import uk.techreturners.VirtuArt.model.dto.ArtworkDTO;
import uk.techreturners.VirtuArt.model.request.AddArtworkRequest;
import uk.techreturners.VirtuArt.repository.ExhibitionItemRepository;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

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
     *  addNewExhibition
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
    }
}
