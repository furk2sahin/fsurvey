package project.fsurvey.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import project.fsurvey.entities.concretes.users.Admin;

@Repository
public interface AdminRepository extends JpaRepository<Admin, Long> {
}
