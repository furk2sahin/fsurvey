package project.fsurvey.business.abstracts;

import project.fsurvey.entities.concretes.survey.Survey;

public interface SurveyService extends CrudService<Survey>{
    Survey findById(Long id);
}
