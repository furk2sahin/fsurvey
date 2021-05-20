package project.fsurvey.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import project.fsurvey.entities.concretes.users.Participant;

public interface ParticipantRepository extends JpaRepository<Participant, Long> {
}
