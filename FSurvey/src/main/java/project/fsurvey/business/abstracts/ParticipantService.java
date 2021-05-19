package project.fsurvey.business.abstracts;

import project.fsurvey.entities.concretes.users.Participant;

import java.util.Optional;
import java.util.UUID;

public interface ParticipantService extends CrudService<Participant>{
    Optional<Participant> findByUUID(UUID uuid);
}
