package project.fsurvey.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import project.fsurvey.business.abstracts.OptionService;
import project.fsurvey.entities.concretes.survey.Option;
import project.fsurvey.core.exception.NotFoundException;
import project.fsurvey.core.exception.ParameterException;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/v1/option")
public class OptionRestController {

    private OptionService optionService;

    @Autowired
    public OptionRestController(OptionService optionService) {
        this.optionService = optionService;
    }

    @GetMapping("/find-by-id/{id}")
    public ResponseEntity<Object> findById(@PathVariable("id") Long id){
        try{
            return ResponseEntity.ok(optionService.findById(id));
        } catch (NotFoundException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Option not found with given id.");
        }
    }

    @PostMapping("/add/{issueId}")
    public ResponseEntity<Object> add(@PathVariable("issueId") Long issueId, @RequestBody @Valid Option option){
        try{
            return ResponseEntity.ok(optionService.add(issueId, option));
        } catch (ParameterException e){
            return ResponseEntity.status(HttpStatus.PRECONDITION_FAILED).body(e.getMessage());
        } catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error when creating Issue");
        }
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<Object> update(@PathVariable("id") Long id, @RequestBody @Valid Option option){
        try{
            return ResponseEntity.ok(optionService.update(id, option));
        } catch (NotFoundException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Survey not found with given id.");
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> delete(@PathVariable("id") Long id){
        try{
            optionService.delete(id);
            return ResponseEntity.ok("Option successfully deleted.");
        } catch (EmptyResultDataAccessException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Option not found with given id");
        }
    }

    @GetMapping("/find-by-issue-id/{id}")
    public ResponseEntity<Object> findByIssueId(@PathVariable("id") Long id){
        return ResponseEntity.ok(optionService.findByIssueId(id));
    }
}
