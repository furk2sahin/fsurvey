package project.fsurvey.dto;

import lombok.Data;

@Data
public class ParticipantDto {
    private String username;
    private String password;
    private String role;
    private String name;
    private String surname;
    private String nationalIdentity;
    private String birthYear;
}
