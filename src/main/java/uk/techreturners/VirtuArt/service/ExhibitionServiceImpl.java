package uk.techreturners.VirtuArt.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import uk.techreturners.VirtuArt.exception.ExhibitionItemExistsAlreadyException;
import uk.techreturners.VirtuArt.exception.ItemNotFoundException;
import uk.techreturners.VirtuArt.model.Exhibition;
import uk.techreturners.VirtuArt.model.ExhibitionItem;
import uk.techreturners.VirtuArt.model.User;
import uk.techreturners.VirtuArt.model.dto.ExhibitionDTO;
import uk.techreturners.VirtuArt.model.dto.ExhibitionDetailDTO;
import uk.techreturners.VirtuArt.model.request.AddArtworkRequest;
import uk.techreturners.VirtuArt.model.request.CreateExhibitionRequest;
import uk.techreturners.VirtuArt.model.request.UpdateExhibitionRequest;
import uk.techreturners.VirtuArt.repository.ExhibitionRepository;
import uk.techreturners.VirtuArt.util.DTOMapper;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ExhibitionServiceImpl implements ExhibitionService, DTOMapper {

    @Autowired
    private ExhibitionRepository exhibitionRepository;

    @Autowired
    private ExhibitionItemService exhibitionItemService;

    @Autowired
    private UserService userService;

    // Helper method
    private User getAuthenticatedUser(Jwt jwt) {
        return userService.getCurrentUser(jwt);
    }

    // Helper method
    private Exhibition getExhibitionForCurrentUser(String exhibitionId, User currentUser) {
        Optional<Exhibition> exhibition = exhibitionRepository.findById(exhibitionId);
        if (exhibition.isPresent()) {
            // Verifies ownership
            if (exhibition.get().getUser() == null || !exhibition.get().getUser().getId().equals(currentUser.getId())) {
                throw new AccessDeniedException("You are not authorised to access or modify this Exhibition");
            }
            return exhibition.get();
        } else {
            throw new ItemNotFoundException(String.format("Exhibition with the id: %s could not be found", exhibitionId));
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<ExhibitionDTO> getAllUserExhibitions(Jwt jwt) {
        User currentUser = getAuthenticatedUser(jwt);
        return exhibitionRepository.findByUser(currentUser).stream()
                .map(this::createExhibitionDTO)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public ExhibitionDetailDTO getExhibitionById(String id, Jwt jwt) {
        User currentUser = getAuthenticatedUser(jwt);
        Exhibition exhibition = getExhibitionForCurrentUser(id, currentUser); // Verifies ownership
        return createExhibitionDetailDTO(exhibition);
    }

    @Override
    @Transactional
    public ExhibitionDTO createUserExhibition(CreateExhibitionRequest request, Jwt jwt) {
        User currentUser = getAuthenticatedUser(jwt);
        Exhibition newExhibition = Exhibition.builder()
                .title(request.title())
                .description(request.description())
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .exhibitionItems(new ArrayList<>())
                .user(currentUser)
                .build();
        return createExhibitionDTO(exhibitionRepository.save(newExhibition));
    }

    @Override
    @Transactional
    public ExhibitionDTO addArtworkToExhibition(String exhibitionId, AddArtworkRequest request, Jwt jwt) {
        User currentUser = getAuthenticatedUser(jwt);
        Exhibition exhibition = getExhibitionForCurrentUser(exhibitionId, currentUser); // Verifies ownership
        ExhibitionItem exhibitionItem = exhibitionItemService.getOrCreateExhibitionItem(request);

        if (exhibition.getExhibitionItems().stream().anyMatch(
                item -> item.getId().equals(exhibitionItem.getId()))
        ) {
            throw new ExhibitionItemExistsAlreadyException(
                    String.format(
                            "The ExhibitionItem name: %s and apiId: %s already in this Exhibition",
                            exhibition.getTitle(), exhibition.getId()
                    )
            );
        } else {
            exhibition.getExhibitionItems().add(exhibitionItem);
            exhibition.setUpdatedAt(LocalDateTime.now());
            Exhibition updatedExhibition = exhibitionRepository.save(exhibition);
            return createExhibitionDTO(updatedExhibition);
        }
    }

    @Override
    @Transactional
    public Void removeArtworkFromExhibition(String exhibitionId, String apiId, String source, Jwt jwt) {
        User currentUser = getAuthenticatedUser(jwt);
        Exhibition exhibition = getExhibitionForCurrentUser(exhibitionId, currentUser); // Verifies ownership
        ExhibitionItem exhibitionItemToDelete = exhibitionItemService.getExhibitionItem(apiId, source);

        boolean isRemoved = exhibition.getExhibitionItems().removeIf(item ->
                item.getId().equals(exhibitionItemToDelete.getId())
        );

        if (isRemoved) {
            exhibition.setUpdatedAt(LocalDateTime.now());
            exhibitionRepository.save(exhibition);
        } else {
            throw new ItemNotFoundException(
                    String.format(
                            "Artwork with the apiId %s from source %s could not be found in exhibition %s",
                            apiId,
                            source,
                            exhibitionId
                    )
            );
        }
        return null;
    }

    @Override
    @Transactional
    public Void deleteExhibition(String id, Jwt jwt) {
        User currentUser = getAuthenticatedUser(jwt);
        Exhibition exhibition = getExhibitionForCurrentUser(id, currentUser); // Verifies ownership
        exhibitionRepository.deleteById(exhibition.getId());
        return null;
    }

    @Override
    @Transactional
    public ExhibitionDTO updateExhibitionDetails(String exhibitionId, UpdateExhibitionRequest request, Jwt jwt) {
        User currentUser = getAuthenticatedUser(jwt);
        Exhibition exhibition = getExhibitionForCurrentUser(exhibitionId, currentUser); // Verifies ownership
        boolean isUpdated = false;

        if (request != null) {
            if (request.title() != null
                    && !request.title().isBlank()
                    && !request.title().trim().equals(exhibition.getTitle())
            ) {
                exhibition.setTitle(request.title().trim());
                isUpdated = true;
            }
            if (request.description() != null
                    && !request.description().isBlank()
                    && !request.description().trim().equals(exhibition.getDescription())) {
                exhibition.setDescription(request.description().trim());
                isUpdated = true;
            }
        }

        if(isUpdated){
            exhibition.setUpdatedAt(LocalDateTime.now());
            return createExhibitionDTO(exhibitionRepository.save(exhibition));
        }
        return createExhibitionDTO(exhibition);
    }
}