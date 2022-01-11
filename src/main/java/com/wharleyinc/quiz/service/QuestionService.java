package com.wharleyinc.quiz.service;

import com.wharleyinc.quiz.domain.QueOption;
import com.wharleyinc.quiz.domain.Question;
import com.wharleyinc.quiz.repository.QuestionRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * Service Implementation for managing {@link Question}.
 */
@Service
@Transactional
public class QuestionService {

    private final Logger log = LoggerFactory.getLogger(QuestionService.class);

    private final QuestionRepository questionRepository;
    private final QueOptionService queOptionService;

    public QuestionService(QuestionRepository questionRepository, QueOptionService queOptionService) {
        this.questionRepository = questionRepository;
        this.queOptionService = queOptionService;
    }

    /**
     * Save a question.
     *
     * @param question the entity to save.
     * @return the persisted entity.
     */
    public Question save(Question question) {
        log.debug("Request to save Question : {}", question);

        Set<QueOption> queOptionSet = question.getQueOptions();
        log.debug("QueOptionSet supplied is {}", queOptionSet);
        Set<QueOption> queOptionSetNew = new HashSet<>();
        QueOption current = new QueOption();

        Iterator<QueOption> setIteratorQueOption = queOptionSet.iterator();
        while (setIteratorQueOption.hasNext()) {
            current = setIteratorQueOption.next();
            log.debug("Current queOption is: {}", current);
            queOptionSetNew.add(queOptionService.save(current));
        }
        Question question2 = question;
        question2.setQueOptions(queOptionSetNew);

        return questionRepository.save(question2);
    }

    /**
     * Partially update a question.
     *
     * @param question the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<Question> partialUpdate(Question question) {
        log.debug("Request to partially update Question : {}", question);

        return questionRepository
                .findById(question.getId())
                .map(existingQuestion -> {
                    if (question.getText() != null) {
                        existingQuestion.setText(question.getText());
                    }
                    if (question.getAttempted() != null) {
                        existingQuestion.setAttempted(question.getAttempted());
                    }

                    return existingQuestion;
                })
                .map(questionRepository::save);
    }

    /**
     * Get all the questions.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<Question> findAll() {
        log.debug("Request to get all Questions");
        return questionRepository.findAll();
    }

    /**
     * Get one question by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<Question> findOne(Long id) {
        log.debug("Request to get Question : {}", id);
        return questionRepository.findById(id);
    }

    /**
     * Delete the question by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Question : {}", id);
        questionRepository.deleteById(id);
    }

    /**
     * Save an Answer to a Question.
     *
     * @param questionId  the question id
     * @param queOptionId id of the option selected
     * @return the Boolean status of request.
     */
    public Boolean submitAnswer(Long questionId, Long queOptionId) {
        log.debug("Request to submit answer: {} to Question : {}", queOptionId, questionId);
        Optional<Question> question = findOne(questionId);
        Optional<QueOption> queOption = queOptionService.findOne(queOptionId);

        Long correctOptionId;
        // check if question exists
        if (question != null) {
            // check if queOption exists
            if (queOption != null) {
                queOption.get().setSelectedOption(true);
                Set<QueOption> queOptionSet = question.get().getQueOptions();

                for (QueOption queOption1 : queOptionSet) {
                    if (queOption1.getAnswerOption().equals(true)) {
                        correctOptionId = queOption1.getId();
                        log.debug("This Option is  {} ", correctOptionId);

                        if (queOption.get().getId().equals(correctOptionId)) {
                            log.debug("This Submitted Option: {} is  {} ", queOption, queOption1);
                            return true;
                        } else {
                            log.debug("The Submitted Option: {} is not correct:  {} ", queOption, queOption1);
                        }
                        return false;
                    }
                }

            }
        }
        return false;
    }
}
