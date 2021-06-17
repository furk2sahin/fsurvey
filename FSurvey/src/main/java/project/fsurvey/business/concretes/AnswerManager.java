package project.fsurvey.business.concretes;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import project.fsurvey.business.abstracts.AnswerService;
import project.fsurvey.business.abstracts.IssueService;
import project.fsurvey.business.abstracts.OptionService;
import project.fsurvey.core.results.DataResult;
import project.fsurvey.core.results.ErrorDataResult;
import project.fsurvey.core.results.SuccessDataResult;
import project.fsurvey.dtos.AnswerDto;
import project.fsurvey.entities.concretes.survey.Answer;
import project.fsurvey.entities.concretes.survey.Issue;
import project.fsurvey.entities.concretes.survey.Option;
import project.fsurvey.mapper.AnswerMapper;
import project.fsurvey.repositories.*;

import java.util.Collections;
import java.util.List;

@Service
public class AnswerManager implements AnswerService {

    private final AnswerRepository answerRepository;
    private final ParticipantRepository participantRepository;
    private final SurveyRepository surveyRepository;
    private final IssueService issueService;
    private final OptionService optionService;
    private final AnswerMapper answerMapper;

    @Autowired
    public AnswerManager(AnswerRepository answerRepository,
                         ParticipantRepository participantRepository,
                         SurveyRepository surveyRepository,
                         IssueService issueService,
                         AnswerMapper answerMapper,
                         OptionService optionService) {
        this.answerRepository = answerRepository;
        this.participantRepository = participantRepository;
        this.surveyRepository = surveyRepository;
        this.issueService = issueService;
        this.optionService = optionService;
        this.answerMapper = answerMapper;
    }

    @Override
    public ResponseEntity<DataResult<Answer>> add(AnswerDto answerDto) {
        if(answerRepository.existsByParticipantIdAndSurveyIdAndIssueIdAndOptionId(
                answerDto.getParticipantId(),
                answerDto.getSurveyId(),
                answerDto.getIssueId(),
                answerDto.getOptionId()))
            return ResponseEntity.badRequest().body(new ErrorDataResult<>("This record already exists"));

        if(!participantRepository.existsById(answerDto.getParticipantId()))
            return ResponseEntity.badRequest().body(new ErrorDataResult<>(
                    "Participant not found with id " + answerDto.getParticipantId()
            ));
        if(!surveyRepository.existsById(answerDto.getSurveyId()))
            return ResponseEntity.badRequest().body(new ErrorDataResult<>(
                    "Survey not found with id " + answerDto.getSurveyId()
            ));

        DataResult<Issue> issueResult = issueService.findById(answerDto.getIssueId()).getBody();

        if(!issueResult.isSuccess()){
            return ResponseEntity.badRequest().body(new ErrorDataResult<>(issueResult.getMessage()));
        }

        Issue issue = issueResult.getData();

        if(!issue.getSurvey().getId().equals(answerDto.getSurveyId()))
            return ResponseEntity.badRequest().body(new ErrorDataResult<>("Survey ID didn't match."));

        DataResult<Option> optionResult = optionService.findById(answerDto.getOptionId()).getBody();

        if(!optionResult.isSuccess()){
            return ResponseEntity.badRequest().body(new ErrorDataResult<>(optionResult.getMessage()));
        }

        Option option = optionResult.getData();

        if(!option.getIssue().getId().equals(answerDto.getIssueId()))
            return ResponseEntity.badRequest().body(new ErrorDataResult<>("Issue ID didn't match."));

        Answer answer = answerMapper.toEntity(answerDto);
        return ResponseEntity.ok(new SuccessDataResult<>(answerRepository.save(answer)));
    }

    @Override
    public ResponseEntity<DataResult<Answer>> update(Long id, AnswerDto answerDto) {
        Answer answerToUpdate = answerRepository.findById(id).orElse(null);

        if(answerToUpdate == null)
            return ResponseEntity.badRequest().body(new ErrorDataResult<>("Answer not found with given id: " + id));

        if(!answerDto.getOptionId().equals(answerToUpdate.getOption().getId())){
            DataResult<Option> result = optionService.findById(answerDto.getOptionId()).getBody();
            if(!result.isSuccess())
                return ResponseEntity.badRequest().body(new ErrorDataResult<>(
                        "Option not found with id " + answerDto.getOptionId())
                );

            Option option = result.getData();

            if(!option.getIssue().getId().equals(answerToUpdate.getIssue().getId()))
                return ResponseEntity.badRequest().body(new ErrorDataResult<>("Option's issue ID didn't match."));

            answerToUpdate.setOption(option);
        }

        return ResponseEntity.ok(new SuccessDataResult<>(
                answerRepository.save(answerToUpdate),
                "Answer updated successfully"
        ));
    }

    @Override
    public ResponseEntity<DataResult<Answer>> findById(Long id) {
        Answer answer = answerRepository.findById(id).orElse(null);
        if(answer == null){
            return ResponseEntity.badRequest().body(new ErrorDataResult<>("Answer not found with id " + id));
        }
        return ResponseEntity.ok(new SuccessDataResult<>(answer, "Data found."));
    }

    @Override
    public ResponseEntity<DataResult<List<Answer>>> findByParticipantId(Long participantId) {

        List<Answer> answers = answerRepository.findAllByParticipantId(participantId);
        if(answers.isEmpty()){
            return ResponseEntity.badRequest().body(new ErrorDataResult<>(
                    "No answer were found by given participant id " + participantId)
            );
        }
        return ResponseEntity.ok(new SuccessDataResult<>(answers, "Answers listed by participant id " + participantId));
    }

    @Override
    public ResponseEntity<DataResult<List<Answer>>> findBySurveyId(Long surveyId) {
        List<Answer> answers = answerRepository.findAllBySurveyId(surveyId);
        if(answers.isEmpty()){
            return ResponseEntity.badRequest().body(new ErrorDataResult<>(
                    "No answer were found by given survey id " + surveyId)
            );
        }
        return ResponseEntity.ok(new SuccessDataResult<>(answers, "Answers listed by survey id " + surveyId));
    }

    @Override
    public ResponseEntity<DataResult<List<Answer>>> findByIssueId(Long issueId) {
        List<Answer> answers = answerRepository.findAllByIssueId(issueId);
        if(answers.isEmpty()){
            return ResponseEntity.badRequest().body(new ErrorDataResult<>(
                    "No answer were found by given issue id " + issueId)
            );
        }
        return ResponseEntity.ok(new SuccessDataResult<>(answers, "Answers listed by issue id " + issueId));
    }

    @Override
    public ResponseEntity<DataResult<List<Answer>>> findByOptionId(Long optionId){
        List<Answer> answers = answerRepository.findAllByOptionId(optionId);
        if(answers.isEmpty()){
            return ResponseEntity.badRequest().body(new ErrorDataResult<>(
                    "No answer were found by given option id " + optionId)
            );
        }
        return ResponseEntity.ok(new SuccessDataResult<>(answers, "Answers listed by option id " + optionId));
    }
}
