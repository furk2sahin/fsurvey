package project.fsurvey.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.userdetails.UserDetails;
import project.fsurvey.entities.abstracts.User;

public interface UserRepository extends JpaRepository<User, Long> {
    UserDetails findByUsername(String username);
    boolean existsByUsername(String username);
}
