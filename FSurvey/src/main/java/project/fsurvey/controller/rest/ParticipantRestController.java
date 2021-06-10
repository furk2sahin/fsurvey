package project.fsurvey.controller.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import project.fsurvey.business.abstracts.ParticipantService;
import project.fsurvey.core.results.DataResult;
import project.fsurvey.core.results.ErrorDataResult;
import project.fsurvey.core.results.Result;
import project.fsurvey.dtos.UserDto;
import project.fsurvey.dtos.UserGetDto;
import project.fsurvey.entities.concretes.users.Participant;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/v1/participant")
public class ParticipantRestController {

    private ParticipantService participantService;

    @Autowired
    public ParticipantRestController(ParticipantService participantService) {
        this.participantService = participantService;
    }

    @GetMapping("/find-by-id/{id}")
    @PreAuthorize("hasAnyRole('ROLE_aDMIN', 'ROLE_PARTICIPANT')")
    public ResponseEntity<DataResult<UserGetDto>> findById(@PathVariable("id") Long id){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(authentication.getPrincipal() instanceof Participant){
            Long principalId = ((Participant) authentication.getPrincipal()).getId();
            if(!principalId.equals(id))
                return ResponseEntity.badRequest().body(new ErrorDataResult<>("You can't see another participant's information"));
        }
        return participantService.findById(id);

    }

    @PostMapping("/add")
    public ResponseEntity<DataResult<UserGetDto>> add(@RequestBody @Valid UserDto participant){
        return participantService.add(participant);
    }

    @PutMapping("/update/{id}")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_PARTICIPANT')")
    public ResponseEntity<DataResult<UserGetDto>> update(@PathVariable("id") Long id,
                                                        @RequestBody UserDto participant){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(authentication.getPrincipal() instanceof Participant){
            Long principalId = ((Participant) authentication.getPrincipal()).getId();
            if(!principalId.equals(id))
                return ResponseEntity.badRequest().body(new ErrorDataResult<>("You can't update another participant"));
        }
        return participantService.update(id, participant);
    }

    @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_PARTICIPANT')")
    public ResponseEntity<Result> delete(@PathVariable("id") Long id){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(authentication.getPrincipal() instanceof Participant){
            Long principalId = ((Participant) authentication.getPrincipal()).getId();
            if(!principalId.equals(id))
                return ResponseEntity.badRequest().body(new ErrorDataResult<>("You can't delete another participant"));
        }
        return participantService.delete(id);
    }
}