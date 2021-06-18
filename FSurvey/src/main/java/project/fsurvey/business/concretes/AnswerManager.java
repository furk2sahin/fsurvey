package project.fsurvey.business.concretes;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
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
    private final Environment environment;

    @Autowired
    public AnswerManager(AnswerRepository answerRepository,
                         ParticipantRepository participantRepository,
                         SurveyRepository surveyRepository,
                         IssueService issueService,
                         AnswerMapper answerMapper,
                         OptionService optionService,
                         Environment environment) {
        this.answerRepository = answerRepository;
        this.participantRepository = participantRepository;
        this.surveyRepository = surveyRepository;
        this.issueService = issueService;
        this.optionService = optionService;
        this.answerMapper = answerMapper;
        this.environment = environment;
    }

    @Override
    public ResponseEntity<DataResult<Answer>> add(AnswerDto answerDto) {
        if(answerRepository.existsByParticipantIdAndSurveyIdAndIssueIdAndOptionId(
                answerDto.getParticipantId(),
                answerDto.getSurveyId(),
                answerDto.getIssueId(),
                answerDto.getOptionId()))
            return ResponseEntity.badRequest().body(new ErrorDataResult<>(environment.getProperty("RECORD_ALREADY_EXISTS")));

        if(!participantRepository.existsById(answerDto.getParticipantId()))
            return ResponseEntity.badRequest().body(new ErrorDataResult<>(
                    environment.getProperty("PARTICIPANT_NOT_FOUND")
            ));
        if(!surveyRepository.existsById(answerDto.getSurveyId()))
            return ResponseEntity.badRequest().body(new ErrorDataResult<>(
                    environment.getProperty("SURVEY_NOT_FOUND")
            ));

        DataResult<Issue> issueResult = issueService.findById(answerDto.getIssueId()).getBody();

        if(!issueResult.isSuccess()){
            return ResponseEntity.badRequest().body(new ErrorDataResult<>(issueResult.getMessage()));
        }

        Issue issue = issueResult.getData();

        if(!issue.getSurvey().getId().equals(answerDto.getSurveyId()))
            return ResponseEntity.badRequest().body(new ErrorDataResult<>(environment.getProperty("ISSUE_SURVEY_ID_DID_NOT_MATCH")));

        DataResult<Option> optionResult = optionService.findById(answerDto.getOptionId()).getBody();

        if(!optionResult.isSuccess()){
            return ResponseEntity.badRequest().body(new ErrorDataResult<>(optionResult.getMessage()));
        }

        Option option = optionResult.getData();

        if(!option.getIssue().getId().equals(answerDto.getIssueId()))
            return ResponseEntity.badRequest().body(new ErrorDataResult<>(environment.getProperty("OPTION_SURVEY_ID_DID_NOT_MATCH")));

        Answer answer = answerMapper.toEntity(answerDto);
        return ResponseEntity.ok(new SuccessDataResult<>(answerRepository.save(answer)));
    }

    @Override
    public ResponseEntity<DataResult<Answer>> update(Long id, AnswerDto answerDto) {
        Answer answerToUpdate = answerRepository.findById(id).orElse(null);

        if(answerToUpdate == null)
            return ResponseEntity.badRequest().body(new ErrorDataResult<>(environment.getProperty("ANSWER_NOT_FOUND")));

        if(!answerDto.getOptionId().equals(answerToUpdate.getOption().getId())){
            DataResult<Option> result = optionService.findById(answerDto.getOptionId()).getBody();
            if(!result.isSuccess())
                return ResponseEntity.badRequest().body(new ErrorDataResult<>(
                        environment.getProperty("OPTION_NOT_FOUND"))
                );

            Option option = result.getData();

            if(!option.getIssue().getId().equals(answerToUpdate.getIssue().getId()))
                return ResponseEntity.badRequest().body(new ErrorDataResult<>(
                        environment.getProperty("OPTION_ISSUE_ID_DID_NOT_MATCH")
                ));

            answerToUpdate.setOption(option);
        }

        return ResponseEntity.ok(new SuccessDataResult<>(
                answerRepository.save(answerToUpdate),
                environment.getProperty("ANSWER_UPDATED")
        ));
    }

    @Override
    public ResponseEntity<DataResult<Answer>> findById(Long id) {
        Answer answer = answerRepository.findById(id).orElse(null);
        if(answer == null){
            return ResponseEntity.badRequest().body(new ErrorDataResult<>(environment.getProperty("ANSWER_NOT_FOUND")));
        }
        return ResponseEntity.ok(new SuccessDataResult<>(answer, environment.getProperty("ANSWER_FOUND")));
    }

    @Override
    public ResponseEntity<DataResult<List<Answer>>> findByParticipantId(Long participantId) {

        List<Answer> answers = answerRepository.findAllByParticipantId(participantId);
        if(answers.isEmpty()){
            return ResponseEntity.badRequest().body(new ErrorDataResult<>(
                    environment.getProperty("ANSWER_NOT_FOUND_BY_PARTICIPANT"))
            );
        }
        return ResponseEntity.ok(new SuccessDataResult<>(answers, environment.getProperty("ANSWER_LISTED_BY_PARTICIPANT")));
    }

    @Override
    public ResponseEntity<DataResult<List<Answer>>> findBySurveyId(Long surveyId) {
        List<Answer> answers = answerRepository.findAllBySurveyId(surveyId);
        if(answers.isEmpty()){
            return ResponseEntity.badRequest().body(new ErrorDataResult<>(
                    environment.getProperty("ANSWER_NOT_FOUND_BY_SURVEY"))
            );
        }
        return ResponseEntity.ok(new SuccessDataResult<>(answers, environment.getProperty("ANSWER_LISTED_BY_SURVEY")));
    }

    @Override
    public ResponseEntity<DataResult<List<Answer>>> findByIssueId(Long issueId) {
        List<Answer> answers = answerRepository.findAllByIssueId(issueId);
        if(answers.isEmpty()){
            return ResponseEntity.badRequest().body(new ErrorDataResult<>(
                    environment.getProperty("ANSWER_NOT_FOUND_BY_ISSUE"))
            );
        }
        return ResponseEntity.ok(new SuccessDataResult<>(answers, environment.getProperty("ANSWER_LISTED_BY_ISSUE")));
    }

    @Override
    public ResponseEntity<DataResult<List<Answer>>> findByOptionId(Long optionId){
        List<Answer> answers = answerRepository.findAllByOptionId(optionId);
        if(answers.isEmpty()){
            return ResponseEntity.badRequest().body(new ErrorDataResult<>(
                    environment.getProperty("ANSWER_NOT_FOUND_BY_OPTION"))
            );
        }
        return ResponseEntity.ok(new SuccessDataResult<>(answers, environment.getProperty("ANSWER_LISTED_BY_OPTION")));
    }
}