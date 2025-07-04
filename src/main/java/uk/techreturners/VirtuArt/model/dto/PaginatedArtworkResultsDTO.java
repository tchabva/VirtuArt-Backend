package uk.techreturners.VirtuArt.model.dto;

import lombok.*;

import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class PaginatedArtworkResultsDTO {
    private Integer totalItems;
    private Integer pageSize;
    private Integer totalPages;
    private Integer currentPage;
    private Boolean hasNext;
    private Boolean hasPrevious;
    @Builder.Default
    private List<ArtworkResultsDTO> data = new ArrayList<>();
}