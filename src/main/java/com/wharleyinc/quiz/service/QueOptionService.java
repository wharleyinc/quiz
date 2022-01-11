package com.wharleyinc.quiz.service;

import com.wharleyinc.quiz.domain.QueOption;
import com.wharleyinc.quiz.repository.QueOptionRepository;

import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link QueOption}.
 */
@Service
@Transactional
public class QueOptionService {

    private final Logger log = LoggerFactory.getLogger(QueOptionService.class);

    private final QueOptionRepository queOptionRepository;

    public QueOptionService(QueOptionRepository queOptionRepository) {
        this.queOptionRepository = queOptionRepository;
    }

    /**
     * Save a queOption.
     *
     * @param queOption the entity to save.
     * @return the persisted entity.
     */
    public QueOption save(QueOption queOption) {
        log.debug("Request to save QueOption : {}", queOption);
        return queOptionRepository.save(queOption);
    }

    /**
     * Partially update a queOption.
     *
     * @param queOption the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<QueOption> partialUpdate(QueOption queOption) {
        log.debug("Request to partially update QueOption : {}", queOption);

        return queOptionRepository
                .findById(queOption.getId())
                .map(existingQueOption -> {
                    if (queOption.getText() != null) {
                        existingQueOption.setText(queOption.getText());
                    }
                    if (queOption.getAnswerOption() != null) {
                        existingQueOption.setAnswerOption(queOption.getAnswerOption());
                    }
                    if (queOption.getSelectedOption() != null) {
                        existingQueOption.setSelectedOption(queOption.getSelectedOption());
                    }

                    return existingQueOption;
                })
                .map(queOptionRepository::save);
    }

    /**
     * Get all the queOptions.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<QueOption> findAll() {
        log.debug("Request to get all QueOptions");
        return queOptionRepository.findAll();
    }

    /**
     * Get one queOption by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<QueOption> findOne(Long id) {
        log.debug("Request to get QueOption : {}", id);
        return queOptionRepository.findById(id);
    }

    /**
     * Delete the queOption by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete QueOption : {}", id);
        queOptionRepository.deleteById(id);
    }
}
