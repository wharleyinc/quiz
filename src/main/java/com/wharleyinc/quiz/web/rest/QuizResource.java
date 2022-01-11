package com.wharleyinc.quiz.web.rest;

import com.wharleyinc.quiz.domain.Question;
import com.wharleyinc.quiz.domain.Quiz;
import com.wharleyinc.quiz.repository.QuizRepository;
import com.wharleyinc.quiz.security.SecurityUtils;
import com.wharleyinc.quiz.service.QuestionService;
import com.wharleyinc.quiz.service.QuizService;
import com.wharleyinc.quiz.utils.HeaderUtil;
import com.wharleyinc.quiz.utils.PaginationUtil;
import com.wharleyinc.quiz.utils.ResponseUtil;
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
import java.util.Objects;
import java.util.Optional;

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

    private final QuizRepository quizRepository;

    private final QuestionService questionService;

    public QuizResource(QuizService quizService, QuizRepository quizRepository, QuestionService questionService) {
        this.quizService = quizService;
        this.quizRepository = quizRepository;
        this.questionService = questionService;
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

        System.out.println(quiz.toString());
        quiz.getQuestions().forEach(question -> question.getQueOptions().forEach(
                queOption -> queOption.setQuestion(question)));
        Quiz result = quizService.save(quiz);


//        Quiz result = quizService.save(quiz);
        return ResponseEntity
                .created(new URI("/api/quizzes/" + result.getId()))
                .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
                .body(result);
    }

    /**
     * {@code PUT  /quizzes/:id} : Updates an existing quiz.
     *
     * @param id   the id of the quiz to save.
     * @param quiz the quiz to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated quiz,
     * or with status {@code 400 (Bad Request)} if the quiz is not valid,
     * or with status {@code 500 (Internal Server Error)} if the quiz couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/quizzes/{id}")
    public ResponseEntity<Quiz> updateQuiz(@PathVariable(value = "id", required = false) final Long id, @RequestBody Quiz quiz)
            throws URISyntaxException {
        log.debug("REST request to update Quiz : {}, {}", id, quiz);
        if (quiz.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, quiz.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!quizRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Quiz result = quizService.save(quiz);
        return ResponseEntity
                .ok()
                .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, quiz.getId().toString()))
                .body(result);
    }

    /**
     * {@code PATCH  /quizzes/:id} : Partial updates given fields of an existing quiz, field will ignore if it is null
     *
     * @param id   the id of the quiz to save.
     * @param quiz the quiz to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated quiz,
     * or with status {@code 400 (Bad Request)} if the quiz is not valid,
     * or with status {@code 404 (Not Found)} if the quiz is not found,
     * or with status {@code 500 (Internal Server Error)} if the quiz couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/quizzes/{id}", consumes = {"application/json", "application/merge-patch+json"})
    public ResponseEntity<Quiz> partialUpdateQuiz(@PathVariable(value = "id", required = false) final Long id, @RequestBody Quiz quiz)
            throws URISyntaxException {
        log.debug("REST request to partial update Quiz partially : {}, {}", id, quiz);
        if (quiz.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, quiz.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!quizRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Quiz> result = quizService.partialUpdate(quiz);

        return ResponseUtil.wrapOrNotFound(
                result,
                HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, quiz.getId().toString())
        );
    }

    /**
     * {@code GET  /quizzes} : get all the quizzes.
     *
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of quizzes in body.
     */
    @GetMapping("/quizzes")
    public List<Quiz> getAllQuizzes(@RequestParam(required = false, defaultValue = "false") boolean eagerload) {
        log.debug("REST request to get all Quizzes");
        return quizService.findAll();
    }

    /**
     * {@code GET  /quizzes/:id} : get the "id" quiz.
     *
     * @param id the id of the quiz to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the quiz, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/quizzes/{id}")
    public ResponseEntity<Quiz> getQuiz(@PathVariable Long id) {
        log.debug("REST request to get Quiz : {}", id);
        Optional<Quiz> quiz = quizService.findOne(id);
        return ResponseUtil.wrapOrNotFound(quiz);
    }

    /**
     * {@code DELETE  /quizzes/:id} : delete the "id" quiz.
     *
     * @param id the id of the quiz to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/quizzes/{id}")
    public ResponseEntity<Void> deleteQuiz(@PathVariable Long id) {
        log.debug("REST request to delete Quiz : {}", id);
        quizService.delete(id);
        return ResponseEntity
                .noContent()
                .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
                .build();
    }

//
//    /**
//     * {@code GET  /submitAnswer} : submit answer to quiz question.
//     *
//     * @param questionId the question id.
//     * @param answerId the question id.
//     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the status of submission in body.
//     */
//    @PostMapping("/submitAnswer")
//    public ResponseEntity<Boolean> submitAnswer(Long questionId, Long answerId) {
//        log.debug("REST request to submit Answer to question");
//        String userName = SecurityUtils.getCurrentUserLogin().orElseThrow(() -> new QuizResourceException("No User found for this quiz resource"));
//        Boolean result = questionService.submitAnswer(questionId, answerId);
//
//        return ResponseEntity.ok().body(result);
//    }


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
