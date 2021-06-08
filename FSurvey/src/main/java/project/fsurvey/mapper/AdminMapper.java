package project.fsurvey.mapper;

import org.mapstruct.IterableMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Named;
import project.fsurvey.dtos.UserDto;
import project.fsurvey.dtos.UserGetDto;
import project.fsurvey.entities.concretes.users.Admin;

import java.util.List;

@Mapper(componentModel = "spring")
public interface AdminMapper {

    Admin postDtoToAdmin(UserDto userPostDto);

    @Named("toGetDto")
    UserGetDto adminToGetDto(Admin admin);

    @IterableMapping(qualifiedByName = "toGetDto")
    List<UserGetDto> adminsToGetDtos(List<Admin> admins);
}
