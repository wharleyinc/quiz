package com.wharleyinc.quiz.service;

import com.wharleyinc.quiz.domain.QueOption;
import com.wharleyinc.quiz.domain.Question;
import com.wharleyinc.quiz.domain.Quiz;
import com.wharleyinc.quiz.domain.User;
import com.wharleyinc.quiz.domain.enums.QuizStatus;
import com.wharleyinc.quiz.repository.QuizRepository;

import java.util.*;

import com.wharleyinc.quiz.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Quiz}.
 */
@Service
@Transactional
public class QuizService {

    private final Logger log = LoggerFactory.getLogger(QuizService.class);

    private static final int QUESTION_SCORE = 1;

    private final QuizRepository quizRepository;
    private final QuestionService questionService;
    private final UserRepository userRepository;

    public QuizService(QuizRepository quizRepository, QuestionService questionService, UserRepository userRepository) {
        this.quizRepository = quizRepository;
        this.questionService = questionService;
        this.userRepository = userRepository;
    }

    /**
     * Save a quiz.
     *
     * @param quiz the entity to save.
     * @return the persisted entity.
     */
    public Quiz save(Quiz quiz) {
        log.debug("Request to save Quiz : {}", quiz);

        quiz.getQuestions().forEach(question -> questionService.save(question));

        return quizRepository.save(quiz);
    }

    /**
     * Partially update a quiz.
     *
     * @param quiz the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<Quiz> partialUpdate(Quiz quiz) {
        log.debug("Request to partially update Quiz : {}", quiz);

        return quizRepository
                .findById(quiz.getId())
                .map(existingQuiz -> {
                    if (quiz.getTitle() != null) {
                        existingQuiz.setTitle(quiz.getTitle());
                    }
                    if (quiz.getDescription() != null) {
                        existingQuiz.setDescription(quiz.getDescription());
                    }

                    return existingQuiz;
                })
                .map(quizRepository::save);
    }

    /**
     * Get all the quizzes.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<Quiz> findAll() {
        log.debug("Request to get all Quizzes");
        return quizRepository.findAllWithEagerRelationships();
    }

    /**
     * Get all the quizzes with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<Quiz> findAllWithEagerRelationships(Pageable pageable) {
        return quizRepository.findAllWithEagerRelationships(pageable);
    }

    /**
     * Get one quiz by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<Quiz> findOne(Long id) {
        log.debug("Request to get Quiz : {}", id);
        return quizRepository.findOneWithEagerRelationships(id);
    }

    /**
     * Delete the quiz by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Quiz : {}", id);
        quizRepository.deleteById(id);
    }


    /**
     * Mark the quiz by Quiz id.
     *
     * @param quizId the id of the entity.
     */
    public int markQuiz(Long quizId) {
        log.debug("Request to mark a Quiz: {}", quizId);

        Quiz quiz = quizRepository.findById(quizId).get();

        // TODO: if quiz had been marked, return quizScore
        if (quiz.getStatus().equals(QuizStatus.COMPLETED)) {
            log.debug("Quiz had already been solved. Try another quiz.");
            return 0;
        }

        Set<Question> questionSet = quiz.getQuestions(); // get questions

        // iterate through the questions and count number of correct answers.
        Iterator<Question> setIteratorQuestion = questionSet.iterator();
        Set<QueOption> queOptionSet = new HashSet<>();
        Question question;
        QueOption queOptionSelected;
//        QueOption2 queOptionCorrect = new QueOption2();
        while (setIteratorQuestion.hasNext()) {
            question = setIteratorQuestion.next();
            queOptionSet = question.getQueOptions();

            Iterator<QueOption> setIteratorQueOption = queOptionSet.iterator();
            while (setIteratorQueOption.hasNext()) {
                queOptionSelected = setIteratorQueOption.next();
                // check if option selected is the same as answer option
                if (queOptionSelected.getSelectedOption() && queOptionSelected.getAnswerOption()) {
                    // mark question as correct and add score to quiz score
                    question.setQuestionCorrect(true);
                    quiz.setQuizScore(quiz.getQuizScore() + QUESTION_SCORE);
                    break;
                }
                // answer was wrong
                question.setQuestionCorrect(false);
            } // end inner while

            // persist question to db
            questionService.save(question);
        }   // end outer while

        // mark quiz as completed
        quiz.setStatus(QuizStatus.COMPLETED);

        // persist quiz to db
        quizRepository.save(quiz);

        return quiz.getQuizScore();
    }

    /**
     * Take quiz.
     *
     * @param user   the current user.
     * @param quizId the current user.
     * @return the list of Questions.
     */
    @Transactional(readOnly = true)
    public Page<Question> takeQuiz(String user, Long quizId, Pageable pageable) {
        log.debug("Request to take Quiz");

        Quiz quiz = quizRepository.findById(quizId).get();
        if (quiz != null) {
            User currentUser = userRepository.findOneByUserName(user).get();
            quiz.setTakenBy(currentUser);
            Quiz activeQuiz = quizRepository.save(quiz);

            // convert set object to List object
            List<Question> targetList = new ArrayList<>(activeQuiz.getQuestions());
            // convert list object to Page object
            Page<Question> quizQuestions = createPageFromList(targetList, pageable);

            return quizQuestions;
        }
        return null;
    }


    /*    */

    /**
     * Find active quiz by currently logged-in user
     * <p>
     * Returns an existing quiz or a new quiz
     *//*
    public Quiz findActiveQuizByUser(String userName) {
        Optional<Quiz> oQuiz = quizRepository.findFirstByTakenBy_UserNameAndStatusOrderByIdAsc(userName, QuizStatus.PENDING);

        Quiz activeQuiz = oQuiz.orElseGet(
                () -> {
                    Optional<User> user = userRepository.findOneByUserName(userName);
                    return quizRepository.save(
                            new Quiz(
                                    "Quiz for "+ user,
                                    "New Quiz Description for User :"+ user,
                                    0,
                                    user.get(),
                                    QuizStatus.PENDING,
                                    null
                            ));
                });
        // also serves as lazy init of questions
        log.info("Quiz for user {} has {} questions", activeQuiz.getTakenBy(), activeQuiz.getQuestions().size());
        return activeQuiz;
    }*/

    // to create a Page object from a List
    static <T> Page<T> createPageFromList(List<T> list, Pageable pageable) {
        if (list == null) {
            throw new IllegalArgumentException("To create a Page, the list mustn't be null!");
        }

        int startOfPage = pageable.getPageNumber() * pageable.getPageSize();
        if (startOfPage > list.size()) {
            return new PageImpl<>(new ArrayList<>(), pageable, 0);
        }

        int endOfPage = Math.min(startOfPage + pageable.getPageSize(), list.size());
        return new PageImpl<>(list.subList(startOfPage, endOfPage), pageable, list.size());
    }


}
