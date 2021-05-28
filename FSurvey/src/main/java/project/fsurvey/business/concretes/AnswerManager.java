package project.fsurvey.business.concretes;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import project.fsurvey.business.abstracts.AnswerService;
import project.fsurvey.entities.concretes.survey.Answer;
import project.fsurvey.entities.concretes.survey.Issue;
import project.fsurvey.entities.concretes.survey.Option;
import project.fsurvey.core.exception.NotFoundException;
import project.fsurvey.core.exception.ParameterException;
import project.fsurvey.repositories.*;

import java.util.List;

@Service
public class AnswerManager implements AnswerService {

    private AnswerRepository answerRepository;
    private ParticipantRepository participantRepository;
    private SurveyRepository surveyRepository;
    private IssueRepository issueRepository;
    private OptionRepository optionRepository;

    @Autowired
    public AnswerManager(AnswerRepository answerRepository,
                         ParticipantRepository participantRepository,
                         SurveyRepository surveyRepository,
                         IssueRepository issueRepository,
                         OptionRepository optionRepository) {
        this.answerRepository = answerRepository;
        this.participantRepository = participantRepository;
        this.surveyRepository = surveyRepository;
        this.issueRepository = issueRepository;
        this.optionRepository = optionRepository;
    }

    @Override
    public Answer findById(Long id) {
        return answerRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Answer not found with id " + id));
    }

    @Override
    public Answer add(Answer answer) {
        Long participantId = answer.getParticipant().getId();
        Long surveyId = answer.getSurvey().getId();
        Long issueId = answer.getIssue().getId();
        Long optionId = answer.getOption().getId();
        if(participantId < 1 || surveyId < 1 || issueId < 1 || optionId < 1)
            throw new ParameterException("ID must be bigger than 0.");
        else if(answerRepository.existsByParticipantIdAndSurveyIdAndIssueIdAndOptionId(participantId,
                surveyId, issueId, optionId))
            throw new ParameterException("This record already exists");

        answer.setParticipant(participantRepository.findById(answer.getParticipant().getId())
                .orElseThrow(() -> new NotFoundException("Participant not found with id " + answer.getParticipant().getId())));
        answer.setSurvey(surveyRepository.findById(answer.getSurvey().getId())
                .orElseThrow(() -> new NotFoundException("Survey not found with id " + answer.getSurvey().getId())));

        Issue issue = issueRepository.findById(answer.getIssue().getId())
                .orElseThrow(() -> new NotFoundException("Issue not found with id " + answer.getIssue().getId()));

        if(!issue.getSurvey().getId().equals(answer.getSurvey().getId()))
            throw new ParameterException("Issue's survey ID didn't match.");
        answer.setIssue(issue);

        Option option = optionRepository.findById(answer.getOption().getId())
                .orElseThrow(() -> new NotFoundException("Option not found with id " + answer.getOption().getId()));

        if(!option.getIssue().getId().equals(answer.getIssue().getId()))
            throw new ParameterException("Option's issue ID didn't match.");
        answer.setOption(option);
        return answerRepository.save(answer);
    }

    @Override
    public List<Answer> findByParticipantId(Long participantId) {
        return answerRepository.findAllByParticipantId(participantId);
    }

    @Override
    public List<Answer> findBySurveyId(Long surveyId) {
        return answerRepository.findAllBySurveyId(surveyId);
    }

    @Override
    public List<Answer> findByIssueId(Long issueId) {
        return answerRepository.findAllByIssueId(issueId);
    }

    @Override
    public List<Answer> findByOptionId(Long optionId){
        return answerRepository.findAllByOptionId(optionId);
    }

    @Override
    public Answer update(Long id, Answer answer) {
        Answer answerToUpdate = answerRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Issue not found."));

        if(!answer.getOption().getId().equals(answerToUpdate.getOption().getId())){
            Option option = optionRepository.findById(answer.getOption().getId())
                    .orElseThrow(() -> new NotFoundException("Option not found with id " + answer.getOption().getId()));

            if(!option.getIssue().getId().equals(answerToUpdate.getIssue().getId()))
                throw new ParameterException("Option's issue ID didn't match.");

            answerToUpdate.setOption(option);
        }

        return answerRepository.save(answerToUpdate);
    }
}
