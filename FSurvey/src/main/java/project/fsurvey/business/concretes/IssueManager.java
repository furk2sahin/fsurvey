package project.fsurvey.business.concretes;

import com.google.common.base.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import project.fsurvey.business.abstracts.IssueService;
import project.fsurvey.business.abstracts.SurveyService;
import project.fsurvey.core.results.*;
import project.fsurvey.dtos.IssueDto;
import project.fsurvey.entities.concretes.survey.Issue;
import project.fsurvey.entities.concretes.survey.Survey;
import project.fsurvey.mapper.IssueMapper;
import project.fsurvey.repositories.IssueRepository;

import java.util.List;

@Service
public class IssueManager implements IssueService {

    private IssueRepository issueRepository;
    private SurveyService surveyService;
    private IssueMapper issueMapper;

    @Autowired
    public IssueManager(IssueRepository issueRepository,
                        SurveyService surveyService,
                        IssueMapper issueMapper) {
        this.issueRepository = issueRepository;
        this.surveyService = surveyService;
        this.issueMapper = issueMapper;
    }

    @Override
    public ResponseEntity<DataResult<Issue>> add(IssueDto issueDto) {
        if(!surveyService.existById(issueDto.getSurveyId()))
            return ResponseEntity.badRequest().body(new ErrorDataResult<>("No survey found with given id"));

        Issue issue = issueMapper.toEntity(issueDto);
        issue.getOptions().forEach(option -> option.setIssue(issue));
        return ResponseEntity.ok(new SuccessDataResult<>(issueRepository.save(issue), "Data saved successfully."));
    }

    @Override
    public ResponseEntity<DataResult<Issue>> update(Long id, IssueDto issueDto) {
        DataResult<Issue> result = findById(id).getBody();
        if(!result.isSuccess())
            return ResponseEntity.badRequest().body(new ErrorDataResult<>(result.getMessage()));

        Issue issue = result.getData();
        if(!Strings.isNullOrEmpty(issueDto.getQuestion()) &&
                !issue.getQuestion().equals(issue.getQuestion())){
            issue.setQuestion(issueDto.getQuestion());
        }

        return ResponseEntity.ok(new SuccessDataResult<>(issueRepository.save(issue), "Data updated successfully."));
    }

    @Override
    public ResponseEntity<DataResult<Issue>> findById(Long id) {
        Issue issue = issueRepository.findById(id).orElse(null);
        if(issue == null){
            return ResponseEntity.badRequest().body(new ErrorDataResult<>("Issue not found with id " + id));
        }
        return ResponseEntity.ok(new SuccessDataResult<>(issue, "Data found."));
    }

    @Override
    public ResponseEntity<DataResult<List<Issue>>> findBySurveyId(Long surveyId) {
        List<Issue> issues = issueRepository.findAllBySurveyId(surveyId);
        if(issues.isEmpty())
            return ResponseEntity.badRequest().body(new ErrorDataResult<>("No data were found."));
        return ResponseEntity.ok(new SuccessDataResult<>(issues, "Data listed by survey id " + surveyId));
    }

    @Override
    public ResponseEntity<Result> delete(Long id) {
        if(!issueRepository.existsById(id))
            return ResponseEntity.badRequest().body(new ErrorResult("No issue were found with id " + id));

        issueRepository.deleteById(id);
        return ResponseEntity.ok(new SuccessResult("Issue deleted successfully with given id " + id));
    }

    @Override
    public ResponseEntity<DataResult<List<Issue>>> addAll(Long surveyId, List<IssueDto> issueDtos) {
        DataResult<Survey> result = surveyService.findById(surveyId).getBody();
        if(!result.isSuccess())
            return ResponseEntity.badRequest().body(new ErrorDataResult<>(result.getMessage()));

        List<Issue> issues = issueMapper.toEntities(issueDtos);
        issues.forEach(issue -> issue.setSurvey(result.getData()));
        return ResponseEntity.ok(new SuccessDataResult<>(issueRepository.saveAll(issues), "Data saved successfully."));
    }

    @Override
    public boolean existById(Long id) {
        return issueRepository.existsById(id);
    }
}
