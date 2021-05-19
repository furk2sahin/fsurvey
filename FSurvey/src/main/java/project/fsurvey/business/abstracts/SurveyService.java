package project.fsurvey.business.abstracts;

import project.fsurvey.entities.concretes.survey.Survey;

import java.util.Optional;

public interface SurveyService {
    Optional<Survey> findById(Long id);
}
