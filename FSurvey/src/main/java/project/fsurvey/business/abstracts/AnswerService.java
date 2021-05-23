package project.fsurvey.business.abstracts;

import project.fsurvey.entities.concretes.survey.Answer;

import java.util.List;

public interface AnswerService{
    Answer add(Answer answer);
    Answer update(Long id, Answer answer);
    Answer findById(Long id);
    List<Answer> findByParticipantId(Long participantId);
    List<Answer> findBySurveyId(Long surveyId);
    List<Answer> findByIssueId(Long issueId);
    List<Answer> findByOptionId(Long optionId);
}
