package project.fsurvey.security;

import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.social.connect.Connection;
import org.springframework.social.connect.web.SignInAdapter;
import org.springframework.web.context.request.NativeWebRequest;
import project.fsurvey.repositories.ParticipantRepository;
import project.fsurvey.security.permissions.UserRole;

@NoArgsConstructor
public class FacebookSignInAdapter implements SignInAdapter {

    private ParticipantRepository participantRepository;

    @Autowired
    public FacebookSignInAdapter(ParticipantRepository participantRepository) {
        this.participantRepository = participantRepository;
    }

    @Override
    public String signIn(String localUserId, Connection<?> connection, NativeWebRequest request) {
        
        SecurityContextHolder.getContext().setAuthentication(
          new UsernamePasswordAuthenticationToken(
          participantRepository.findByUsername(connection.getKey().getProviderUserId()).orElse(null),
                  null,
          UserRole.PARTICIPANT.getAuthorities()));
        
        return connection.getDisplayName();
    }
}