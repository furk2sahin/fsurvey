package project.fsurvey.mapper;

import org.mapstruct.IterableMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import project.fsurvey.dtos.IssueDto;
import project.fsurvey.dtos.OptionDto;
import project.fsurvey.entities.concretes.survey.Issue;
import project.fsurvey.entities.concretes.survey.Option;

import java.util.List;

@Mapper(componentModel = "spring")
public interface IssueMapper {

    @Mapping(target = "issue.id", source = "issueId")
    @Named("toOptionEntity")
    Option toOptionEntity(OptionDto optionDto);

    @Mapping(target = "survey.id", source = "surveyId")
    @Mapping(target = "options", source = "options", qualifiedByName = "toOptionEntity")
    @Named("toEntity")
    Issue toEntity(IssueDto issueDto);

    @IterableMapping(qualifiedByName = "toEntity")
    List<Issue> toEntities(List<IssueDto> issueDtos);
}
