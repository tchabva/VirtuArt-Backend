package uk.techreturners.VirtuArt.model.request;

import lombok.Builder;

@Builder
public record AdvancedSearchRequest(
      String title,
      String artist,
      String medium,
      String department,
      String sortBy,
      String sortOrder,
      String pageSize,
      String currentPage
) {
}
