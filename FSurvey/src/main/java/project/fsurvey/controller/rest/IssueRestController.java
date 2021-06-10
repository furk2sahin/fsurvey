package project.fsurvey.controller.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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

    @Autowired
    public IssueRestController(IssueService issueService) {
        this.issueService = issueService;
    }

    @GetMapping("/find-by-id/{id}")
    @PreAuthorize("hasAuthority('read:issue')")
    public ResponseEntity<DataResult<Issue>> findById(@PathVariable("id") Long id){
        return issueService.findById(id);
    }

    @PostMapping("/add")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<DataResult<Issue>> add(@RequestBody @Valid IssueDto issue){
        return issueService.add(issue);
    }

    @PutMapping("/update/{id}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<DataResult<Issue>> update(@PathVariable("id") Long id,
                                                    @RequestBody IssueDto issue){
        return issueService.update(id, issue);
    }

    @GetMapping("/find-by-survey-id/{id}")
    @PreAuthorize("hasAuthority('read:issue')")
    public ResponseEntity<DataResult<List<Issue>>> findBySurveyId(@PathVariable("id") Long id){
        return issueService.findBySurveyId(id);
    }

    @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<Result> delete(@PathVariable("id") Long id){
        return issueService.delete(id);
    }
}
