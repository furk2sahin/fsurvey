package project.fsurvey.business.abstracts;

import project.fsurvey.entities.concretes.survey.Option;

import java.util.List;

public interface OptionService{
    Option add(Long issueId, Option option);
    Option update(Long id, Option option);
    void delete(Long id);
    Option findById(Long id);
    List<Option> findByIssueId(Long issueId);
    void addAll(Long issueId, List<Option> options);
}
