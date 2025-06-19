package uk.techreturners.VirtuArt.model.cmaapi;

public record CmaApiImages(
        CmaApiWeb web
) {
    public record CmaApiWeb(
            String url
    ) {
    }
}