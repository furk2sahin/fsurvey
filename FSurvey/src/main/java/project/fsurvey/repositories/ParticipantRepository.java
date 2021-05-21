package project.fsurvey.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import project.fsurvey.entities.concretes.users.Participant;

import java.util.Optional;
import java.util.UUID;

public interface ParticipantRepository extends JpaRepository<Participant, Long> {
    Optional<Participant> findParticipantByUuid(UUID uuid);
}
