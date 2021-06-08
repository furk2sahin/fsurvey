package project.fsurvey.business.abstracts;

import org.springframework.http.ResponseEntity;
import project.fsurvey.core.results.DataResult;
import project.fsurvey.dtos.AnswerDto;
import project.fsurvey.entities.concretes.survey.Answer;

import java.util.List;

public interface AnswerService{
    ResponseEntity<DataResult<Answer>> add(AnswerDto answerDto);
    ResponseEntity<DataResult<Answer>> update(Long id, AnswerDto answerDto);
    ResponseEntity<DataResult<Answer>> findById(Long id);
    ResponseEntity<DataResult<List<Answer>>> findByParticipantId(Long participantId);
    ResponseEntity<DataResult<List<Answer>>> findBySurveyId(Long surveyId);
    ResponseEntity<DataResult<List<Answer>>> findByIssueId(Long issueId);
    ResponseEntity<DataResult<List<Answer>>> findByOptionId(Long optionId);
}
