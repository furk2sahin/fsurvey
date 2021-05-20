package project.fsurvey.business.abstracts;

import project.fsurvey.entities.concretes.survey.Option;

import java.util.List;

public interface OptionService {
    Option findById(Long id);
    List<Option> findByIssueId(Long issueId);
}
