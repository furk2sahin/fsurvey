package project.fsurvey.business.abstracts;

import project.fsurvey.entities.concretes.survey.Issue;

import java.util.List;
import java.util.Optional;

public interface IssueService extends CrudService<Issue> {
    Optional<Issue> findById(Long id);
    List<Issue> findBySurveyId(Long surveyId);
}
