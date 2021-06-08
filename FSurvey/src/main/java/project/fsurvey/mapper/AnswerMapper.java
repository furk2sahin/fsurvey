package project.fsurvey.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import project.fsurvey.dtos.AnswerDto;
import project.fsurvey.entities.concretes.survey.Answer;

@Mapper(componentModel = "spring")
public interface AnswerMapper {

    @Mapping(target = "participant.id", source = "participantId")
    @Mapping(target = "survey.id", source = "surveyId")
    @Mapping(target = "issue.id", source = "issueId")
    @Mapping(target = "option.id", source = "optionId")
    Answer toEntity(AnswerDto answerDto);
}
