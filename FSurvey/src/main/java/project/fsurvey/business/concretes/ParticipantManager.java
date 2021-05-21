package project.fsurvey.business.concretes;

import com.google.common.base.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import project.fsurvey.adapter.abstracts.UserVerificationService;
import project.fsurvey.business.abstracts.ParticipantService;
import project.fsurvey.entities.concretes.users.Participant;
import project.fsurvey.exception.NotFoundException;
import project.fsurvey.exception.ParameterException;
import project.fsurvey.exception.UserVerificationException;
import project.fsurvey.repositories.ParticipantRepository;

import java.util.UUID;

@Service
public class ParticipantManager implements ParticipantService {

    private ParticipantRepository participantRepository;
    private UserVerificationService userVerificationService;
    private PasswordEncoder passwordEncoder;

    @Autowired
    public ParticipantManager(ParticipantRepository participantRepository,
                              UserVerificationService userVerificationService,
                              PasswordEncoder passwordEncoder) {
        this.participantRepository = participantRepository;
        this.userVerificationService = userVerificationService;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public Participant findByUUID(UUID uuid) throws NotFoundException{
        return participantRepository.findParticipantByUuid(uuid)
                .orElseThrow(() -> new NotFoundException("Participant not found."));
    }

    @Override
    public Participant add(Participant participant){
        if(Strings.isNullOrEmpty(participant.getRole()) ||
                Strings.isNullOrEmpty(participant.getName()) ||
                Strings.isNullOrEmpty(participant.getBirthYear()) ||
                Strings.isNullOrEmpty(participant.getSurname()) ||
                Strings.isNullOrEmpty(participant.getNationalIdentity()) ||
                Strings.isNullOrEmpty(participant.getPassword()) ||
                Strings.isNullOrEmpty(participant.getUsername()))
            throw new ParameterException("Null or empty property found.");
        else if(userVerificationService.validate(participant.getNationalIdentity(),
                participant.getName(),
                participant.getSurname(),
                participant.getBirthYear())){
            participant.setPassword(passwordEncoder.encode(participant.getPassword()));
            return participantRepository.save(participant);
        } else
            throw new UserVerificationException("User information is incorrect.");
    }

    @Override
    public Participant update(Long id, Participant participant) {
        Participant participantToUpdate = participantRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Participant not found."));
        if(!Strings.isNullOrEmpty(participant.getPassword()) &&
                !passwordEncoder.matches(participant.getPassword(), participantToUpdate.getPassword())){
            participantToUpdate.setPassword(passwordEncoder.encode(participant.getPassword()));
        }
        if(!Strings.isNullOrEmpty(participant.getUsername()) &&
                !participantToUpdate.getUsername().equals(participant.getUsername())){
            participantToUpdate.setUsername(participant.getUsername());
        }
        return participantRepository.save(participantToUpdate);
    }

    @Override
    public void delete(Long id) {
        participantRepository.deleteById(id);
    }
}
