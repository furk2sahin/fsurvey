package project.fsurvey.business.concretes;

import com.google.common.base.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.fsurvey.business.abstracts.IssueService;
import project.fsurvey.business.abstracts.OptionService;
import project.fsurvey.core.results.*;
import project.fsurvey.dtos.OptionDto;
import project.fsurvey.entities.concretes.survey.Issue;
import project.fsurvey.entities.concretes.survey.Option;
import project.fsurvey.mapper.OptionMapper;
import project.fsurvey.repositories.OptionRepository;

import java.util.List;

@Service
public class OptionManager implements OptionService {

    private OptionRepository optionRepository;
    private IssueService issueService;
    private OptionMapper optionMapper;
    private Environment environment;

    @Autowired
    public OptionManager(OptionRepository optionRepository,
                         IssueService issueService,
                         OptionMapper optionMapper,
                         Environment environment) {
        this.optionRepository = optionRepository;
        this.issueService = issueService;
        this.optionMapper = optionMapper;
        this.environment = environment;
    }

    @Override
    public ResponseEntity<DataResult<Option>> add(OptionDto optionDto) {
        DataResult<Issue> result = issueService.findById(optionDto.getIssueId()).getBody();
        if(!result.isSuccess())
            return ResponseEntity.badRequest().body(new ErrorDataResult<>(
                    environment.getProperty("ISSUE_NOT_FOUND")
            ));

        Option option = optionMapper.toEntity(optionDto);
        return ResponseEntity.ok(new SuccessDataResult<>(optionRepository.save(option)));
    }

    @Override
    public ResponseEntity<DataResult<Option>> update(Long id, OptionDto optionDto) {
        Option optionToUpdate = optionRepository.findById(id).orElse(null);
        if(optionToUpdate == null)
            return ResponseEntity.badRequest().body(new ErrorDataResult<>(environment.getProperty("OPTION_NOT_FOUND")));

        if(!Strings.isNullOrEmpty(optionDto.getAnswer()) &&
                !optionToUpdate.getAnswer().equals(optionDto.getAnswer())){
            optionToUpdate.setAnswer(optionDto.getAnswer());
        }
        return ResponseEntity.ok(new SuccessDataResult<>(
                optionRepository.save(optionToUpdate),
                environment.getProperty("OPTION_NOT_FOUND"))
        );
    }

    @Override
    public ResponseEntity<DataResult<Option>> findById(Long id) {
        Option option = optionRepository.findById(id).orElse(null);
        if(option == null){
            return ResponseEntity.badRequest().body(new ErrorDataResult<>(environment.getProperty("OPTION_NOT_FOUND")));
        }
        return ResponseEntity.ok(new SuccessDataResult<>(option, "Data found."));
    }

    @Override
    public ResponseEntity<DataResult<List<Option>>> findByIssueId(Long issueId) {
        List<Option> options = optionRepository.findAllByIssueId(issueId);
        if(options.isEmpty()){
            return ResponseEntity.badRequest().body(new ErrorDataResult<>(
                    environment.getProperty("OPTION_NOT_FOUND_BY_ISSUE"))
            );
        }
        return ResponseEntity.ok(new SuccessDataResult<>(options, environment.getProperty("OPTION_LISTED_BY_ISSUE")));
    }
}
