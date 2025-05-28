package uk.techreturners.VirtuArt.util;

import uk.techreturners.VirtuArt.model.aicapi.AicApiSearchResult;
import uk.techreturners.VirtuArt.model.aicapi.AicApiSearchArtwork;
import uk.techreturners.VirtuArt.model.aicapi.AicApiArtwork;
import uk.techreturners.VirtuArt.model.dto.ArtworkDTO;
import uk.techreturners.VirtuArt.model.dto.ArtworkResultsDTO;
import uk.techreturners.VirtuArt.model.dto.PaginatedArtworkResultsDTO;

// DTO Mapper Interface
public interface DTOMapper {
    default ArtworkDTO createArtworkDtoWithAicApi(AicApiArtwork aicApiArtwork) {
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

    default String aicImageUrlCreator(String artworkID) {
        String AIC_IMAGE_URL = "https://www.artic.edu/iiif/2/";
        String AIC_DEFAULT_IMAGE_SETTING = "/full/843,/0/default.jpg";
        return AIC_IMAGE_URL.concat(artworkID).concat(AIC_DEFAULT_IMAGE_SETTING);
    }

    default PaginatedArtworkResultsDTO aicPaginatedResponseMapper(AicApiSearchResult aicApiSearchResult) {
        return PaginatedArtworkResultsDTO.builder()
                .data(
                        aicApiSearchResult.data().stream()
                                .map(this::aicArtworkResultsResponseMapper)
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

    default ArtworkResultsDTO aicArtworkResultsResponseMapper(AicApiSearchArtwork aicArtworkSearchResult) {
        return ArtworkResultsDTO.builder()
                .id(aicArtworkSearchResult.id().toString())
                .title(aicArtworkSearchResult.title())
                .artistTitle(aicArtworkSearchResult.artistTitle())
                .date(aicArtworkSearchResult.dateDisplay())
                .build();
    }
}
