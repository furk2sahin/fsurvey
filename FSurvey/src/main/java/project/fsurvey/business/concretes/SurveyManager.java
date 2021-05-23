package project.fsurvey.business.concretes;

import com.google.common.base.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import project.fsurvey.business.abstracts.SurveyService;
import project.fsurvey.entities.concretes.survey.Survey;
import project.fsurvey.exception.NotFoundException;
import project.fsurvey.exception.ParameterException;
import project.fsurvey.repositories.SurveyRepository;

@Service
public class SurveyManager implements SurveyService {

    private SurveyRepository surveyRepository;

    @Autowired
    public SurveyManager(SurveyRepository surveyRepository) {
        this.surveyRepository = surveyRepository;
    }

    @Override
    public Survey findById(Long id) {
        return surveyRepository.findById(id).orElseThrow(() -> new NotFoundException("Survey not found with id " + id));
    }

    @Override
    public Survey add(Survey survey) {
        if(Strings.isNullOrEmpty(survey.getName()) ||
                survey.getIssues().isEmpty() ||
                survey.getIssues().stream().anyMatch(issue -> issue.getOptions().isEmpty()))
            throw new ParameterException("Null or empty property found.");
        else
            return surveyRepository.save(survey);
    }

    @Override
    public Survey update(Long id, Survey survey) {
        Survey surveyToUpdate = surveyRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Survey not found."));
        if(!Strings.isNullOrEmpty(survey.getName()) &&
                !surveyToUpdate.getName().equals(survey.getName())){
            surveyToUpdate.setName(survey.getName());
        }
        if(!Strings.isNullOrEmpty(survey.getDescription()) &&
                !surveyToUpdate.getDescription().equals(survey.getDescription())){
            surveyToUpdate.setDescription(survey.getDescription());
        }
        return surveyRepository.save(surveyToUpdate);
    }

    @Override
    public void delete(Long id) {
        surveyRepository.deleteById(id);
    }
}
