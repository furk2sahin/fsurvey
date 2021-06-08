package project.fsurvey.dtos;

import lombok.Data;

import java.util.Date;

@Data
public class UserGetDto {
    private Long id;
    private Date createDate;
    private String username;
    private String password;
    private String role;
    private String name;
    private String surname;
    private String nationalIdentity;
    private String birthYear;
}
