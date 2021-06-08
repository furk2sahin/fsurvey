package project.fsurvey.controller.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import project.fsurvey.business.abstracts.IssueService;
import project.fsurvey.business.abstracts.OptionService;
import project.fsurvey.business.abstracts.SurveyService;
import project.fsurvey.core.results.DataResult;
import project.fsurvey.core.results.Result;
import project.fsurvey.dtos.IssueDto;
import project.fsurvey.entities.concretes.survey.Issue;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/v1/issue")
public class IssueRestController {

    private IssueService issueService;
    private SurveyService surveyService;
    private OptionService optionService;

    @Autowired
    public IssueRestController(IssueService issueService, SurveyService surveyService, OptionService optionService) {
        this.issueService = issueService;
        this.surveyService = surveyService;
        this.optionService = optionService;
    }

    @GetMapping("/find-by-id/{id}")
    public ResponseEntity<DataResult<Issue>> findById(@PathVariable("id") Long id){
        return issueService.findById(id);
    }

    @PostMapping("/add")
    public ResponseEntity<DataResult<Issue>> add(@RequestBody IssueDto issueDto){
        return issueService.add(issueDto);
    }

    @PostMapping("/add-all/{surveyId}")
    public ResponseEntity<DataResult<List<Issue>>> addAll(@PathVariable("surveyId") Long surveyId,
                                                    @RequestBody @Valid List<IssueDto> issueDtos){
        return issueService.addAll(surveyId, issueDtos);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<DataResult<Issue>> update(@PathVariable("id") Long id,
                                                    @RequestBody @Valid IssueDto issueDto){
        return issueService.update(id, issueDto);
    }

    @GetMapping("/find-by-survey-id/{id}")
    public ResponseEntity<DataResult<List<Issue>>> findBySurveyId(@PathVariable("id") Long id){
        return issueService.findBySurveyId(id);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Result> delete(@PathVariable("id") Long id){
        return issueService.delete(id);
    }
}
