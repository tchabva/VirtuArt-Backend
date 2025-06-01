package uk.techreturners.VirtuArt.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uk.techreturners.VirtuArt.exception.ItemNotFoundException;
import uk.techreturners.VirtuArt.model.ExhibitionItem;
import uk.techreturners.VirtuArt.model.request.AddArtworkRequest;
import uk.techreturners.VirtuArt.repository.ExhibitionItemRepository;

@Service
public class ExhibitionItemServiceImpl implements ExhibitionItemService {

    @Autowired
    ExhibitionItemRepository exhibitionItemRepository;

    @Override
    public ExhibitionItem addNewExhibitionItem(AddArtworkRequest request) {
        ExhibitionItem newExhibitionItem = ExhibitionItem.builder()
                .apiId(request.apiId())
                .title(request.title())
                .artistTitle(request.artistTitle())
                .date(request.date())
                .imageUrl(request.imageUrl())
                .source(request.source())
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
            return addNewExhibitionItem(request);
        }
    }
}