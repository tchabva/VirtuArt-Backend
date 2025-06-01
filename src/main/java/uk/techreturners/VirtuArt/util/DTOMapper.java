package uk.techreturners.VirtuArt.util;

import uk.techreturners.VirtuArt.exception.ItemNotFoundException;
import uk.techreturners.VirtuArt.model.Exhibition;
import uk.techreturners.VirtuArt.model.ExhibitionItem;
import uk.techreturners.VirtuArt.model.aicapi.AicApiSearchResult;
import uk.techreturners.VirtuArt.model.aicapi.AicApiSearchArtwork;
import uk.techreturners.VirtuArt.model.aicapi.AicApiArtwork;
import uk.techreturners.VirtuArt.model.dto.*;

// DTO Mapper Interface
public interface DTOMapper {
    default ArtworkDTO createArtworkDtoWithAicApi(AicApiArtwork aicApiArtwork) {
        if (aicApiArtwork == null) {
            throw new ItemNotFoundException("Null response from AIC API");
        } else {
            return ArtworkDTO.builder()
                    .id(aicApiArtwork.id().toString())
                    .title(aicApiArtwork.title())
                    .artist(aicApiArtwork.artist())
                    .date(aicApiArtwork.dateDisplay())
                    .displayMedium(aicApiArtwork.displayMedium())
                    .imageUrl(aicImageUrlCreator(aicApiArtwork.primaryImageId()))
                    .altImageUrls( // Map each altImageId in the List to the URL
                            aicApiArtwork.altImageIds().stream()
                                    .map(this::aicImageUrlCreator)
                                    .toList())
                    .description(aicApiArtwork.description())
                    .origin(aicApiArtwork.origin())
                    .category(aicApiArtwork.department())
                    .sourceMuseum("Art Institute of Chicago")
                    .build();
        }
    }

    default String aicImageUrlCreator(String artworkID) {
        if (artworkID == null || artworkID.isBlank()) {
            return "";
        } else {
            String AIC_IMAGE_URL = "https://www.artic.edu/iiif/2/";
            String AIC_DEFAULT_IMAGE_SETTING = "/full/843,/0/default.jpg";
            return AIC_IMAGE_URL.concat(artworkID).concat(AIC_DEFAULT_IMAGE_SETTING);
        }
    }

    default PaginatedArtworkResultsDTO aicPaginatedResponseMapper(AicApiSearchResult aicApiSearchResult) {
        if (aicApiSearchResult == null) {
            throw new ItemNotFoundException("Null response from AIC API");
        } else if (aicApiSearchResult.pagination() == null) {
            throw new ItemNotFoundException("Null pagination response from AIC API");
        } else if (aicApiSearchResult.data() == null) {
            throw new ItemNotFoundException("Null data response froma AIC API");
        } else {
            return PaginatedArtworkResultsDTO.builder()
                    .data(
                            aicApiSearchResult.data().stream()
                                    .map(this::aicSearchArtworkResultsResponseMapper)
                                    .toList())
                    .totalItems(aicApiSearchResult.pagination().total())
                    .pageSize(aicApiSearchResult.pagination().limit())
                    .totalPages(aicApiSearchResult.pagination().totalPages())
                    .currentPage(aicApiSearchResult.pagination().currentPage())
                    .hasNext(
                            PaginatedArtworkResultsDTO.checkHasNext.test(
                                    aicApiSearchResult.pagination().currentPage(),
                                    aicApiSearchResult.pagination().totalPages()
                            )
                    )
                    .hasPrevious(
                            PaginatedArtworkResultsDTO.checkHasPrevious.test(
                                    aicApiSearchResult.pagination().currentPage()
                            )
                    )
                    .build();
        }
    }

    default ArtworkResultsDTO aicSearchArtworkResultsResponseMapper(AicApiSearchArtwork aicArtworkSearchResult) {
        if (aicArtworkSearchResult == null) {
            throw new ItemNotFoundException("Null response from AIC API");
        } else {
            return ArtworkResultsDTO.builder()
                    .id(aicArtworkSearchResult.id().toString())
                    .title(aicArtworkSearchResult.title())
                    .artistTitle(aicArtworkSearchResult.artistTitle())
                    .date(aicArtworkSearchResult.dateDisplay())
                    .imageURL(aicImageUrlCreator(aicArtworkSearchResult.primaryImageId()))
                    .build();
        }
    }

    default ExhibitionDTO createExhibitionDTO(Exhibition exhibition) {
        if (exhibition == null) {
            throw new ItemNotFoundException("Exhibition Item could not be found");
        } else {
            return ExhibitionDTO.builder()
                    .id(exhibition.getId())
                    .title(exhibition.getTitle())
                    .itemCount(exhibition.getExhibitionItems().size())
                    .createdAt(exhibition.getCreatedAt())
                    .updatedAt(exhibition.getUpdatedAt())
                    .build();
        }
    }

    default ExhibitionDetailDTO createExhibitionDetailDTO(Exhibition exhibition) {
        if (exhibition == null) {
            throw new ItemNotFoundException("Exhibition Item could not be found");
        } else {
            return ExhibitionDetailDTO.builder()
                    .id(exhibition.getId())
                    .title(exhibition.getTitle())
                    .description(exhibition.getDescription())
                    .createdAt(exhibition.getCreatedAt())
                    .updatedAt(exhibition.getUpdatedAt())
                    .exhibitionItems(
                            exhibition.getExhibitionItems().stream().map(this::createExhibitionItemDTO).toList()
                    )
                    .build();
        }
    }

    default ExhibitionItemDTO createExhibitionItemDTO(ExhibitionItem exhibitionItem) {
        if (exhibitionItem == null) {
            throw new ItemNotFoundException("Exhibition Item could not be found");
        } else {
            return ExhibitionItemDTO.builder()
                    .id(exhibitionItem.getId())
                    .apiId(exhibitionItem.getApiId())
                    .title(exhibitionItem.getTitle())
                    .date(exhibitionItem.getDate())
                    .imageUrl(exhibitionItem.getImageUrl())
                    .source(exhibitionItem.getSource())
                    .build();
        }
    }
}