package com.wharleyinc.quiz.repository;

import com.wharleyinc.quiz.domain.QueOption;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the QueOption entity.
 */
@SuppressWarnings("unused")
@Repository
public interface QueOptionRepository extends JpaRepository<QueOption, Long> {
}
