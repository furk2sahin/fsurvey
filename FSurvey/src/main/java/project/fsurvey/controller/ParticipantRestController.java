package project.fsurvey.controller;

import com.google.common.base.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import project.fsurvey.business.concretes.AdminManager;
import project.fsurvey.business.concretes.ParticipantManager;
import project.fsurvey.dto.AdminDto;
import project.fsurvey.dto.ParticipantDto;
import project.fsurvey.entities.concretes.users.Admin;
import project.fsurvey.entities.concretes.users.Participant;
import project.fsurvey.exception.NotFoundException;
import project.fsurvey.exception.ParameterException;
import project.fsurvey.exception.UserVerificationException;
import project.fsurvey.util.RoleParser;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/participant")
public class ParticipantRestController {

    private ParticipantManager participantManager;

    @Autowired
    public ParticipantRestController(ParticipantManager participantManager) {
        this.participantManager = participantManager;
    }

    @GetMapping("/find-by-uuid")
    public ResponseEntity<Object> findByUuid(@RequestParam("uuid") UUID uuid){
        try{
            return ResponseEntity.ok(participantManager.findByUUID(uuid));
        } catch (NotFoundException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Participant not found with given uuid.");
        }
    }

    @PostMapping("/add")
    public ResponseEntity<Object> add(@RequestBody ParticipantDto participantDto){
        Participant participant = new Participant();
        participant.setName(participantDto.getName());
        participant.setBirthYear(participantDto.getBirthYear());
        participant.setSurname(participantDto.getSurname());
        participant.setNationalIdentity(participantDto.getNationalIdentity());
        participant.setPassword(participantDto.getPassword());
        participant.setRole(participantDto.getRole());
        participant.setUsername(participantDto.getUsername());
        if(!Strings.isNullOrEmpty(participant.getRole()))
            participant.setAuthorities(RoleParser.parse(participant.getRole().split(","), participant));
        try{
            return ResponseEntity.ok(participantManager.add(participant));
        } catch (ParameterException | UserVerificationException e){
            return ResponseEntity.status(HttpStatus.PRECONDITION_FAILED).body(e.getMessage());
        } catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error when creating participant");
        }
    }

    @PutMapping("/update")
    public ResponseEntity<Object> update(@RequestParam("id") Long id, @RequestBody ParticipantDto participantDto){
        Participant participant = new Participant();
        participant.setUsername(participantDto.getUsername());
        participant.setPassword(participantDto.getPassword());
        try{
            return ResponseEntity.ok(participantManager.update(id, participant));
        } catch (NotFoundException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Participant not found with given id.");
        }
    }

    @DeleteMapping("/delete")
    public ResponseEntity<String> delete(@RequestParam("id") Long id){
        try{
            participantManager.delete(id);
            return ResponseEntity.ok("Participant successfully deleted.");
        } catch (EmptyResultDataAccessException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Participant not found with given id");
        }
    }

}
