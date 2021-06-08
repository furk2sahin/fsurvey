package project.fsurvey.controller.rest;

import com.google.common.base.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import project.fsurvey.business.abstracts.ParticipantService;
import project.fsurvey.core.results.DataResult;
import project.fsurvey.core.results.Result;
import project.fsurvey.dtos.UserDto;
import project.fsurvey.dtos.UserGetDto;
import project.fsurvey.entities.concretes.users.Participant;
import project.fsurvey.core.util.RoleParser;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/v1/participant")
public class ParticipantRestController {

    private ParticipantService participantService;

    @Autowired
    public ParticipantRestController(ParticipantService participantService) {
        this.participantService = participantService;
    }

    @GetMapping("/find-by-uuid/{id}")
    public ResponseEntity<DataResult<UserGetDto>> findById(@PathVariable("id") Long id){
        return participantService.findById(id);

    }

    @PostMapping("/add")
    public ResponseEntity<DataResult<UserGetDto>> add(@RequestBody @Valid UserDto participantDto){
        return participantService.add(participantDto);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<DataResult<UserGetDto>> update(@PathVariable("id") Long id,
                                         @RequestBody @Valid UserDto participantDto){
         return participantService.update(id, participantDto);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Result> delete(@PathVariable("id") Long id){
        return participantService.delete(id);
    }
}