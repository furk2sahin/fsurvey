package project.fsurvey.business.concretes;

import com.google.common.base.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import project.fsurvey.business.abstracts.OptionService;
import project.fsurvey.entities.concretes.survey.Issue;
import project.fsurvey.entities.concretes.survey.Option;
import project.fsurvey.exception.NotFoundException;
import project.fsurvey.repositories.IssueRepository;
import project.fsurvey.repositories.OptionRepository;

import java.util.List;

@Service
public class OptionManager implements OptionService {

    private OptionRepository optionRepository;
    private IssueRepository issueRepository;

    @Autowired
    public OptionManager(OptionRepository optionRepository, IssueRepository issueRepository) {
        this.optionRepository = optionRepository;
        this.issueRepository = issueRepository;
    }

    @Override
    public void addAll(Long issueId, List<Option> options) {
        Issue issue = issueRepository.findById(issueId)
                .orElseThrow(() -> new NotFoundException("Issue not found with given id."));
        options.stream().forEach(option -> option.setIssue(issue));
        optionRepository.saveAll(options);
    }

    @Override
    public Option add(Long issueId, Option option) {
        option.setIssue(issueRepository.findById(issueId)
                .orElseThrow(() -> new NotFoundException("Issue not found with given id.")));
        return optionRepository.save(option);
    }

    @Override
    public Option update(Long id, Option option) {
        Option optionToUpdate = optionRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Option not found."));
        if(!Strings.isNullOrEmpty(option.getAnswer()) &&
                !optionToUpdate.getAnswer().equals(option.getAnswer())){
            optionToUpdate.setAnswer(option.getAnswer());
        }
        return optionRepository.save(optionToUpdate);
    }

    @Override
    public void delete(Long id) {
        optionRepository.deleteById(id);
    }

    @Override
    public Option findById(Long id) {
        return optionRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Option not found with given id."));
    }

    @Override
    public List<Option> findByIssueId(Long issueId) {
        return optionRepository.findAllByIssueId(issueId);
    }
}
