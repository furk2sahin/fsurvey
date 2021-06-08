package project.fsurvey.business.abstracts;

import org.springframework.http.ResponseEntity;
import project.fsurvey.core.results.DataResult;
import project.fsurvey.core.results.Result;
import project.fsurvey.dtos.IssueDto;
import project.fsurvey.entities.concretes.survey.Issue;

import java.util.List;

public interface IssueService {
    ResponseEntity<DataResult<Issue>> add(IssueDto issueDto);
    ResponseEntity<DataResult<Issue>> update(Long id, IssueDto issueDto);
    ResponseEntity<Result> delete(Long id);
    ResponseEntity<DataResult<Issue>> findById(Long id);
    ResponseEntity<DataResult<List<Issue>>> findBySurveyId(Long surveyId);
    ResponseEntity<DataResult<List<Issue>>> addAll(Long surveyId, List<IssueDto> issueDtos);
    boolean existById(Long id);
}
