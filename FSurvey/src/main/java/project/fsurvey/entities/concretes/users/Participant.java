package project.fsurvey.entities.concretes.users;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;
import project.fsurvey.entities.abstracts.User;
import project.fsurvey.entities.concretes.survey.Answer;

import javax.persistence.*;
import java.util.List;

@EqualsAndHashCode(callSuper = false)
@Entity
@Data
@Table(name = "participants")
public class Participant extends User {

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String surname;

    @Column(nullable = false, unique = true)
    private String nationalIdentity;

    @Column(nullable = false)
    private String birthYear;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "participant")
    @LazyCollection(LazyCollectionOption.FALSE)
    private List<Answer> answers;
}