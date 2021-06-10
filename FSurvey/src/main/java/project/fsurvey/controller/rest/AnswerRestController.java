package project.fsurvey.controller.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import project.fsurvey.business.abstracts.AnswerService;
import project.fsurvey.core.results.DataResult;
import project.fsurvey.core.results.ErrorDataResult;
import project.fsurvey.core.results.SuccessDataResult;
import project.fsurvey.dtos.AnswerDto;
import project.fsurvey.entities.concretes.survey.Answer;
import project.fsurvey.entities.concretes.users.Participant;

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
    @PreAuthorize("hasAuthority('read:answer')")
    public ResponseEntity<DataResult<Answer>> findById(@PathVariable("id") Long id){
        DataResult<Answer> result = answerService.findById(id).getBody();

        if(!result.isSuccess())
            return ResponseEntity.badRequest().body(result);

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(authentication.getPrincipal() instanceof Participant){
            Long answerParticipantId = result.getData().getParticipant().getId();
            Long authenticatedUserId = ((Participant) authentication.getPrincipal()).getId();
            if(!answerParticipantId.equals(authenticatedUserId)){
                return ResponseEntity.badRequest().body(new ErrorDataResult<>("You can't read answer that don't belong to you."));
            }
        }
        return ResponseEntity.ok(new SuccessDataResult<>(result.getData()));
    }

    @PostMapping("/add")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_PARTICIPANT')")
    public ResponseEntity<DataResult<Answer>> add(@RequestBody @Valid AnswerDto answerDto){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(authentication.getPrincipal() instanceof Participant){
            Long authenticatedParticipantId = ((Participant) authentication.getPrincipal()).getId();
            if(!authenticatedParticipantId.equals(answerDto.getParticipantId()))
                return ResponseEntity.badRequest().body(new ErrorDataResult<>("Your id didn't match"));
        }
        return answerService.add(answerDto);
    }

    @GetMapping("/find-by-participant-id/{id}")
    @PreAuthorize("hasAuthority('read:answer')")
    public ResponseEntity<DataResult<List<Answer>>> findByParticipantId(@PathVariable("id") Long id){
        DataResult<List<Answer>> result = answerService.findByParticipantId(id).getBody();

        if(!result.isSuccess())
            return ResponseEntity.badRequest().body(result);

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if(authentication.getPrincipal() instanceof Participant){
            Long loggedParticipantId = ((Participant) authentication.getPrincipal()).getId();
            boolean checkAnswers = result.getData().stream()
                    .anyMatch(answer -> !answer.getParticipant().getId().equals(loggedParticipantId));
            if(checkAnswers)
                return ResponseEntity.badRequest().body(new ErrorDataResult<>("You can't read answers that don't belongs to you."));
        }
        return ResponseEntity.ok(result);
    }

    @GetMapping("/find-by-survey-id/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<DataResult<List<Answer>>> findBySurveyId(@PathVariable("id") Long id){
        return answerService.findBySurveyId(id);
    }

    @GetMapping("/find-by-issue-id/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<DataResult<List<Answer>>> findByIssueId(@PathVariable("id") Long id){
        return answerService.findByIssueId(id);
    }

    @GetMapping("/find-by-option-id/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<DataResult<List<Answer>>> findByOptionId(@PathVariable("id") Long id){
        return answerService.findByOptionId(id);
    }

    @PutMapping("/update/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<DataResult<Answer>> update(@PathVariable("id") Long id,
                                                     @RequestBody AnswerDto answerDto){
        return answerService.update(id, answerDto);
    }
}
