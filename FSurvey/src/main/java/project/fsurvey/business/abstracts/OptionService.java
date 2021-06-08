package project.fsurvey.business.abstracts;

import org.springframework.http.ResponseEntity;
import project.fsurvey.core.results.DataResult;
import project.fsurvey.core.results.Result;
import project.fsurvey.dtos.OptionDto;
import project.fsurvey.entities.concretes.survey.Option;

import java.util.List;

public interface OptionService{
    ResponseEntity<DataResult<Option>> add(OptionDto optionDto);
    ResponseEntity<DataResult<Option>> update(Long id, OptionDto optionDto);
    ResponseEntity<Result> delete(Long id);
    ResponseEntity<DataResult<Option>> findById(Long id);
    ResponseEntity<DataResult<List<Option>>> findByIssueId(Long issueId);
    ResponseEntity<DataResult<List<Option>>> addAll(List<OptionDto> optionDtos);
}
