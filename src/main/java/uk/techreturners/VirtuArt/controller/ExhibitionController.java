package uk.techreturners.VirtuArt.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uk.techreturners.VirtuArt.model.dto.ExhibitionDTO;
import uk.techreturners.VirtuArt.model.request.CreateExhibitionRequest;
import uk.techreturners.VirtuArt.service.ExhibitionService;

import java.util.List;

@RestController
@RequestMapping("api/v1/exhibitions")
public class ExhibitionController {

    @Autowired
    private ExhibitionService exhibitionService;

    @GetMapping
    public ResponseEntity<List<ExhibitionDTO>> getUserExhibitions() {
        return new ResponseEntity<>(exhibitionService.getAllUserExhibitions(), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<ExhibitionDTO> createExhibition(@RequestBody CreateExhibitionRequest request){
        return new ResponseEntity<>(exhibitionService.createUserExhibition(request), HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteExhibition(@PathVariable("id") String exhibitId){
        return new ResponseEntity<>(exhibitionService.deleteExhibition(exhibitId), HttpStatus.NO_CONTENT);
    }
}