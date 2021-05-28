package project.fsurvey.business.concretes;

import com.google.common.base.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import project.fsurvey.business.abstracts.IssueService;
import project.fsurvey.entities.concretes.survey.Issue;
import project.fsurvey.entities.concretes.survey.Survey;
import project.fsurvey.core.exception.NotFoundException;
import project.fsurvey.core.exception.ParameterException;
import project.fsurvey.repositories.IssueRepository;
import project.fsurvey.repositories.SurveyRepository;

import java.util.List;

@Service
public class IssueManager implements IssueService {

    private IssueRepository issueRepository;
    private SurveyRepository surveyRepository;

    @Autowired
    public IssueManager(IssueRepository issueRepository, SurveyRepository surveyRepository) {
        this.issueRepository = issueRepository;
        this.surveyRepository = surveyRepository;
    }

    @Override
    public Issue findById(Long id) {
        return issueRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Issue not found by id " + id));
    }

    @Override
    public Issue add(Issue issue) {
        if(Strings.isNullOrEmpty(issue.getQuestion()) ||
                issue.getIssueOrder() < 0) {
            throw new ParameterException("Null or empty property found.");
        } else return issueRepository.save(issue);
    }

    @Override
    public List<Issue> addAll(Long surveyId, List<Issue> issues) {
        Survey survey = surveyRepository.findById(surveyId)
                .orElseThrow(() -> new NotFoundException("Survey not found with given id."));
        issues.stream().forEach(issue -> issue.setSurvey(survey));
        return issueRepository.saveAll(issues);
    }

    @Override
    public Issue update(Long id, Issue issue) {
        Issue issueToUpdate = issueRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Issue not found."));
        if(!Strings.isNullOrEmpty(issue.getQuestion()) &&
                !issueToUpdate.getQuestion().equals(issue.getQuestion())){
            issueToUpdate.setQuestion(issue.getQuestion());
        }

        return issueRepository.save(issueToUpdate);
    }

    @Override
    public List<Issue> findBySurveyId(Long surveyId) {
        return issueRepository.findAllBySurveyId(surveyId);
    }

    @Override
    public void delete(Long id) {
        Issue issue = issueRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Issue not found."));
        issue.setSurvey(null);
        issueRepository.save(issue);
        issueRepository.deleteById(id);
    }
}
