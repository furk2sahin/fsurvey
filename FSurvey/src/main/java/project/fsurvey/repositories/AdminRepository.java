package project.fsurvey.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import project.fsurvey.entities.concretes.users.Admin;

import java.util.Optional;

public interface AdminRepository extends JpaRepository<Admin, Long> {
}
