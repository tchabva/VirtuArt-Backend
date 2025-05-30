package uk.techreturners.VirtuArt.model.aicapi;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.List;
import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AicApiElasticSearchQuery {
    @JsonProperty("query")
    private Query query;

    @JsonProperty("sort")
    private List<Map<String, Object>> sort;

    @JsonProperty("fields")
    private final List<String> fields = List.of("id", "title", "date_display", "artist_title");

    @JsonProperty("size")
    private int size;

    @JsonProperty("page")
    private int page;


    // Inner classes for query structure
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class Query {
        @JsonProperty("bool")
        private BoolQuery bool;

    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class BoolQuery {
        @JsonProperty("must")
        private List<Map<String, Object>> must;

    }

    @Override
    public String toString() {
        return "ElasticsearchQuery{" +
                "query=" + query +
                ", sort=" + sort +
                ", fields=" + fields +
                ", size=" + size +
                ", page=" + page +
                '}';
    }
}