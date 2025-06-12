package uk.techreturners.VirtuArt.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uk.techreturners.VirtuArt.exception.ItemNotFoundException;
import uk.techreturners.VirtuArt.model.ExhibitionItem;
import uk.techreturners.VirtuArt.model.dto.ArtworkDTO;
import uk.techreturners.VirtuArt.model.request.AddArtworkRequest;
import uk.techreturners.VirtuArt.repository.ExhibitionItemRepository;

@Service
public class ExhibitionItemServiceImpl implements ExhibitionItemService {

    @Autowired
    ExhibitionItemRepository exhibitionItemRepository;

    @Autowired
    ArtworkService artworkService;

    @Override
    public ExhibitionItem addNewExhibitionItem(ArtworkDTO artworkDTO, String source) {
        ExhibitionItem newExhibitionItem = ExhibitionItem.builder()
                .apiId(artworkDTO.id())
                .title(artworkDTO.title())
                .artistTitle(artworkDTO.artist())
                .date(artworkDTO.date())
                .imageUrl(artworkDTO.imageUrl())
                .source(source)
                .build();
        return exhibitionItemRepository.save(newExhibitionItem);
    }

    @Override
    public ExhibitionItem getExhibitionItem(String apiId, String source) {
        if(exhibitionItemRepository.findByApiIdAndSource(apiId, source).isPresent()){
            return exhibitionItemRepository.findByApiIdAndSource(apiId, source).get();
        } else {
            throw new ItemNotFoundException(
                    String.format("ExhibitionItem with the Apid: %s and Source: %s could not be found", apiId, source)
            );
        }
    }

    @Override
    public ExhibitionItem getOrCreateExhibitionItem(AddArtworkRequest request) {
        if (exhibitionItemRepository.findByApiIdAndSource(request.apiId(), request.source()).isPresent()) {
            return exhibitionItemRepository.findByApiIdAndSource(request.apiId(), request.source()).get();
        } else {

            ArtworkDTO newArtwork = artworkService.getArtworkById(request.source(), request.apiId());

            return addNewExhibitionItem(newArtwork, request.source());
        }
    }
}