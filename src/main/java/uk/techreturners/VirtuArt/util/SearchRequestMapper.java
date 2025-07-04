package uk.techreturners.VirtuArt.util;

import uk.techreturners.VirtuArt.model.aicapi.AicApiElasticSearchQuery;
import uk.techreturners.VirtuArt.model.request.AdvancedSearchRequest;
import uk.techreturners.VirtuArt.model.request.BasicSearchRequest;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public interface SearchRequestMapper {
    default AicApiElasticSearchQuery createAicAdvancedElasticQuery(AdvancedSearchRequest request) {
        // Must clauses list
        List<Map<String, Object>> mustClauses = new ArrayList<>();

        // Add title clause if not blank
        if (request.title() != null && !request.title().isBlank()) {
            Map<String, Object> titleMatch = new HashMap<>(Map.of("title", request.title().trim()));
            Map<String, Object> titleClause = new HashMap<>(Map.of("match", titleMatch));
            mustClauses.add(titleClause);
        }

        // Add artist clause if not blank
        if (request.artist() != null && !request.artist().isBlank()) {
            Map<String, Object> artistMatch = new HashMap<>(Map.of("artist_title", request.artist().trim()));
            Map<String, Object> artistClause = new HashMap<>(Map.of("match", artistMatch));
            mustClauses.add(artistClause);
        }

        // Add medium clause if not blank
        if (request.medium() != null && !request.medium().isBlank()) {
            Map<String, Object> mediumMatch = new HashMap<>(Map.of("medium_display", request.medium().trim()));
            Map<String, Object> mediumClause = new HashMap<>(Map.of("match", mediumMatch));
            mustClauses.add(mediumClause);
        }

        // Add department clause if not blank
        if (request.department() != null && !request.department().isBlank()) {
            Map<String, Object> departmentMatch = new HashMap<>(
                    Map.of("department_title", request.department().trim())
            );
            Map<String, Object> departmentClause = new HashMap<>(Map.of("match", departmentMatch));
            mustClauses.add(departmentClause);
        }

        // Add public domain term clause
        Map<String, Object> publicDomainTerm = new HashMap<>(Map.of("is_public_domain", true));
        Map<String, Object> publicDomainClause = new HashMap<>(Map.of("term", publicDomainTerm));
        mustClauses.add(publicDomainClause);

        // Build the bool query structure
        Map<String, Object> boolQuery = new HashMap<>(Map.of("must", mustClauses));
        Map<String, Object> queryMap = new HashMap<>(Map.of("bool", boolQuery));

        // Sort clauses
        List<Map<String, Object>> sortClauses = new ArrayList<>();

        if (request.source() != null && !request.sortBy().isBlank() && request.sortOrder() != null && !request.sortOrder().isBlank()) {
            String sortBy;
            switch (request.sortBy().toLowerCase()) {
                case "artist" -> sortBy = "artist_tile.keyword";
                case "date" -> sortBy = "date_end";
                default -> sortBy = "title.keyword";
            }

            String sorOrderStr = (request.sortOrder().equalsIgnoreCase("ascending")) ? "asc" : "desc";

            Map<String, Object> sortOrder = new HashMap<>(Map.of("order", sorOrderStr));
            Map<String, Object> sortClause = new HashMap<>(Map.of(sortBy, sortOrder));
            sortClauses.add(sortClause);
        }

        // Create the final Elasticsearch query
        AicApiElasticSearchQuery elasticsearchQuery = new AicApiElasticSearchQuery();
        elasticsearchQuery.setQuery(queryMap);
        elasticsearchQuery.setSort(sortClauses);
        elasticsearchQuery.setSize(request.pageSize());
        elasticsearchQuery.setPage(request.currentPage());

        System.out.println(elasticsearchQuery);
        return elasticsearchQuery;
    }

    default AicApiElasticSearchQuery createBasicElasticQuery(BasicSearchRequest request) {
        AicApiElasticSearchQuery elasticSearchQuery = new AicApiElasticSearchQuery();

        if (request.query() != null && !request.query().isBlank()) {
            elasticSearchQuery.setQ(request.query().trim());
        }

        // Add public domain term clause
        Map<String, Object> publicDomainTerm = new HashMap<>(Map.of("is_public_domain", true));
        Map<String, Object> publicDomainClause = new HashMap<>(Map.of("term", publicDomainTerm));

        elasticSearchQuery.setQuery(publicDomainClause);
        elasticSearchQuery.setSize(request.pageSize());
        elasticSearchQuery.setPage(request.currentPage());

        return elasticSearchQuery;
    }
}