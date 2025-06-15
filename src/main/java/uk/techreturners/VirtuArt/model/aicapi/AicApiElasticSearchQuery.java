package uk.techreturners.VirtuArt.model.aicapi;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AicApiElasticSearchQuery {

    @JsonProperty("q")
    private String q;

    @JsonProperty("query")
    private Map<String, Object> query;

    @JsonProperty("sort")
    private List<Map<String, Object>> sort;

    @JsonProperty("fields")
    private final List<String> fields = List.of(
            "id", "title", "date_display", "artist_title", "image_id"
    );

    @JsonProperty("size")
    private int size;

    @JsonProperty("page")
    private int page;

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