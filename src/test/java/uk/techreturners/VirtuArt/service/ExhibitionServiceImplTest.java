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
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.test.context.ActiveProfiles;
import uk.techreturners.VirtuArt.model.Exhibition;
import uk.techreturners.VirtuArt.model.ExhibitionItem;
import uk.techreturners.VirtuArt.model.User;
import uk.techreturners.VirtuArt.model.dto.ExhibitionDTO;
import uk.techreturners.VirtuArt.model.dto.ExhibitionDetailDTO;
import uk.techreturners.VirtuArt.repository.ExhibitionRepository;

import javax.swing.text.html.Option;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

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

    /**
     * getAllUserExhibitions
     */
    @Nested
    @DisplayName("Get All User Exhibitions")
    class GetAllUserExhibitions {

        @Test
        @DisplayName("getAllUserExhibitions returns a mapped ExhibitionDTO for the current user")
        void getAllUserExhibitionsReturnsMappedDtoList() {
            // Arrange
            when(mockUserService.getCurrentUser(mockJwt)).thenReturn(mockUserOne);
            when(mockExhibitionRepository.findByUser(mockUserOne)).thenReturn(List.of(mockExhibition));

            // Act
            List<ExhibitionDTO> results = exhibitionService.getAllUserExhibitions(mockJwt);

            // Assert
            assertAll(
                    () -> assertNotNull(results),
                    () -> assertEquals(1, results.size()),
                    () -> assertEquals(mockExhibition.getId(), results.getFirst().id()),
                    () -> assertEquals(mockExhibition.getTitle(), results.getFirst().title()),
                    () -> assertEquals(mockExhibition.getExhibitionItems().size(), results.getFirst().itemCount()),
                    () -> assertEquals(mockExhibition.getCreatedAt(), results.getFirst().createdAt()),
                    () -> assertEquals(mockExhibition.getUpdatedAt(), results.getFirst().updatedAt())
            );

            verify(mockExhibitionRepository).findByUser(mockUserOne);
            verify(exhibitionService, times(1)).createExhibitionDTO(mockExhibition);
        }

        @Test
        @DisplayName("getAllUserExhibitions returns an empty list when the user has no exhibitions")
        void getAllUserExhibitionsReturnsEmptyList() {
            // Arrange
            when(mockUserService.getCurrentUser(mockJwt)).thenReturn(mockUserOne);
            when(mockExhibitionRepository.findByUser(mockUserOne)).thenReturn(Collections.emptyList());

            // Act
            List<ExhibitionDTO> results = exhibitionService.getAllUserExhibitions(mockJwt);

            // Assert
            assertAll(
                    () -> assertNotNull(results),
                    () -> assertTrue(results.isEmpty())
            );
        }

        @Test
        @DisplayName("getAllUserExhibitions returns a correctly sized list when the user has multiple exhibitions")
        void getAllUserExhibitionsReturnsAllExhibitions() {
            // Arrange
            Exhibition secondExhibition = Exhibition.builder()
                    .id("exhibition-uuid-2")
                    .title("My Second Exhibition")
                    .description("Another test exhibition")
                    .createdAt(FIXED_TIME)
                    .updatedAt(FIXED_TIME)
                    .user(mockUserOne)
                    .exhibitionItems(new ArrayList<>())
                    .build();
            when(mockUserService.getCurrentUser(mockJwt)).thenReturn(mockUserOne);
            when(mockExhibitionRepository.findByUser(mockUserOne))
                    .thenReturn(List.of(mockExhibition, secondExhibition));

            // Act
            List<ExhibitionDTO> results = exhibitionService.getAllUserExhibitions(mockJwt);

            // Assert
            assertAll(
                    () -> assertNotNull(results),
                    () -> assertEquals(2, results.size())
            );

            verify(exhibitionService, times(2))
                    .createExhibitionDTO(any(Exhibition.class));
        }
    }

    /**
     * getExhibitionById
     */
    @Nested
    @DisplayName("Get Exhibition By ID")
    class GetExhibitionById {

        @Test
        @DisplayName("getExhibitionById returns an ExhibitionDetailDTO when found & owned by the current user")
        void getExhibitionByIdReturnsDetailDTO() {
            // Arrange
            when(mockUserService.getCurrentUser(mockJwt)).thenReturn(mockUserOne);
            when(mockExhibitionRepository.findById(EXHIBITION_ID))
                    .thenReturn(Optional.of(mockExhibition));

            // Act
            ExhibitionDetailDTO result = exhibitionService.getExhibitionById(EXHIBITION_ID,mockJwt);

            // Assert
            assertAll(
                    () -> assertNotNull(result),
                    () -> assertEquals(mockExhibition.getId(), result.id()),
                    () -> assertEquals(mockExhibition.getTitle(), result.title()),
                    () -> assertEquals(mockExhibition.getDescription(), result.description()),
                    () -> assertEquals(mockExhibition.getCreatedAt(), result.createdAt()),
                    () -> assertEquals(mockExhibition.getUpdatedAt(), result.updatedAt()),
                    () -> assertNotNull(result.exhibitionItems())
            );

            verify(mockExhibitionRepository).findById(EXHIBITION_ID);
            verify(exhibitionService, times(1)).createExhibitionDetailDTO(mockExhibition);
        }
    }
}