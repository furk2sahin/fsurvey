package project.fsurvey.entities.concretes.users;

import lombok.*;
import project.fsurvey.entities.abstracts.User;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@EqualsAndHashCode(callSuper = false)
@Entity
@Data
@Table(name = "admins")
@AllArgsConstructor
@NoArgsConstructor
public class Admin extends User {
    @Column(nullable = false)
    @Size(min = 2, message = "Name length cannot be less than 2.")
    @NotBlank(message = "Name cannot be empty.")
    private String name;

    @Column(nullable = false)
    @NotBlank(message = "Surname cannot be empty.")
    @Size(min = 2, message = "Surname length cannot be less than 2.")
    private String surname;

    @Column(nullable = false, unique = true)
    @NotBlank(message = "National identity cannot be empty.")
    @Pattern(regexp = "^[1-9]{1}[0-9]{9}[02468]{1}$",
            message = "National identity can't start with 0 and end with even numbers or contain text.")
    private String nationalIdentity;

    @Column(nullable = false)
    @Size(min = 4, max = 4, message = "Birth year length must be 4.")
    @NotBlank(message = "Birth year cannot be empty.")
    @Pattern(regexp = "^[12]{1}[0-9]{3}$",
            message = "Wrong email format")
    private String birthYear;
}
