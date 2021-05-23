package project.fsurvey.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import project.fsurvey.business.abstracts.IssueService;
import project.fsurvey.business.abstracts.OptionService;
import project.fsurvey.business.abstracts.SurveyService;
import project.fsurvey.entities.concretes.survey.Issue;
import project.fsurvey.entities.concretes.survey.Survey;
import project.fsurvey.exception.NotFoundException;
import project.fsurvey.exception.ParameterException;

import java.util.List;

@RestController
@RequestMapping("/api/v1/survey")
public class SurveyRestController {

    private SurveyService surveyService;
    private IssueService issueService;
    private OptionService optionService;

    @Autowired
    public SurveyRestController(SurveyService surveyService, IssueService issueService, OptionService optionService) {
        this.surveyService = surveyService;
        this.issueService = issueService;
        this.optionService = optionService;
    }

    @GetMapping("/find-by-id/{id}")
    public ResponseEntity<Object> findById(@PathVariable("id") Long id){
        try{
            return ResponseEntity.ok(surveyService.findById(id));
        } catch (NotFoundException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Survey not found with given id.");
        }
    }

    @PostMapping("/add")
    public ResponseEntity<Object> add(@RequestBody Survey survey){
        try{
            Survey addedSurvey = surveyService.add(survey);
            List<Issue> issues = issueService.addAll(addedSurvey.getId(), survey.getIssues());
            issues.forEach(issue -> optionService.addAll(issue.getId(), issue.getOptions()));
            return ResponseEntity.ok(addedSurvey);
        } catch (ParameterException e){
            return ResponseEntity.status(HttpStatus.PRECONDITION_FAILED).body(e.getMessage());
        } catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error when creating Survey");
        }
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<Object> update(@PathVariable("id") Long id, @RequestBody Survey survey){
        try{
            return ResponseEntity.ok(surveyService.update(id, survey));
        } catch (NotFoundException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Survey not found with given id.");
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> delete(@PathVariable("id") Long id){
        try{
            surveyService.delete(id);
            return ResponseEntity.ok("Survey successfully deleted.");
        } catch (EmptyResultDataAccessException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Survey not found with given id");
        }
    }
}