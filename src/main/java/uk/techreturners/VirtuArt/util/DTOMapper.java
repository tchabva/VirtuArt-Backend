package uk.techreturners.VirtuArt.util;

import uk.techreturners.VirtuArt.model.aicapi.AicApiResult;
import uk.techreturners.VirtuArt.model.aicapi.AicApiSearchArtwork;
import uk.techreturners.VirtuArt.model.aicapi.AicArtwork;
import uk.techreturners.VirtuArt.model.dto.ArtworkDTO;
import uk.techreturners.VirtuArt.model.dto.ArtworkResultsDTO;
import uk.techreturners.VirtuArt.model.dto.PaginatedArtworkResultsDTO;

// DTO Mapper Interface
public interface DTOMapper {
    default ArtworkDTO createArtworkDtoWithAicApi(AicArtwork aicArtwork) {
        return ArtworkDTO.builder()
                .id(aicArtwork.id().toString())
                .title(aicArtwork.title())
                .artist(aicArtwork.artist())
                .date(aicArtwork.dateDisplay())
                .displayMedium(aicArtwork.displayMedium())
                .imageUrl(aicImageUrlCreator(aicArtwork.primaryImageId()))
                .altImageUrls( // Map each altImageId in the List to the URL
                        aicArtwork.altImageIds().stream()
                                .map(this::aicImageUrlCreator)
                                .toList())
                .description(aicArtwork.description())
                .origin(aicArtwork.origin())
                .category(aicArtwork.department())
                .sourceMuseum("Art Institute of Chicago")
                .build();
    }

    default String aicImageUrlCreator(String artworkID) {
        String AIC_IMAGE_URL = "https://www.artic.edu/iiif/2/";
        String AIC_DEFAULT_IMAGE_SETTING = "/full/843,/0/default.jpg";
        return AIC_IMAGE_URL.concat(artworkID).concat(AIC_DEFAULT_IMAGE_SETTING);
    }

    default PaginatedArtworkResultsDTO aicPaginatedResponseMapper(AicApiResult aicApiResult) {
        return PaginatedArtworkResultsDTO.builder()
                .data(
                        aicApiResult.data().stream()
                                .map(this::aicArtworkResultsResponseMapper)
                                .toList())
                .totalItems(aicApiResult.pagination().total())
                .pageSize(aicApiResult.pagination().limit())
                .totalPages(aicApiResult.pagination().totalPages())
                .currentPage(aicApiResult.pagination().currentPage())
                .hasNext(
                        PaginatedArtworkResultsDTO.checkHasNext.test(
                                aicApiResult.pagination().currentPage(),
                                aicApiResult.pagination().totalPages()
                        )
                )
                .hasPrevious(
                        PaginatedArtworkResultsDTO.checkHasPrevious.test(
                                aicApiResult.pagination().currentPage()
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
