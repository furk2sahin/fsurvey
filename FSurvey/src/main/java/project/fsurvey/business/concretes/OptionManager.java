package project.fsurvey.business.concretes;

import com.google.common.base.Strings;
import org.springframework.beans.factory.annotation.Autowired;
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

    @Autowired
    public OptionManager(OptionRepository optionRepository,
                         IssueService issueService,
                         OptionMapper optionMapper) {
        this.optionRepository = optionRepository;
        this.issueService = issueService;
        this.optionMapper = optionMapper;
    }

    @Override
    public ResponseEntity<DataResult<Option>> add(OptionDto optionDto) {
        DataResult<Issue> result = issueService.findById(optionDto.getIssueId()).getBody();
        if(!result.isSuccess())
            return ResponseEntity.badRequest().body(new ErrorDataResult<>(
                    "No Issue found with given id: " + optionDto.getIssueId()
            ));

        Option option = optionMapper.toEntity(optionDto);
        return ResponseEntity.ok(new SuccessDataResult<>(optionRepository.save(option)));
    }

    @Override
    public ResponseEntity<DataResult<Option>> update(Long id, OptionDto optionDto) {
        Option optionToUpdate = optionRepository.findById(id).orElse(null);
        if(optionToUpdate == null)
            return ResponseEntity.badRequest().body(new ErrorDataResult<>("No option found with given id" + id));

        if(!Strings.isNullOrEmpty(optionDto.getAnswer()) &&
                !optionToUpdate.getAnswer().equals(optionDto.getAnswer())){
            optionToUpdate.setAnswer(optionDto.getAnswer());
        }
        return ResponseEntity.ok(new SuccessDataResult<>(
                optionRepository.save(optionToUpdate),
                "Option updated successfully.")
        );
    }

    @Override
    public ResponseEntity<DataResult<Option>> findById(Long id) {
        Option option = optionRepository.findById(id).orElse(null);
        if(option == null){
            return ResponseEntity.badRequest().body(new ErrorDataResult<>("Option not found with id " + id));
        }
        return ResponseEntity.ok(new SuccessDataResult<>(option, "Data found."));
    }

    @Override
    public ResponseEntity<DataResult<List<Option>>> findByIssueId(Long issueId) {
        List<Option> options = optionRepository.findAllByIssueId(issueId);
        if(options.isEmpty()){
            return ResponseEntity.badRequest().body(new ErrorDataResult<>(
                    "No option were found by given issue id " + issueId)
            );
        }
        return ResponseEntity.ok(new SuccessDataResult<>(options, "Answers listed by issue id " + issueId));
    }
}
