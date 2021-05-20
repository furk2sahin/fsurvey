package project.fsurvey.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import project.fsurvey.entities.concretes.survey.Issue;

import java.util.List;

public interface IssueRepository extends JpaRepository<Issue, Long> {
    List<Issue> findAllBySurveyId(Long id);
}
