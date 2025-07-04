package uk.techreturners.VirtuArt.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import uk.techreturners.VirtuArt.model.dto.PaginatedArtworkResultsDTO;
import uk.techreturners.VirtuArt.model.request.AdvancedSearchRequest;
import uk.techreturners.VirtuArt.model.request.BasicSearchRequest;
import uk.techreturners.VirtuArt.service.SearchService;

@RestController
@RequestMapping("api/v1/search")
public class SearchController {

    @Autowired
    private SearchService searchService;

    @PostMapping
    public ResponseEntity<PaginatedArtworkResultsDTO> basicApiSearch(@RequestBody BasicSearchRequest searchQuery) {
        return new ResponseEntity<>(searchService.getArtworksByBasicSearchQuery(searchQuery), HttpStatus.OK);
    }

    @PostMapping("/advanced")
    public ResponseEntity<PaginatedArtworkResultsDTO> advancedApiSearch(@RequestBody AdvancedSearchRequest searchQuery) {
        return new ResponseEntity<>(searchService.getArtworksByAdvancedSearchQuery(searchQuery), HttpStatus.OK);
    }
}