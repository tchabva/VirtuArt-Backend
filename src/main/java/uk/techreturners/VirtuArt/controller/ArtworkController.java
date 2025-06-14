package uk.techreturners.VirtuArt.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uk.techreturners.VirtuArt.model.dto.ArtworkDTO;
import uk.techreturners.VirtuArt.model.dto.PaginatedArtworkResultsDTO;
import uk.techreturners.VirtuArt.model.request.AdvancedSearchRequest;
import uk.techreturners.VirtuArt.model.request.BasicSearchRequest;
import uk.techreturners.VirtuArt.service.ArtworkService;

@RestController
@RequestMapping("api/v1/artworks")
public class ArtworkController {

    @Autowired
    private ArtworkService artworkService;

    @GetMapping(path = "/{source}")
    public ResponseEntity<PaginatedArtworkResultsDTO> getArtworks(
            @PathVariable("source") String source,
            @RequestParam(value = "limit", defaultValue = "25") String limit,
            @RequestParam(value = "page", defaultValue = "1") String page
    ) {
        PaginatedArtworkResultsDTO paginatedArtworkResultsDTO = artworkService.getArtworks(source, limit, page);
        return new ResponseEntity<>(paginatedArtworkResultsDTO, HttpStatus.OK);
    }

    @GetMapping(path = "/{source}/{id}")
    public ResponseEntity<ArtworkDTO> getArtworkById(
            @PathVariable("source") String source,
            @PathVariable("id") String artworkId) {
        return new ResponseEntity<>(artworkService.getArtworkById(source, artworkId), HttpStatus.OK);
    }

    @PostMapping("/search")
    public ResponseEntity<PaginatedArtworkResultsDTO> basicApiSearch(@RequestBody BasicSearchRequest searchQuery) {
        return new ResponseEntity<>(artworkService.getArtworksByBasicSearchQuery(searchQuery), HttpStatus.OK);
    }

    @PostMapping("/search/advanced")
    public ResponseEntity<PaginatedArtworkResultsDTO> advancedApiSearch(@RequestBody AdvancedSearchRequest searchQuery) {
        return new ResponseEntity<>(artworkService.getArtworksByAdvancedSearchQuery(searchQuery), HttpStatus.OK);
    }
}