package uk.techreturners.VirtuArt.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.test.context.ActiveProfiles;
import uk.techreturners.VirtuArt.model.Exhibition;
import uk.techreturners.VirtuArt.model.ExhibitionItem;
import uk.techreturners.VirtuArt.model.User;
import uk.techreturners.VirtuArt.repository.ExhibitionRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@ActiveProfiles("dev")
class ExhibitionServiceImplTest {

    @Mock
    private ExhibitionRepository mockExhibitionRepository;

    @Mock
    private ExhibitionItemService mockExhibitionItemService;

    @Mock
    private UserService mockUserService;

    @Mock
    private Jwt mockJwt;

    @Spy // as ExhibitionServiceImpl implements DTOMapper
    @InjectMocks
    private ExhibitionServiceImpl exhibitionService;

    private User mockUserOne;
    private User mockUserTwo;
    private Exhibition mockExhibition;
    private ExhibitionItem mockExhibitionItem;

    private static final String EXHIBITION_ID = "exhibition-uuid-1";
    private static final String USER_ONE_ID = "user-uuid-1";
    private static final String USER_TWO_ID = "user-uuid-2";
    private static final String ITEM_ID = "item-uuid-1";
    private static final String API_ID = "123";
    private static final String SOURCE = "aic";
    private static final LocalDateTime FIXED_TIME = LocalDateTime.of(2024, 1, 1, 12, 0);

    @BeforeEach
    void setUp() {
        // Arrange Users
        mockUserOne = User.builder()
                .id(USER_ONE_ID)
                .build();

        mockUserTwo = User.builder()
                .id(USER_TWO_ID)
                .build();

        // Arrange ExhibitionItem
        mockExhibitionItem = ExhibitionItem.builder()
                .id(ITEM_ID)
                .apiId(API_ID)
                .title("Mona Lisa")
                .artistTitle("Leonardo da Vinci")
                .date("1503-1506")
                .imageUrl("https://www.image.com/imageId")
                .source(SOURCE)
                .exhibitions(new ArrayList<>())
                .build();

        // Arrange Exhibition owned by mockUserOne
        mockExhibition = Exhibition.builder()
                .id(EXHIBITION_ID)
                .title("My Exhibition")
                .description("A test exhibition")
                .createdAt(FIXED_TIME)
                .updatedAt(FIXED_TIME)
                .user(mockUserOne)
                .exhibitionItems(new ArrayList<>())
                .build();
    }

}