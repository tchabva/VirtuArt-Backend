package uk.techreturners.VirtuArt.model.cmaapi;

public record CmaApiCreators(
        String description
) {
    // Returns the artist's name
    @Override
    public String description() {
        return description.replaceAll("\\s*\\([^)]+\\)$", "");
    }
}