package project.fsurvey.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import project.fsurvey.dtos.IssueDto;
import project.fsurvey.dtos.OptionDto;
import project.fsurvey.dtos.SurveyDto;
import project.fsurvey.entities.concretes.survey.Issue;
import project.fsurvey.entities.concretes.survey.Option;
import project.fsurvey.entities.concretes.survey.Survey;

@Mapper(componentModel = "spring")
public interface SurveyMapper {

    @Mapping(target = "issue.id", source = "issueId")
    @Named("toOptionEntity")
    Option toOptionEntity(OptionDto optionDto);

    @Mapping(target = "survey.id", source = "surveyId")
    @Mapping(target = "options", source = "options", qualifiedByName = "toOptionEntity")
    @Named("toIssueEntity")
    Issue toIssueEntity(IssueDto issueDto);

    @Mapping(target = "issues", source = "issue", qualifiedByName = "toIssueEntity")
    Survey toEntity(SurveyDto surveyDto);
}
