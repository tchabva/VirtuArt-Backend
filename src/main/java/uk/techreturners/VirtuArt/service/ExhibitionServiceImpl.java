package uk.techreturners.VirtuArt.service;

import org.springframework.beans.factory.annotation.Autowired;
import uk.techreturners.VirtuArt.exception.ItemNotFoundException;
import uk.techreturners.VirtuArt.model.ExhibitionItem;
import uk.techreturners.VirtuArt.model.User;
import uk.techreturners.VirtuArt.model.dto.ExhibitionDTO;
import uk.techreturners.VirtuArt.model.dto.ExhibitionDetailDTO;
import uk.techreturners.VirtuArt.model.request.AddArtworkRequest;
import uk.techreturners.VirtuArt.model.request.CreateExhibitionRequest;
import uk.techreturners.VirtuArt.model.request.UpdateExhibitionRequest;
import uk.techreturners.VirtuArt.repository.ExhibitionRepository;
import uk.techreturners.VirtuArt.repository.UserRepository;
import uk.techreturners.VirtuArt.util.DTOMapper;

import java.util.List;

public class ExhibitionServiceImpl implements ExhibitionService, DTOMapper {

    @Autowired
    private  ExhibitionRepository exhibitionRepository;

    @Autowired
    private UserRepository userRepository;


    @Override
    public List<ExhibitionDTO> getAllUserExhibitions() {
        User cUser;
        if (userRepository.findByGoogleId("test").isPresent()){
          cUser = userRepository.findByGoogleId("test").get();
          return exhibitionRepository.findByUser(cUser).stream().map(this::createExhibitionDTO).toList();
        }else {
            throw new ItemNotFoundException("User could not be found");
        }
    }

    @Override
    public ExhibitionDetailDTO getExhibitionById(String id) {
        return null;
    }

    @Override
    public ExhibitionDTO createUserExhibition(CreateExhibitionRequest request) {
        return null;
    }

    @Override
    public ExhibitionItem addArtworkToExhibition(String exhibitionId, AddArtworkRequest request) {
        return null;
    }

    @Override
    public Void removeArtworkFromExhibition(String exhibitionId, String artworkId) {
        return null;
    }

    @Override
    public Void deleteExhibition(String id) {
        return null;
    }

    @Override
    public ExhibitionDTO updateExhibitionDetails(String exhibitionId, UpdateExhibitionRequest request) {
        return null;
    }
}
