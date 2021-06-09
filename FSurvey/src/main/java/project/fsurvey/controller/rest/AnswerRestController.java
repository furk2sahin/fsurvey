package project.fsurvey.controller.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import project.fsurvey.business.abstracts.AnswerService;
import project.fsurvey.core.results.DataResult;
import project.fsurvey.dtos.AnswerDto;
import project.fsurvey.entities.concretes.survey.Answer;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/v1/answer")
public class AnswerRestController {

    private AnswerService answerService;

    @Autowired
    public AnswerRestController(AnswerService answerService) {
        this.answerService = answerService;
    }

    @GetMapping("/find-by-id/{id}")
    public ResponseEntity<DataResult<Answer>> findById(@PathVariable("id") Long id){
        return answerService.findById(id);
    }

    @PostMapping("/add")
    public ResponseEntity<DataResult<Answer>> add(@RequestBody @Valid AnswerDto answerDto){
        return answerService.add(answerDto);
    }

    @GetMapping("/find-by-participant-id/{id}")
    public ResponseEntity<DataResult<List<Answer>>> findByParticipantId(@PathVariable("id") Long id){
        return answerService.findByParticipantId(id);
    }

    @GetMapping("/find-by-survey-id/{id}")
    public ResponseEntity<DataResult<List<Answer>>> findBySurveyId(@PathVariable("id") Long id){
        return answerService.findBySurveyId(id);
    }

    @GetMapping("/find-by-issue-id/{id}")
    public ResponseEntity<DataResult<List<Answer>>> findByIssueId(@PathVariable("id") Long id){
        return answerService.findByIssueId(id);
    }

    @GetMapping("/find-by-option-id/{id}")
    public ResponseEntity<DataResult<List<Answer>>> findByOptionId(@PathVariable("id") Long id){
        return answerService.findByOptionId(id);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<DataResult<Answer>> update(@PathVariable("id") Long id,
                                                     @RequestBody AnswerDto answerDto){
        return answerService.update(id, answerDto);
    }
}
