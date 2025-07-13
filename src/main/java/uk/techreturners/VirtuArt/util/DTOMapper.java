package uk.techreturners.VirtuArt.util;

import uk.techreturners.VirtuArt.exception.ItemNotFoundException;
import uk.techreturners.VirtuArt.model.Exhibition;
import uk.techreturners.VirtuArt.model.ExhibitionItem;
import uk.techreturners.VirtuArt.model.aicapi.AicApiArtwork;
import uk.techreturners.VirtuArt.model.aicapi.AicApiSearchArtwork;
import uk.techreturners.VirtuArt.model.aicapi.AicApiSearchResult;
import uk.techreturners.VirtuArt.model.cmaapi.CmaApiArtwork;
import uk.techreturners.VirtuArt.model.cmaapi.CmaApiSearchResult;
import uk.techreturners.VirtuArt.model.cmaapi.CmaApiSearchResult.CmaApiSearchArtwork;
import uk.techreturners.VirtuArt.model.dto.*;

// DTO Mapper Interface
public interface DTOMapper {
    default ArtworkDTO createArtworkDtoWithAicApi(AicApiArtwork aicApiArtwork) {
        if (aicApiArtwork == null) {
            throw new ItemNotFoundException("Null response from Art Institute of Chicago API");
        } else {
            return new ArtworkDTO(
                    aicApiArtwork.id().toString(),
                    aicApiArtwork.title(),
                    aicApiArtwork.artist(),
                    aicApiArtwork.dateDisplay(),
                    aicApiArtwork.displayMedium(),
                    aicImageUrlCreator(aicApiArtwork.primaryImageId()),
                    aicApiArtwork.altImageIds().stream()
                            .map(this::aicImageUrlCreator)
                            .toList(),
                    aicApiArtwork.description(),
                    aicApiArtwork.origin(),
                    aicApiArtwork.department(),
                    "Art Institute of Chicago"
            );
        }
    }

    default ArtworkDTO createArtworkDtoWithCmaApi(CmaApiArtwork cmaApiArtwork) {
        if (cmaApiArtwork == null) {
            throw new ItemNotFoundException("Null response from Cleveland Museum of Art API");
        } else {
            return new ArtworkDTO(
                    cmaApiArtwork.id().toString(),
                    cmaApiArtwork.title(),
                    cmaApiArtwork.creators() != null && !cmaApiArtwork.creators().isEmpty()
                            ? cmaApiArtwork.creators().getFirst().description()
                            : null,
                    cmaApiArtwork.creationDate(),
                    cmaApiArtwork.technique(),
                    cmaApiArtwork.images().web() != null
                            ? cmaApiArtwork.images().web().url()
                            : null,
                    cmaApiArtwork.alternateImages().stream()
                            .map(cmaApiAlternateImages -> cmaApiAlternateImages.web().url())
                            .toList(),
                    cmaApiArtwork.description(),
                    cmaApiArtwork.culture() != null && !cmaApiArtwork.culture().isEmpty()
                            ? cmaApiArtwork.culture().getFirst()
                            : null,
                    cmaApiArtwork.department(),
                    "Cleveland Museum of Art"
            );
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
            throw new ItemNotFoundException("Null response from Art Institute of Chicago API");
        } else if (aicApiSearchResult.pagination() == null) {
            throw new ItemNotFoundException("Null pagination response from Art Institute of Chicago API");
        } else if (aicApiSearchResult.data() == null) {
            throw new ItemNotFoundException("Null data response from Art Institute of Chicago API");
        } else {
            return new PaginatedArtworkResultsDTO(
                    aicApiSearchResult.pagination().total(),
                    aicApiSearchResult.pagination().limit(),
                    aicApiSearchResult.pagination().totalPages(),
                    aicApiSearchResult.pagination().currentPage(),
                    aicApiSearchResult.pagination().checkHasNext(),
                    aicApiSearchResult.pagination().checkHasPrevious(),
                    aicApiSearchResult.data().stream()
                            .map(this::aicSearchArtworkResultsResponseMapper)
                            .toList()
            );
        }
    }

