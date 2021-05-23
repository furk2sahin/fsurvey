package project.fsurvey.business.abstracts;

import project.fsurvey.entities.concretes.survey.Issue;

import java.util.List;

public interface IssueService extends CrudService<Issue> {
    Issue findById(Long id);
    List<Issue> findBySurveyId(Long surveyId);
    List<Issue> addAll(Long surveyId, List<Issue> issues);
}
