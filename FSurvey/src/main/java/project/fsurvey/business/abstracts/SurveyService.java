package project.fsurvey.business.abstracts;

import org.springframework.http.ResponseEntity;
import project.fsurvey.core.results.DataResult;
import project.fsurvey.core.results.Result;
import project.fsurvey.dtos.SurveyDto;
import project.fsurvey.entities.concretes.survey.Survey;

import java.util.List;

public interface SurveyService{
    ResponseEntity<DataResult<Survey>> add(SurveyDto surveyDto);
    ResponseEntity<DataResult<Survey>> update(Long id, SurveyDto surveyDto);
    ResponseEntity<DataResult<Survey>> findById(Long id);
    ResponseEntity<DataResult<List<Survey>>> findAll();
    ResponseEntity<DataResult<List<Survey>>> findAllPageable(int pageNo, int pageSize);
    ResponseEntity<Result> delete(Long id);
    Long surveyCount();
    boolean existById(Long id);

}
