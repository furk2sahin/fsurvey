package project.fsurvey.security.social;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.social.connect.Connection;
import org.springframework.social.connect.ConnectionSignUp;
import org.springframework.stereotype.Service;
import project.fsurvey.core.util.RoleParser;
import project.fsurvey.entities.concretes.users.Participant;
import project.fsurvey.repositories.ParticipantRepository;

import java.util.Arrays;

@Service
public class SocialConnectionSignup implements ConnectionSignUp {

    private ParticipantRepository participantRepository;
    private PasswordEncoder passwordEncoder;

    @Autowired
    public SocialConnectionSignup(ParticipantRepository participantRepository, PasswordEncoder passwordEncoder) {
        this.participantRepository = participantRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public String execute(Connection<?> connection) {
        String providerUserId = connection.getKey().getProviderUserId();
        Participant participant = participantRepository.findByUsername(providerUserId).orElse(null);
        if(participant != null)
            return participant.getUsername();

        participant = new Participant();

        String[] displayNames = connection.getDisplayName().split(" ");

        if(displayNames.length > 1){
            participant.setName(displayNames[0]);
            String[] surnames = Arrays.copyOfRange(displayNames, 1, displayNames.length);
            String surname = "";
            for(String str : surnames)
                surname += str + " ";
            participant.setSurname(surname);
        } else {
            participant.setName(displayNames[0]);
            participant.setSurname(displayNames[0]);
        }

        participant.setUsername(providerUserId);
        participant.setPassword(passwordEncoder.encode(providerUserId));
        String[] role = {"PARTICIPANT"};
        participant.setAuthorities(RoleParser.parse(role, participant));
        participant.setRole("PARTICIPANT");
        participantRepository.save(participant);
        return participant.getUsername();
    }
}