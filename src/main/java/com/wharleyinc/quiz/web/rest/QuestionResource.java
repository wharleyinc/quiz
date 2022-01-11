package com.wharleyinc.quiz.web.rest;

import com.wharleyinc.quiz.domain.Question;
import com.wharleyinc.quiz.service.QuestionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * REST controller for managing {@link Question}.
 */
@RestController
@RequestMapping("/api")
public class QuestionResource {

    private final Logger log = LoggerFactory.getLogger(QuestionResource.class);

    private final QuestionService questionService;

    public QuestionResource(QuestionService questionService) {
        this.questionService = questionService;
    }

    /**
     * {@code GET  /submitAnswer} : submit answer to quiz question.
     *
     * @param questionId  the question id.
     * @param queOptionId the queOption id.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the status of submission in body.
     */
    @PostMapping("/submit-answer")
    public ResponseEntity<Boolean> submitAnswer(@RequestParam Long questionId, @RequestParam Long queOptionId) {
        log.debug("REST request to submit answer : {}  and {}", questionId, queOptionId);

        Boolean result = questionService.submitAnswer(questionId, queOptionId);

        return ResponseEntity.ok(result);
    }
}
