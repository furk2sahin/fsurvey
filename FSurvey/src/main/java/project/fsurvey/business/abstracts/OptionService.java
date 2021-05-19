package project.fsurvey.business.abstracts;

import project.fsurvey.entities.concretes.survey.Option;

import java.util.List;
import java.util.Optional;

public interface OptionService {
    Optional<Option> findById(Long id);
    List<Option> findByIssueId(Long issueId);
}
