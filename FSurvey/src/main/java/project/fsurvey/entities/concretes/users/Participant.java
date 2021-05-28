package project.fsurvey.entities.concretes.users;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;
import project.fsurvey.entities.abstracts.User;
import project.fsurvey.entities.concretes.survey.Answer;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.List;

@EqualsAndHashCode(callSuper = false)
@Entity
@Data
@Table(name = "participants")
public class Participant extends User {

    @Column(nullable = false)
    @Size(min = 2, message = "Name length cannot be less than 2.")
    private String name;

    @Column(nullable = false)
    @Size(min = 2, message = "Surname length cannot be less than 2.")
    private String surname;

    @Column(nullable = false, unique = true)
    @NotBlank(message = "National identity cannot be empty.")
    @Size(min = 11, max = 11, message = "National identity length must be 11.")
    @Pattern(regexp = "^[1-9]{1}[0-9]{9}[02468]{1}$",
            message = "National identity can't start with 0 or contain text.")
    private String nationalIdentity;

    @Column(nullable = false)
    @NotBlank(message = "Birth year cannot be empty.")
    @Size(min = 4, max = 4, message = "Birth year length must be 4.")
    @Pattern(regexp = "^[12]{1}[0-9]{3}$",
            message = "Wrong email format")
    private String birthYear;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "participant")
    @LazyCollection(LazyCollectionOption.FALSE)
    private List<Answer> answers;
}