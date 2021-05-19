package project.fsurvey.entities.concretes.survey;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "issues")
@Data
public class Issue {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String question;

    @Column(nullable = false)
    private int issueOrder;

    @ManyToOne
    @JoinColumn
    @JsonIgnoreProperties("issues")
    private Survey survey;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "issue")
    @LazyCollection(LazyCollectionOption.FALSE)
    private List<Option> options;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "issue")
    @LazyCollection(LazyCollectionOption.FALSE)
    private List<Answer> answers;
}
