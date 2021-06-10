package project.fsurvey.controller.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import project.fsurvey.business.abstracts.SurveyService;
import project.fsurvey.core.results.DataResult;
import project.fsurvey.core.results.Result;
import project.fsurvey.dtos.SurveyDto;
import project.fsurvey.entities.concretes.survey.Survey;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/v1/survey")
public class SurveyRestController {

    private SurveyService surveyService;

    @Autowired
    public SurveyRestController(SurveyService surveyService) {
        this.surveyService = surveyService;
    }

    @GetMapping("/find-by-id/{id}")
    @PreAuthorize("hasAuthority('read:survey')")
    public ResponseEntity<DataResult<Survey>> findById(@PathVariable("id") Long id){
        return surveyService.findById(id);
    }

    @GetMapping("/get-all")
    @PreAuthorize("hasAuthority('read:survey')")
    public ResponseEntity<DataResult<List<Survey>>> findAll(){
        return surveyService.findAll();
    }

    @PostMapping("/add")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<DataResult<Survey>> add(@RequestBody @Valid SurveyDto survey){
        return surveyService.add(survey);
    }

    @PutMapping("/update/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<DataResult<Survey>> update(@PathVariable("id") Long id,
                                         @RequestBody SurveyDto survey){
        return surveyService.update(id, survey);
    }

    @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<Result> delete(@PathVariable("id") Long id){
        return surveyService.delete(id);
    }
}