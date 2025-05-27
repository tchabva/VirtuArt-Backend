package uk.techreturners.VirtuArt.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import uk.techreturners.VirtuArt.model.dto.PaginatedArtworkResultsDTO;
import uk.techreturners.VirtuArt.service.ArtworkService;

@RestController
@RequestMapping("api/v1/artworks")
public class ArtworkController {

    @Autowired
    private ArtworkService artworkService;

    @GetMapping()
    public ResponseEntity<PaginatedArtworkResultsDTO> getArtworks(
            @RequestParam(value = "limit", defaultValue = "50") String limit,
            @RequestParam(value = "page", defaultValue = "1") String page
    ) {
        PaginatedArtworkResultsDTO paginatedArtworkResultsDTO = artworkService.getAicArtworks(limit, page);
        return new ResponseEntity<>(paginatedArtworkResultsDTO, HttpStatus.OK);
    }
}