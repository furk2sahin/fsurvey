package project.fsurvey.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import project.fsurvey.business.abstracts.AnswerService;
import project.fsurvey.entities.concretes.survey.Answer;
import project.fsurvey.entities.concretes.survey.Issue;
import project.fsurvey.entities.concretes.survey.Option;
import project.fsurvey.exception.NotFoundException;
import project.fsurvey.exception.ParameterException;

@RestController
@RequestMapping("/api/v1/answer")
public class AnswerRestController {

    private AnswerService answerService;

    @Autowired
    public AnswerRestController(AnswerService answerService) {
        this.answerService = answerService;
    }

    @GetMapping("/find-by-id/{id}")
    public ResponseEntity<Object> findById(@PathVariable("id") Long id){
        try{
            return ResponseEntity.ok(answerService.findById(id));
        } catch (NotFoundException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Answer not found with given id.");
        }
    }

    @PostMapping("/add")
    public ResponseEntity<Object> add(@RequestBody Answer answer){
        try{
            return ResponseEntity.ok(answerService.add(answer));
        } catch (ParameterException | NotFoundException e){
            return ResponseEntity.status(HttpStatus.PRECONDITION_FAILED).body(e.getMessage());
        } catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error when creating Answer");
        }
    }

    @GetMapping("/find-by-participant-id/{id}")
    public ResponseEntity<Object> findByParticipantId(@PathVariable("id") Long id){
        return ResponseEntity.ok(answerService.findByParticipantId(id));
    }

    @GetMapping("/find-by-survey-id/{id}")
    public ResponseEntity<Object> findBySurveyId(@PathVariable("id") Long id){
        return ResponseEntity.ok(answerService.findBySurveyId(id));
    }

    @GetMapping("/find-by-issue-id/{id}")
    public ResponseEntity<Object> findByIssueId(@PathVariable("id") Long id){
        return ResponseEntity.ok(answerService.findByIssueId(id));
    }

    @GetMapping("/find-by-option-id/{id}")
    public ResponseEntity<Object> findByOptionId(@PathVariable("id") Long id){
        return ResponseEntity.ok(answerService.findByOptionId(id));
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<Object> update(@PathVariable("id") Long id, @RequestBody Answer answer){
        try{
            return ResponseEntity.ok(answerService.update(id, answer));
        }  catch (ParameterException | NotFoundException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }
}
