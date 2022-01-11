package com.wharleyinc.quiz.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import java.io.Serializable;


@Entity
@Table(name = "que_option")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class QueOption implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "text")
    private String text;

    //    @JsonIgnore
    @Column(name = "answer_option")
    private Boolean answerOption;

    @Column(name = "selected_option")
    private Boolean selectedOption;

    @ManyToOne
    @JsonIgnoreProperties(value = {"queOptions", "quizzes"}, allowSetters = true)
    private Question question;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public QueOption id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getText() {
        return this.text;
    }

    public QueOption text(String text) {
        this.setText(text);
        return this;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Boolean getAnswerOption() {
        return this.answerOption;
    }

    public QueOption answerOption(Boolean answerOption) {
        this.setAnswerOption(answerOption);
        return this;
    }

    public void setAnswerOption(Boolean answerOption) {
        this.answerOption = answerOption;
    }

    public Boolean getSelectedOption() {
        return this.selectedOption;
    }

    public QueOption selectedOption(Boolean selectedOption) {
        this.setSelectedOption(selectedOption);
        return this;
    }

    public void setSelectedOption(Boolean selectedOption) {
        this.selectedOption = selectedOption;
    }

    public Question getQuestion() {
        return this.question;
    }

    public void setQuestion(Question question) {
        this.question = question;
    }

    public QueOption question(Question question) {
        this.setQuestion(question);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof QueOption)) {
            return false;
        }
        return id != null && id.equals(((QueOption) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "QueOption{" +
                "id=" + getId() +
                ", text='" + getText() + "'" +
                ", selectedOption='" + getSelectedOption() + "'" +
                "}";
    }
}
