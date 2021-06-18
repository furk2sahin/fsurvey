package project.fsurvey.business.concretes;

import com.google.common.base.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import project.fsurvey.business.abstracts.IssueService;
import project.fsurvey.business.abstracts.SurveyService;
import project.fsurvey.core.results.*;
import project.fsurvey.dtos.IssueDto;
import project.fsurvey.entities.concretes.survey.Issue;
import project.fsurvey.mapper.IssueMapper;
import project.fsurvey.repositories.IssueRepository;

import java.util.List;

@Service
public class IssueManager implements IssueService {

    private IssueRepository issueRepository;
    private SurveyService surveyService;
    private IssueMapper issueMapper;
    private Environment environment;
    @Autowired
    public IssueManager(IssueRepository issueRepository,
                        SurveyService surveyService,
                        IssueMapper issueMapper,
                        Environment environment) {
        this.issueRepository = issueRepository;
        this.surveyService = surveyService;
        this.issueMapper = issueMapper;
        this.environment = environment;
    }

    @Override
    public ResponseEntity<DataResult<Issue>> add(IssueDto issueDto) {
        if(!surveyService.existById(issueDto.getSurveyId()))
            return ResponseEntity.badRequest().body(new ErrorDataResult<>(environment.getProperty("SURVEY_NOT_FOUND")));

        Issue issue = issueMapper.toEntity(issueDto);
        issue.getOptions().forEach(option -> option.setIssue(issue));
        return ResponseEntity.ok(new SuccessDataResult<>(issueRepository.save(issue), environment.getProperty("ISSUE_ADDED")));
    }

    @Override
    public ResponseEntity<DataResult<Issue>> update(Long id, IssueDto issueDto) {
        DataResult<Issue> result = findById(id).getBody();
        if(!result.isSuccess())
            return ResponseEntity.badRequest().body(new ErrorDataResult<>(result.getMessage()));

        Issue issue = result.getData();
        if(!Strings.isNullOrEmpty(issueDto.getQuestion()) &&
                !issue.getQuestion().equals(issueDto.getQuestion())){
            issue.setQuestion(issueDto.getQuestion());
        }

        return ResponseEntity.ok(new SuccessDataResult<>(issueRepository.save(issue), environment.getProperty("ISSUE_UPDATED")));
    }

    @Override
    public ResponseEntity<DataResult<Issue>> findById(Long id) {
        Issue issue = issueRepository.findById(id).orElse(null);
        if(issue == null){
            return ResponseEntity.badRequest().body(new ErrorDataResult<>(environment.getProperty("ISSUE_NOT_FOUND")));
        }
        return ResponseEntity.ok(new SuccessDataResult<>(issue, environment.getProperty("ISSUE_FOUND")));
    }

    @Override
    public ResponseEntity<DataResult<List<Issue>>> findBySurveyId(Long surveyId) {
        List<Issue> issues = issueRepository.findAllBySurveyId(surveyId);
        if(issues.isEmpty())
            return ResponseEntity.badRequest().body(new ErrorDataResult<>(environment.getProperty("ISSUE_NOT_FOUND_BY_SURVEY")));
        return ResponseEntity.ok(new SuccessDataResult<>(issues, environment.getProperty("ISSUE_LISTED_BY_SURVEY")));
    }

    @Override
    public ResponseEntity<Result> delete(Long id) {
        Issue issue = issueRepository.findById(id).orElse(null);
        if(issue == null)
            return ResponseEntity.badRequest().body(new ErrorResult(environment.getProperty("ISSUE_NOT_FOUND")));
        issue.setStatus(false);
        issueRepository.save(issue);
        return ResponseEntity.ok(new SuccessResult(environment.getProperty("ISSUE_DELETED")));
    }
}
