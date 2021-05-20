package project.fsurvey.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import project.fsurvey.entities.concretes.survey.Survey;

public interface SurveyRepository extends JpaRepository<Survey, Long> {
}
