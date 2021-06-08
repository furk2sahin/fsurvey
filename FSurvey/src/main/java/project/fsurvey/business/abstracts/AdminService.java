package project.fsurvey.business.abstracts;

import org.springframework.http.ResponseEntity;
import project.fsurvey.core.results.DataResult;
import project.fsurvey.core.results.Result;
import project.fsurvey.dtos.UserGetDto;
import project.fsurvey.dtos.UserDto;

public interface AdminService {
    ResponseEntity<DataResult<UserGetDto>> add(UserDto userPostDto);
    ResponseEntity<DataResult<UserGetDto>> update(Long id, UserDto userUpdateDto);
    ResponseEntity<Result> delete(Long id);
    ResponseEntity<DataResult<UserGetDto>> findById(Long id);
}
