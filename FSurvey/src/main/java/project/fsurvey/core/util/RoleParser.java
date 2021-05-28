package project.fsurvey.core.util;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import project.fsurvey.entities.abstracts.User;
import project.fsurvey.entities.concretes.users.Role;

import java.util.HashSet;
import java.util.Set;

import static project.fsurvey.security.permissions.UserRole.*;

public class RoleParser {

    private RoleParser(){

    }

    public static Set<Role> parse(String[] roles, User user){
        Set<Role> authorities = new HashSet<>();
        for(String role : roles){
            if(role.toUpperCase().equals(ADMIN.name())){
                for(SimpleGrantedAuthority permission : ADMIN.getAuthorities())
                    addAuthority(authorities, permission, user);
            } else if(role.toUpperCase().equals(PARTICIPANT.name())){
                for(SimpleGrantedAuthority permission : PARTICIPANT.getAuthorities())
                    addAuthority(authorities, permission, user);
            }
        }
        return authorities;
    }

    public static void addAuthority(Set<Role> authorities,
                                    SimpleGrantedAuthority permission,
                                    User user){
        Role roleToAdd = new Role();
        roleToAdd.setUser(user);
        roleToAdd.setAuthority(permission.getAuthority());
        authorities.add(roleToAdd);
    }
}