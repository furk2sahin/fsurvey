package project.fsurvey.security.social;

import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.social.connect.Connection;
import org.springframework.social.connect.web.SignInAdapter;
import org.springframework.web.context.request.NativeWebRequest;
import project.fsurvey.entities.concretes.users.Participant;
import project.fsurvey.repositories.ParticipantRepository;

@NoArgsConstructor
public class SocialSignInAdapter implements SignInAdapter {

    private ParticipantRepository participantRepository;

    @Autowired
    public SocialSignInAdapter(ParticipantRepository participantRepository) {
        this.participantRepository = participantRepository;
    }

    @Override
    public String signIn(String localUserId, Connection<?> connection, NativeWebRequest request) {

        Participant participant = participantRepository
                .findByUsername(connection.getKey().getProviderUserId()).orElse(null);

        if(participant != null){
            SecurityContextHolder.getContext().setAuthentication(
                    new UsernamePasswordAuthenticationToken(
                            participant,
                            participant.getPassword(),
                            participant.getAuthorities()));
            return "/welcome";
        }
        return "/error";
    }
}