package project.fsurvey.business.abstracts;

import project.fsurvey.entities.concretes.survey.Survey;

public interface SurveyService {
    Survey findById(Long id);
}