    default PaginatedArtworkResultsDTO cmaPaginatedResponseMapper(CmaApiSearchResult result) {
        if (result == null) {
            throw new ItemNotFoundException("Null response from Cleveland Museum of Art");
        } else if (result.info() == null) {
            throw new ItemNotFoundException("Null pagination response from Cleveland Museum of Art");
        } else if (result.data() == null) {
            throw new ItemNotFoundException("Null data response from Cleveland Museum of Art");
        } else {
            return new PaginatedArtworkResultsDTO(
                    result.info().total(),
                    result.info().parameters().limit(),
                    result.info().parameters().calculateTotalPages(result.info().total()),
                    result.info().parameters().calculateCurrentPage(),
                    result.info().checkHasNext(),
                    result.info().checkHasPrevious(),
                    result.data().stream()
                            .map(this::cmaSearchArtworkResultsResponseMapper)
                            .toList()
            );
        }
    }

    default ArtworkResultsDTO aicSearchArtworkResultsResponseMapper(AicApiSearchArtwork aicArtworkSearchResult) {
        if (aicArtworkSearchResult == null) {
            throw new ItemNotFoundException("Null response from Art Institute of Chicago API");
        } else {
            return new ArtworkResultsDTO(
                    aicArtworkSearchResult.id().toString(),
                    aicArtworkSearchResult.title(),
                    aicArtworkSearchResult.artistTitle(),
                    aicArtworkSearchResult.dateDisplay(),
                    aicImageUrlCreator(aicArtworkSearchResult.primaryImageId()),
                    "aic"
            );
        }
    }

    default ArtworkResultsDTO cmaSearchArtworkResultsResponseMapper(CmaApiSearchArtwork artworkResult) {
        if (artworkResult == null) {
            throw new ItemNotFoundException("Null response from Cleveland Museum of Art");
        } else {
            return new ArtworkResultsDTO(
                    artworkResult.id().toString(),
                    artworkResult.title(),
                    artworkResult.creators() != null && !artworkResult.creators().isEmpty()
                            ? artworkResult.creators().getFirst().description()
                            : null,
                    artworkResult.creationDate(),
                    artworkResult.images().web() != null
                            ? artworkResult.images().web().url()
                            : null,
                    "cma"
            );
        }
    }

    default ExhibitionDTO createExhibitionDTO(Exhibition exhibition) {
        if (exhibition == null) {
            throw new ItemNotFoundException("Exhibition Item could not be found");
        } else {
            return new ExhibitionDTO(
                    exhibition.getId(),
                    exhibition.getTitle(),
                    exhibition.getExhibitionItems().size(),
                    exhibition.getCreatedAt(),
                    exhibition.getUpdatedAt()
            );
        }
    }

    default ExhibitionDetailDTO createExhibitionDetailDTO(Exhibition exhibition) {
        if (exhibition == null) {
            throw new ItemNotFoundException("Exhibition Item could not be found");
        } else {
            return new ExhibitionDetailDTO(
                    exhibition.getId(),
                    exhibition.getTitle(),
                    exhibition.getDescription(),
                    exhibition.getCreatedAt(),
                    exhibition.getUpdatedAt(),
                    exhibition.getExhibitionItems().stream().map(this::createExhibitionItemDTO).toList()
            );
        }
    }

    default ExhibitionItemDTO createExhibitionItemDTO(ExhibitionItem exhibitionItem) {
        if (exhibitionItem == null) {
            throw new ItemNotFoundException("Exhibition Item could not be found");
        } else {
            return new ExhibitionItemDTO(
                    exhibitionItem.getId(),
                    exhibitionItem.getApiId(),
                    exhibitionItem.getTitle(),
                    exhibitionItem.getArtistTitle(),
                    exhibitionItem.getDate(),
                    exhibitionItem.getImageUrl(),
                    exhibitionItem.getSource()
            );
        }
    }
}