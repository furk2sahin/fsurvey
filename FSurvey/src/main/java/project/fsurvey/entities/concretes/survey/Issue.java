package project.fsurvey.entities.concretes.survey;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "issues")
@Data
public class Issue {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    @NotBlank(message = "Question cannot be empty.")
    private String question;

    @Column(nullable = false)
    @Pattern(regexp = "^[1-9]{1}$",
            message = "Wrong issue order format")
    private int issueOrder;

    @ManyToOne
    @JoinColumn(foreignKey = @ForeignKey(name = "fk_issue_survey"))
    @JsonIgnoreProperties("issues")
    private Survey survey;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "issue")
    @LazyCollection(LazyCollectionOption.FALSE)
    @JsonIgnoreProperties("issue")
    private List<Option> options = new ArrayList<>();

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "issue")
    @LazyCollection(LazyCollectionOption.FALSE)
    @JsonIgnoreProperties("issue")
    private List<Answer> answers = new ArrayList<>();
}
