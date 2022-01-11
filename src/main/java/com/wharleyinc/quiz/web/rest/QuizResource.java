package com.wharleyinc.quiz.web.rest;

import com.wharleyinc.quiz.domain.Question;
import com.wharleyinc.quiz.domain.Quiz;
import com.wharleyinc.quiz.security.SecurityUtils;
import com.wharleyinc.quiz.service.QuizService;
import com.wharleyinc.quiz.utils.HeaderUtil;
import com.wharleyinc.quiz.utils.PaginationUtil;
import com.wharleyinc.quiz.web.rest.errors.BadRequestAlertException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

/**
 * REST controller for managing {@link Quiz}.
 */
@RestController
@RequestMapping("/api")
public class QuizResource {

    private static class QuizResourceException extends RuntimeException {

        private QuizResourceException(String message) {
            super(message);
        }
    }

    private final Logger log = LoggerFactory.getLogger(QuizResource.class);

    private static final String ENTITY_NAME = "quiz";

    @Value("${spring.application.name}")
    private String applicationName;

    private final QuizService quizService;

    public QuizResource(QuizService quizService) {
        this.quizService = quizService;
    }

    /**
     * {@code POST  /quizzes} : Create a new quiz.
     *
     * @param quiz the quiz to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new quiz, or with status {@code 400 (Bad Request)} if the quiz has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/quizzes")
    public ResponseEntity<Quiz> createQuiz(@RequestBody Quiz quiz) throws URISyntaxException {
        log.debug("REST request to save Quiz : {}", quiz);
        if (quiz.getId() != null) {
            throw new BadRequestAlertException("A new quiz cannot already have an ID", ENTITY_NAME, "idexists");
        }

        quiz.getQuestions().forEach(question -> question.getQueOptions().forEach(
                queOption -> queOption.setQuestion(question)));
        Quiz result = quizService.save(quiz);

        return ResponseEntity
                .created(new URI("/api/quizzes/" + result.getId()))
                .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
                .body(result);
    }

    /**
     * {@code GET  /quiz-questions} : get all the quiz questions.
     *
     * @param id       the quiz id.
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of questions in body.
     */
    @GetMapping("/quiz-questions")
    public ResponseEntity<List<Question>> getQuizQuestions(@RequestParam Long id, Pageable pageable) {
        log.debug("REST request to get a page of Questions");
        String userName = SecurityUtils.getCurrentUserLogin().orElseThrow(() -> new QuizResourceException("No User found for this quiz resource"));
        Page<Question> page = quizService.takeQuiz(userName, id, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /mark-quiz} : mark the quiz questions.
     *
     * @param id the quiz id.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the quiz score in body.
     */
    @GetMapping("/mark-quiz")
    public ResponseEntity<Integer> markQuizQuestions(@RequestParam Long id) {
        log.debug("REST request to mark a Quiz");
        int result = quizService.markQuiz(id);
        return ResponseEntity.ok(result);
    }

}
