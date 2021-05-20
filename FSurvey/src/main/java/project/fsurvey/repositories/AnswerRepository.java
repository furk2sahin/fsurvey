package project.fsurvey.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import project.fsurvey.entities.concretes.survey.Answer;

import java.util.List;

public interface AnswerRepository extends JpaRepository<Answer, Long> {
    List<Answer> findAllBySurveyId(Long id);
    List<Answer> findAllByParticipantId(Long id);
    List<Answer> findAllByOptionId(Long id);
    List<Answer> findAllByIssueId(Long id);
}
