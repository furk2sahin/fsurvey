package project.fsurvey.mapper;

import org.mapstruct.IterableMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Named;
import project.fsurvey.dtos.UserDto;
import project.fsurvey.dtos.UserGetDto;
import project.fsurvey.entities.concretes.users.Participant;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ParticipantMapper {

    Participant postDtoToParticipant(UserDto userPostDto);

    @Named("toGetDto")
    UserGetDto participantToGetDto(Participant participant);

    @IterableMapping(qualifiedByName = "toGetDto")
    List<UserGetDto> participantsToGetDtos(List<Participant> participants);
}
