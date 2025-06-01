package uk.techreturners.VirtuArt.service;

import org.springframework.beans.factory.annotation.Autowired;
import uk.techreturners.VirtuArt.exception.ItemNotFoundException;
import uk.techreturners.VirtuArt.model.Exhibition;
import uk.techreturners.VirtuArt.model.ExhibitionItem;
import uk.techreturners.VirtuArt.model.User;
import uk.techreturners.VirtuArt.model.dto.ExhibitionDTO;
import uk.techreturners.VirtuArt.model.dto.ExhibitionDetailDTO;
import uk.techreturners.VirtuArt.model.request.AddArtworkRequest;
import uk.techreturners.VirtuArt.model.request.CreateExhibitionRequest;
import uk.techreturners.VirtuArt.model.request.UpdateExhibitionRequest;
import uk.techreturners.VirtuArt.repository.ExhibitionItemRepository;
import uk.techreturners.VirtuArt.repository.ExhibitionRepository;
import uk.techreturners.VirtuArt.repository.UserRepository;
import uk.techreturners.VirtuArt.util.DTOMapper;

import java.time.LocalDateTime;
import java.util.List;

public class ExhibitionServiceImpl implements ExhibitionService, DTOMapper {

    @Autowired
    private ExhibitionRepository exhibitionRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ExhibitionItemRepository exhibitionItemRepository;

    @Override
    public List<ExhibitionDTO> getAllUserExhibitions() {
        User cUser;
        if (userRepository.findByGoogleId("test").isPresent()) {
            cUser = userRepository.findByGoogleId("test").get();
            return exhibitionRepository.findByUser(cUser).stream().map(this::createExhibitionDTO).toList();
        } else {
            throw new ItemNotFoundException("User could not be found");
        }
    }

    @Override
    public ExhibitionDetailDTO getExhibitionById(String id) {
        if (exhibitionRepository.findById(id).isPresent()) {
            return createExhibitionDetailDTO(exhibitionRepository.findById(id).get());
        } else {
            throw new ItemNotFoundException(String.format("Exhibition with the id: %s could not be found", id));
        }
    }

    @Override
    public ExhibitionDTO createUserExhibition(CreateExhibitionRequest request) {
        User cUser;
        if (userRepository.findByGoogleId("test").isPresent()) {
            cUser = userRepository.findByGoogleId("test").get();
            Exhibition newExhibition = Exhibition.builder()
                    .title(request.title())
                    .description(request.description())
                    .createdAt(LocalDateTime.now())
                    .updatedAt(LocalDateTime.now())
                    .user(cUser)
                    .build();
            return createExhibitionDTO(exhibitionRepository.save(newExhibition));
        } else {
            throw new ItemNotFoundException("User could not be found");
        }
    }

    @Override
    public ExhibitionDTO addArtworkToExhibition(String exhibitionId, AddArtworkRequest request) {
        if (exhibitionRepository.findById(exhibitionId).isPresent()) {
            Exhibition exhibition = exhibitionRepository.findById(exhibitionId).get();
            ExhibitionItem newExhibitionItem = ExhibitionItem.builder()
                    .apiId(request.apiId())
                    .title(request.title())
                    .artistTitle(request.artistTitle())
                    .date(request.date())
                    .imageUrl(request.imageUrl())
                    .source(request.source())
                    .build();
            exhibition.getExhibitionItems().add(newExhibitionItem);
            return createExhibitionDTO(exhibitionRepository.save(exhibition));
        } else {
            throw new ItemNotFoundException(String.format("Exhibition with the id: %s could not be found", exhibitionId));
        }
    }

    @Override
    public Void removeArtworkFromExhibition(String exhibitionId, String artworkId) {
        if (exhibitionRepository.findById(exhibitionId).isPresent()) {
            Exhibition exhibition = exhibitionRepository.findById(exhibitionId).get();
            if (exhibitionItemRepository.findById(artworkId).isPresent()) {
                ExhibitionItem exhibitionItem = exhibitionItemRepository.findById(artworkId).get();
                exhibition.getExhibitionItems().remove(exhibitionItem);
                exhibitionRepository.save(exhibition);
            } else {
                throw new ItemNotFoundException(
                        String.format("ExhibitionItem with the id: %s could not be found", artworkId)
                );
            }
        } else {
            throw new ItemNotFoundException(String.format("Exhibition with the id: %s could not be found", exhibitionId));
        }
        return null;
    }

    @Override
    public Void deleteExhibition(String id) {
        if (exhibitionRepository.findById(id).isPresent()) {
            exhibitionRepository.deleteById(id);
        } else {
            throw new ItemNotFoundException(String.format("Exhibition with the id: %s could not be found", id));
        }
        return null;
    }

    @Override
    public ExhibitionDTO updateExhibitionDetails(String exhibitionId, UpdateExhibitionRequest request) {
        if (exhibitionRepository.findById(exhibitionId).isPresent()) {
            Exhibition exhibition = exhibitionRepository.findById(exhibitionId).get();
            if (request != null) {
                if (request.title() != null && !request.title().isBlank()) {
                    exhibition.setTitle(request.title());
                    exhibition.setUpdatedAt(LocalDateTime.now());
                }
                if (request.description() != null && !request.description().isBlank()) {
                    exhibition.setDescription(request.description());
                    exhibition.setUpdatedAt(LocalDateTime.now());
                }
            }
            return createExhibitionDTO(exhibitionRepository.save(exhibition));
        } else {
            throw new ItemNotFoundException(
                    String.format("Exhibition with the id: %s could not be found", exhibitionId)
            );
        }
    }
}