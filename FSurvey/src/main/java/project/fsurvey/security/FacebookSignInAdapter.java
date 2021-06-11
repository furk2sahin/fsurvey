package project.fsurvey.security;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.social.connect.Connection;
import org.springframework.social.connect.web.SignInAdapter;
import org.springframework.web.context.request.NativeWebRequest;
import project.fsurvey.security.permissions.UserRole;

public class FacebookSignInAdapter implements SignInAdapter {

    @Override
    public String signIn(String localUserId, Connection<?> connection, NativeWebRequest request) {
        
        SecurityContextHolder.getContext().setAuthentication(
          new UsernamePasswordAuthenticationToken(
          connection.getDisplayName(), null,
          UserRole.PARTICIPANT.getAuthorities()));
        
        return null;
    }
}