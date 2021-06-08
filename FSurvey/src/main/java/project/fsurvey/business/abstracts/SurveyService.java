package project.fsurvey.business.abstracts;

import org.springframework.http.ResponseEntity;
import project.fsurvey.core.results.DataResult;
import project.fsurvey.core.results.Result;
import project.fsurvey.dtos.SurveyDto;
import project.fsurvey.entities.concretes.survey.Survey;

public interface SurveyService{
    ResponseEntity<DataResult<Survey>> add(SurveyDto surveyDto);
    ResponseEntity<DataResult<Survey>> update(Long id, SurveyDto surveyDto);
    ResponseEntity<DataResult<Survey>> findById(Long id);
    ResponseEntity<Result> delete(Long id);
    boolean existById(Long id);

}
