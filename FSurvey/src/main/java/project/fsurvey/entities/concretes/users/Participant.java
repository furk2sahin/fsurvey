package project.fsurvey.entities.concretes.users;

import lombok.Data;
import lombok.EqualsAndHashCode;
import project.fsurvey.entities.abstracts.User;

import javax.persistence.*;

@EqualsAndHashCode(callSuper = false)
@Entity
@Data
@Table(name = "participants")
public class Participant extends User {

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String surname;

    @Column(unique = true)
    private String nationalIdentity;

    @Column
    private String birthYear;
}