package project.fsurvey.business.concretes;

import com.google.common.base.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import project.fsurvey.core.results.*;
import project.fsurvey.dtos.SurveyDto;
import project.fsurvey.entities.concretes.survey.Survey;
import project.fsurvey.mapper.SurveyMapper;
import project.fsurvey.repositories.SurveyRepository;

import java.util.List;

@Service
public class SurveyManager implements project.fsurvey.business.abstracts.SurveyService {

    private SurveyRepository surveyRepository;
    private SurveyMapper surveyMapper;
    private Environment environment;

    @Autowired
    public SurveyManager(SurveyRepository surveyRepository,
                         SurveyMapper surveyMapper,
                         Environment environment) {
        this.surveyRepository = surveyRepository;
        this.surveyMapper = surveyMapper;
        this.environment = environment;
    }


    @Override
    public ResponseEntity<DataResult<Survey>> add(SurveyDto surveyDto) {

        if(surveyDto.getIssue().isEmpty())
            return ResponseEntity.badRequest().body(new ErrorDataResult<>(environment.getProperty("ISSUES_CANNOT_BE_EMPTY")));

        if(surveyDto.getIssue().stream().anyMatch(issue -> issue.getOptions().isEmpty()))
            return ResponseEntity.badRequest().body(new ErrorDataResult<>(environment.getProperty("OPTIONS_CANNOT_BE_EMPTY")));

        Survey survey = surveyMapper.toEntity(surveyDto);
        survey.getIssues().forEach(issue -> {
            issue.setSurvey(survey);
            issue.getOptions().forEach(option -> option.setIssue(issue));
        });
        return ResponseEntity.ok(new SuccessDataResult<>(
                surveyRepository.save(survey),
                environment.getProperty("SURVEY_ADDED")
        ));
    }

    @Override
    public ResponseEntity<DataResult<Survey>> update(Long id, SurveyDto surveyDto) {
        Survey surveyToUpdate = surveyRepository.findById(id).orElse(null);
        if(surveyToUpdate == null)
            return ResponseEntity.badRequest().body(new ErrorDataResult(environment.getProperty("SURVEY_NOT_FOUND")));

        if(!surveyToUpdate.getName().equals(surveyDto.getName())){
            surveyToUpdate.setName(surveyDto.getName());
        }

        if(!Strings.isNullOrEmpty(surveyDto.getDescription()) &&
                !surveyToUpdate.getDescription().equals(surveyDto.getDescription())){
            surveyToUpdate.setDescription(surveyDto.getDescription());
        }
        return ResponseEntity.ok(new SuccessDataResult<>(
                surveyRepository.save(surveyToUpdate),
                environment.getProperty("SURVEY_UPDATED")
        ));
    }

    @Override
    public ResponseEntity<DataResult<Survey>> findById(Long id) {
        Survey survey = surveyRepository.findById(id).orElse(null);
        if(survey == null)
            return ResponseEntity.badRequest().body(new ErrorDataResult<>(environment.getProperty("SURVEY_NOT_FOUND")));
        return ResponseEntity.ok(new SuccessDataResult<>(survey, environment.getProperty("SURVEY_FOUND")));
    }

    @Override
    public ResponseEntity<Result> delete(Long id) {
        if(!surveyRepository.existsById(id))
            return ResponseEntity.ok(new ErrorResult(environment.getProperty("SURVEY_NOT_FOUND")));
        surveyRepository.deleteById(id);
        return ResponseEntity.ok(new SuccessResult(environment.getProperty("SURVEY_DELETED")));
    }

    @Override
    public ResponseEntity<DataResult<List<Survey>>> findAll() {
        List<Survey> surveys = surveyRepository.findAll();
        if(surveys.isEmpty()){
            return ResponseEntity.badRequest().body(new ErrorDataResult<>(environment.getProperty("SURVEY_NOT_FOUND")));
        }
        return ResponseEntity.ok(new SuccessDataResult<>(surveys,
                environment.getProperty(environment.getProperty("SURVEY_LISTED"))));
    }

    @Override
    public boolean existById(Long id) {
        return surveyRepository.existsById(id);
    }
}