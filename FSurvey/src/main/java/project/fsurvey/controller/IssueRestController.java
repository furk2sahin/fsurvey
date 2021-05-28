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
import project.fsurvey.core.exception.NotFoundException;
import project.fsurvey.core.exception.ParameterException;

import java.util.List;

@RestController
@RequestMapping("/api/v1/issue")
public class IssueRestController {

    private IssueService issueService;
    private SurveyService surveyService;
    private OptionService optionService;

    @Autowired
    public IssueRestController(IssueService issueService, SurveyService surveyService, OptionService optionService) {
        this.issueService = issueService;
        this.surveyService = surveyService;
        this.optionService = optionService;
    }

    @GetMapping("/find-by-id/{id}")
    public ResponseEntity<Object> findById(@PathVariable("id") Long id){
        try{
            return ResponseEntity.ok(issueService.findById(id));
        } catch (NotFoundException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Issue not found with given id.");
        }
    }

    @PostMapping("/add/{surveyId}")
    public ResponseEntity<Object> add(@PathVariable("surveyId") Long surveyId, @RequestBody Issue issue){
        try{
            issue.setSurvey(surveyService.findById(surveyId));
            Issue addedIssue = issueService.add(issue);
            optionService.addAll(issue.getId(), issue.getOptions());
            return ResponseEntity.ok(addedIssue);
        } catch (ParameterException e){
            return ResponseEntity.status(HttpStatus.PRECONDITION_FAILED).body(e.getMessage());
        } catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error when creating Issue");
        }
    }

    @PostMapping("/add-all/{surveyId}")
    public ResponseEntity<Object> addAll(@PathVariable("surveyId") Long surveyId, @RequestBody @Valid List<Issue> issues){
        try{
            List<Issue> savedIssues = issueService.addAll(surveyId, issues);
            issues.forEach(issue -> optionService.addAll(issue.getId(), issue.getOptions()));
            return ResponseEntity.ok(savedIssues);
        } catch (ParameterException e){
            return ResponseEntity.status(HttpStatus.PRECONDITION_FAILED).body(e.getMessage());
        } catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error when creating Issue");
        }
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<Object> update(@PathVariable("id") Long id, @RequestBody @Valid Issue issue){
        try{
            return ResponseEntity.ok(issueService.update(id, issue));
        } catch (NotFoundException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Issue not found with given id.");
        }
    }

    @GetMapping("/find-by-survey-id/{id}")
    public ResponseEntity<Object> findBySurveyId(@PathVariable("id") Long id){
        return ResponseEntity.ok(issueService.findBySurveyId(id));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> delete(@PathVariable("id") Long id){
        try{
            issueService.delete(id);
            return ResponseEntity.ok("Issue successfully deleted.");
        } catch (EmptyResultDataAccessException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Issue not found with given id");
        }
    }
}
