package uk.techreturners.VirtuArt.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uk.techreturners.VirtuArt.model.dto.ArtworkDTO;
import uk.techreturners.VirtuArt.model.dto.PaginatedArtworkResultsDTO;
import uk.techreturners.VirtuArt.service.ArtworkService;

@RestController
@RequestMapping("api/v1/artworks")
public class ArtworkController {

    @Autowired
    private ArtworkService artworkService;

    @GetMapping("/aic")
    public ResponseEntity<PaginatedArtworkResultsDTO> getAicArtworks(
            @RequestParam(value = "limit", defaultValue = "50") String limit,
            @RequestParam(value = "page", defaultValue = "1") String page
    ) {
        PaginatedArtworkResultsDTO paginatedArtworkResultsDTO = artworkService.getAicArtworks(limit, page);
        return new ResponseEntity<>(paginatedArtworkResultsDTO, HttpStatus.OK);
    }

    @GetMapping(path = "/{source}/{id}")
    public ResponseEntity<ArtworkDTO> getArtworkById(
            @PathVariable("source") String source,
            @PathVariable("id") String artworkId) {
        return new ResponseEntity<>(artworkService.getArtworkById(source, artworkId), HttpStatus.OK);
    }
}