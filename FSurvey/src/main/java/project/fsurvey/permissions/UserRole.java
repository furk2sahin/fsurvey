package project.fsurvey.permissions;

import com.google.common.collect.Sets;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Set;
import java.util.stream.Collectors;

import static project.fsurvey.permissions.Permission.*;

public enum UserRole {
    ADMIN(Sets.newHashSet(READ_SURVEY, READ_OPTION, READ_ISSUE, READ_ANSWER, READ_PARTICIPANT)),
    PARTICIPANT(Sets.newHashSet(READ_SURVEY, READ_OPTION, READ_ISSUE, READ_ANSWER));

    Set<Permission> permissions;

    UserRole(Set<Permission> permissions) {
        this.permissions = permissions;
    }

    public Set<Permission> getPermissions() {
        return permissions;
    }

    public Set<SimpleGrantedAuthority> getAuthorities(){
        Set<SimpleGrantedAuthority> authorities = getPermissions().stream()
                .map(permission -> new SimpleGrantedAuthority(permission.getPermission()))
                .collect(Collectors.toSet());
        authorities.add(new SimpleGrantedAuthority("ROLE_"+this.name()));
        return authorities;
    }
}
