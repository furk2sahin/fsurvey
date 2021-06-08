package project.fsurvey.business.abstracts;

import org.springframework.http.ResponseEntity;
import project.fsurvey.core.results.DataResult;
import project.fsurvey.core.results.Result;
import project.fsurvey.dtos.UserDto;
import project.fsurvey.dtos.UserGetDto;

public interface ParticipantService{
    ResponseEntity<DataResult<UserGetDto>> add(UserDto userPostDto);
    ResponseEntity<DataResult<UserGetDto>> update(Long id, UserDto userPostDto);
    ResponseEntity<Result> delete(Long id);
    ResponseEntity<DataResult<UserGetDto>> findById(Long id);
    boolean existById(Long id);
}
