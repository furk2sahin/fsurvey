package project.fsurvey.business.concretes;

import com.google.common.base.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import project.fsurvey.business.abstracts.UserService;
import project.fsurvey.core.adapter.abstracts.UserVerificationService;
import project.fsurvey.core.results.*;
import project.fsurvey.core.util.RoleParser;
import project.fsurvey.dtos.UserDto;
import project.fsurvey.dtos.UserGetDto;
import project.fsurvey.entities.concretes.users.Participant;
import project.fsurvey.mapper.ParticipantMapper;
import project.fsurvey.repositories.ParticipantRepository;

@Service
public class ParticipantManager implements project.fsurvey.business.abstracts.ParticipantService {

    private ParticipantRepository participantRepository;
    private UserVerificationService userVerificationService;
    private PasswordEncoder passwordEncoder;
    private final ParticipantMapper participantMapper;
    private final UserService userService;
    private Environment environment;

    @Autowired
    public ParticipantManager(ParticipantRepository participantRepository,
                              UserVerificationService userVerificationService,
                              PasswordEncoder passwordEncoder,
                              ParticipantMapper participantMapper,
                              UserService userService,
                              Environment environment) {
        this.participantRepository = participantRepository;
        this.userVerificationService = userVerificationService;
        this.passwordEncoder = passwordEncoder;
        this.participantMapper = participantMapper;
        this.userService = userService;
        this.environment = environment;
    }

    @Override
    public ResponseEntity<DataResult<UserGetDto>> findById(Long id) {
        Participant participant = participantRepository.findById(id).orElse(null);
        if(participant == null){
            return ResponseEntity.badRequest().body(new ErrorDataResult());
        } else {
            return ResponseEntity.ok(new SuccessDataResult<>(
                    participantMapper.participantToGetDto(participant),
                    environment.getProperty("PARTICIPANT_NOT_FOUND")
            ));
        }
    }

    @Override
    public ResponseEntity<DataResult<UserGetDto>> add(UserDto participantDto){
        if(userService.existsByUsername(participantDto.getUsername()))
            return ResponseEntity.badRequest().body(new ErrorDataResult<>(environment.getProperty("USERNAME_ALREADY_TAKEN")));

        if(userVerificationService.validate(
                participantDto.getNationalIdentity(),
                participantDto.getName(),
                participantDto.getSurname(),
                participantDto.getBirthYear())){

            Participant participant = participantMapper.postDtoToParticipant(participantDto);
            participant.setPassword(passwordEncoder.encode(participant.getPassword()));
            participant.setAuthorities(RoleParser.parse(participant.getRole().split(","), participant));
            return ResponseEntity.ok(
                    new SuccessDataResult<>(participantMapper.participantToGetDto(participantRepository.save(participant)))
            );

        } else
            return ResponseEntity.badRequest().body(new ErrorDataResult<>("User information is incorrect."));
    }

    @Override
    public ResponseEntity<DataResult<UserGetDto>> update(Long id, UserDto userPostDto) {
        if(userService.existsByUsername(userPostDto.getUsername()))
            return ResponseEntity.badRequest().body(new ErrorDataResult<>("This username already exists."));

        Participant participantToUpdate = participantRepository.findById(id).orElse(null);
        if(participantToUpdate == null){
            return ResponseEntity.badRequest().body(new ErrorDataResult<>("No Participant found with given id"));
        }
        if(!Strings.isNullOrEmpty(userPostDto.getUsername()) &&
                !participantToUpdate.getUsername().equals(userPostDto.getUsername())){
            participantToUpdate.setUsername(userPostDto.getUsername());
        }
        if(!Strings.isNullOrEmpty(userPostDto.getPassword()) &&
                !passwordEncoder.matches(userPostDto.getPassword(), participantToUpdate.getPassword())){
            participantToUpdate.setPassword(passwordEncoder.encode(userPostDto.getPassword()));
        }

        return ResponseEntity.ok(new SuccessDataResult<>(
                participantMapper.participantToGetDto(participantRepository.save(participantToUpdate)),
                "User updated successfully.")
        );
    }

    @Override
    public ResponseEntity<Result> delete(Long id) {
        if(participantRepository.existsById(id)){
            participantRepository.deleteById(id);
            return ResponseEntity.ok(new SuccessResult("Participant deleted successfully."));
        } else{
            return ResponseEntity.badRequest().body(new ErrorResult("Participant not found with given id."));
        }
    }

    @Override
    public boolean existById(Long id) {
        return participantRepository.existsById(id);
    }
}
