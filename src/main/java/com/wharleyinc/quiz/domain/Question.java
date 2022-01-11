package com.wharleyinc.quiz.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * A Question.
 */
@Entity
@Table(name = "question")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Question implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "text")
    private String text;

    @JsonIgnore
    @Column(name = "attempted")
    private Boolean attempted;

    @JsonIgnore
    @Column(name = "question_correct")
    private Boolean questionCorrect;

    @OneToMany(mappedBy = "question")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = {"question"}, allowSetters = true)
    private Set<QueOption> queOptions = new HashSet<>();

    @ManyToMany(mappedBy = "questions")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = {"takenBy", "questions"}, allowSetters = true)
    private Set<Quiz> quizzes = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Question id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getText() {
        return this.text;
    }

    public Question text(String text) {
        this.setText(text);
        return this;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Boolean getAttempted() {
        return this.attempted;
    }

    public Question attempted(Boolean attempted) {
        this.setAttempted(attempted);
        return this;
    }

    public void setAttempted(Boolean attempted) {
        this.attempted = attempted;
    }

    public Boolean getQuestionCorrect() {
        return this.questionCorrect;
    }

    public Question questionCorrect(Boolean questionCorrect) {
        this.setQuestionCorrect(questionCorrect);
        return this;
    }

    public void setQuestionCorrect(Boolean questionCorrect) {
        this.questionCorrect = questionCorrect;
    }

    public Set<QueOption> getQueOptions() {
        return this.queOptions;
    }

    public void setQueOptions(Set<QueOption> queOptions) {
        if (this.queOptions != null) {
            this.queOptions.forEach(i -> i.setQuestion(null));
        }
        if (queOptions != null) {
            queOptions.forEach(i -> i.setQuestion(this));
        }
        this.queOptions = queOptions;
    }

    public Question queOptions(Set<QueOption> queOptions) {
        this.setQueOptions(queOptions);
        return this;
    }

    public Question addQueOption(QueOption queOption) {
        this.queOptions.add(queOption);
        queOption.setQuestion(this);
        return this;
    }

    public Question removeQueOption(QueOption queOption) {
        this.queOptions.remove(queOption);
        queOption.setQuestion(null);
        return this;
    }

    public Set<Quiz> getQuizzes() {
        return this.quizzes;
    }

    public void setQuizzes(Set<Quiz> quizzes) {
        if (this.quizzes != null) {
            this.quizzes.forEach(i -> i.removeQuestion(this));
        }
        if (quizzes != null) {
            quizzes.forEach(i -> i.addQuestion(this));
        }
        this.quizzes = quizzes;
    }

    public Question quizzes(Set<Quiz> quizzes) {
        this.setQuizzes(quizzes);
        return this;
    }

    public Question addQuiz(Quiz quiz) {
        this.quizzes.add(quiz);
        quiz.getQuestions().add(this);
        return this;
    }

    public Question removeQuiz(Quiz quiz) {
        this.quizzes.remove(quiz);
        quiz.getQuestions().remove(this);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Question)) {
            return false;
        }
        return id != null && id.equals(((Question) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Question{" +
                "id=" + getId() +
                ", text='" + getText() + "'" +
                ", attempted='" + getAttempted() + "'" +
                ", questionCorrect='" + getQuestionCorrect() + "'" +
                "}";
    }
}
