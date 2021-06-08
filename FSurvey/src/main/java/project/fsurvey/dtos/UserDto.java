package project.fsurvey.dtos;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Data
public class UserDto {

    @Size(min = 6, max = 30, message = "Username length should be between 6-30.")
    @NotBlank(message = "Username cannot be empty.")
    private String username;

    @Size(min = 6, max = 30, message = "Password length should be between 6-30.")
    @NotBlank(message = "Username cannot be empty.")
    private String password;

    @NotBlank(message = "Role cannot be empty.")
    private String role;

    @Size(min = 2, message = "Name length cannot be less than 2.")
    @NotBlank(message = "Name cannot be empty.")
    private String name;

    @NotBlank(message = "Surname cannot be empty.")
    @Size(min = 2, message = "Surname length cannot be less than 2.")
    private String surname;

    @NotBlank(message = "National identity cannot be empty.")
    @Size(min = 11, max = 11, message = "National identity length must be 11.")
    @Pattern(regexp = "^[1-9][0-9]{9}[02468]$",
            message = "National identity can't start with 0 or contain text.")
    private String nationalIdentity;

    @NotBlank
    @Size(min = 4, max = 4, message = "Birth year length must be 4.")
    @Pattern(regexp = "^(19|20)[0-9]{2}$",
            message = "Birth year should be only 4 characters with numbers.")
    private String birthYear;

}
