package project.fsurvey.entities.concretes.users;

import lombok.*;
import project.fsurvey.entities.abstracts.User;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@EqualsAndHashCode(callSuper = false)
@Entity
@Data
@Table(name = "admins")
@AllArgsConstructor
@NoArgsConstructor
public class Admin extends User {
    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String surname;

    @Column(nullable = false, unique = true)
    private String nationalIdentity;

    @Column(nullable = false)
    private String birthYear;
}
