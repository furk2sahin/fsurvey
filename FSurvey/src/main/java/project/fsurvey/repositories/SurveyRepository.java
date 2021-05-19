package project.fsurvey.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import project.fsurvey.entities.concretes.survey.Survey;

@Repository
public interface SurveyRepository extends JpaRepository<Survey, Long> {
}
