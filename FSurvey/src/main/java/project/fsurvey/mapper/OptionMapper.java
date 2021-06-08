package project.fsurvey.mapper;

import org.mapstruct.IterableMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import project.fsurvey.dtos.OptionDto;
import project.fsurvey.entities.concretes.survey.Option;

import java.util.List;

@Mapper(componentModel = "spring")
public interface OptionMapper {

    @Mapping(target = "issue.id", source = "issueId")
    @Named("toEntity")
    Option toEntity(OptionDto optionDto);

    @IterableMapping(qualifiedByName = "toEntity")
    List<Option> toEntities(List<OptionDto> optionDtos);
}
