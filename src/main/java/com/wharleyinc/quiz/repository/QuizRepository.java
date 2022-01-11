package com.wharleyinc.quiz.repository;

import com.wharleyinc.quiz.domain.Quiz;
import com.wharleyinc.quiz.domain.enums.QuizStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Spring Data SQL repository for the Quiz entity.
 */
@Repository
public interface QuizRepository extends JpaRepository<Quiz, Long> {
    @Query("select quiz from Quiz quiz where quiz.takenBy.userName = ?#{principal.username}")
    List<Quiz> findByTakenByIsCurrentUser();

    @Query(
            value = "select distinct quiz from Quiz quiz left join fetch quiz.questions",
            countQuery = "select count(distinct quiz) from Quiz quiz"
    )
    Page<Quiz> findAllWithEagerRelationships(Pageable pageable);

    @Query("select distinct quiz from Quiz quiz left join fetch quiz.questions")
    List<Quiz> findAllWithEagerRelationships();

    @Query("select quiz from Quiz quiz left join fetch quiz.questions where quiz.id =:id")
    Optional<Quiz> findOneWithEagerRelationships(@Param("id") Long id);

    Optional<Quiz> findFirstByTakenBy_UserNameAndStatusOrderByIdAsc(String userName, QuizStatus pending);

}
