package com.wharleyinc.quiz.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.wharleyinc.quiz.domain.enums.QuizStatus;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * A Quiz.
 */
@Entity
@Table(name = "quiz")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Quiz implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "title")
    private String title;

    @Column(name = "description")
    private String description;

    @Column(name = "quiz_score")
    private int quizScore;

    @ManyToOne
    private User takenBy;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private QuizStatus status;

    @ManyToMany
    @JoinTable(
            name = "rel_quiz__question",
            joinColumns = @JoinColumn(name = "quiz_id"),
            inverseJoinColumns = @JoinColumn(name = "question_id")
    )
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = {"quizzes"}, allowSetters = true)
    private Set<Question> questions = new HashSet<>();

    public Quiz() {
    }

    public Quiz(String title, String description, int quizScore, User takenBy, QuizStatus status, Set<Question> questions) {
        this.title = title;
        this.description = description;
        this.quizScore = quizScore;
        this.takenBy = takenBy;
        this.status = status;
        this.questions = questions;
    }

// jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Quiz id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return this.title;
    }

    public Quiz title(String title) {
        this.setTitle(title);
        return this;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return this.description;
    }

    public Quiz description(String description) {
        this.setDescription(description);
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getQuizScore() {
        return this.quizScore;
    }

    public Quiz quiScore(int quizScore) {
        this.setQuizScore(quizScore);
        return this;
    }

    public void setQuizScore(int quizScore) {
        this.quizScore = quizScore;
    }

    public User getTakenBy() {
        return this.takenBy;
    }

    public void setTakenBy(User user) {
        this.takenBy = user;
    }

    public Quiz takenBy(User user) {
        this.setTakenBy(user);
        return this;
    }

    public QuizStatus getStatus() {
        return this.status;
    }

    public void setStatus(QuizStatus quizStatus) {
        this.status = quizStatus;
    }

    public Quiz status(QuizStatus quizStatus) {
        this.setStatus(quizStatus);
        return this;
    }

    public Set<Question> getQuestions() {
        return this.questions;
    }

    public void setQuestions(Set<Question> questions) {
        this.questions = questions;
    }

    public Quiz questions(Set<Question> questions) {
        this.setQuestions(questions);
        return this;
    }

    public Quiz addQuestion(Question question) {
        this.questions.add(question);
        question.getQuizzes().add(this);
        return this;
    }

    public Quiz removeQuestion(Question question) {
        this.questions.remove(question);
        question.getQuizzes().remove(this);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Quiz)) {
            return false;
        }
        return id != null && id.equals(((Quiz) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Quiz{" +
                "id=" + getId() +
                ", title='" + getTitle() + "'" +
                ", description='" + getDescription() + "'" +
                "}";
    }
}
