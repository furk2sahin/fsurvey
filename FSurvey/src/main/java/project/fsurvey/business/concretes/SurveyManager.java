package project.fsurvey.business.concretes;

import com.google.common.base.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import project.fsurvey.core.results.*;
import project.fsurvey.dtos.SurveyDto;
import project.fsurvey.entities.concretes.survey.Survey;
import project.fsurvey.mapper.SurveyMapper;
import project.fsurvey.repositories.SurveyRepository;

@Service
public class SurveyManager implements project.fsurvey.business.abstracts.SurveyService {

    private SurveyRepository surveyRepository;
    private SurveyMapper surveyMapper;

    @Autowired
    public SurveyManager(SurveyRepository surveyRepository, SurveyMapper surveyMapper) {
        this.surveyRepository = surveyRepository;
        this.surveyMapper = surveyMapper;
    }


    @Override
    public ResponseEntity<DataResult<Survey>> add(SurveyDto surveyDto) {
        if(surveyDto.getIssue().isEmpty())
            return ResponseEntity.badRequest().body(new ErrorDataResult<>("Issues cannot be empty."));

        if(surveyDto.getIssue().stream().anyMatch(issue -> issue.getOptions().isEmpty()))
            return ResponseEntity.badRequest().body(new ErrorDataResult<>("Options cannot be empty"));

        Survey survey = surveyMapper.toEntity(surveyDto);
        survey.getIssues().forEach(issue -> {
            issue.setSurvey(survey);
            issue.getOptions().forEach(option -> option.setIssue(issue));
        });
        return ResponseEntity.ok(new SuccessDataResult<>(surveyRepository.save(survey), "Survey added successfully."));
    }

    @Override
    public ResponseEntity<DataResult<Survey>> update(Long id, SurveyDto surveyDto) {
        Survey surveyToUpdate = surveyRepository.findById(id).orElse(null);
        if(surveyToUpdate == null)
            return ResponseEntity.badRequest().body(new ErrorDataResult("No survey found with given id " + id));

        if(!surveyToUpdate.getName().equals(surveyDto.getName())){
            surveyToUpdate.setName(surveyDto.getName());
        }

        if(!Strings.isNullOrEmpty(surveyDto.getDescription()) &&
                !surveyToUpdate.getDescription().equals(surveyDto.getDescription())){
            surveyToUpdate.setDescription(surveyDto.getDescription());
        }
        return ResponseEntity.ok(new SuccessDataResult<>(surveyRepository.save(surveyToUpdate), "Data updated successfully"));
    }

    @Override
    public ResponseEntity<DataResult<Survey>> findById(Long id) {
        Survey survey = surveyRepository.findById(id).orElse(null);
        if(survey == null)
            return ResponseEntity.badRequest().body(new ErrorDataResult<>("No survey were found with id " + id));
        return ResponseEntity.ok(new SuccessDataResult<>(survey, "Survey found!"));
    }

    @Override
    public ResponseEntity<Result> delete(Long id) {
        if(!surveyRepository.existsById(id))
            return ResponseEntity.ok(new ErrorResult("No survey were found with given id"));
        surveyRepository.deleteById(id);
        return ResponseEntity.ok(new SuccessResult("Survey successfully deleted with given id"));
    }

    @Override
    public boolean existById(Long id) {
        return surveyRepository.existsById(id);
    }
}
