package project.fsurvey.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import project.fsurvey.entities.concretes.users.Participant;

@Repository
public interface ParticipantRepository extends JpaRepository<Participant, Long> {
}
