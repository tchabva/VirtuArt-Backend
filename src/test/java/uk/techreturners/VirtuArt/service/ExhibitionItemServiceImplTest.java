package uk.techreturners.VirtuArt.service;

import org.apiguardian.api.API;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import uk.techreturners.VirtuArt.model.ExhibitionItem;
import uk.techreturners.VirtuArt.model.dto.ArtworkDTO;
import uk.techreturners.VirtuArt.model.request.AddArtworkRequest;
import uk.techreturners.VirtuArt.repository.ExhibitionItemRepository;

import java.util.Collections;

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
}
