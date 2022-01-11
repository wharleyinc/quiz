package com.wharleyinc.quiz.web.rest;

import com.wharleyinc.quiz.domain.QueOption;
import com.wharleyinc.quiz.repository.QueOptionRepository;
import com.wharleyinc.quiz.service.QueOptionService;
import com.wharleyinc.quiz.utils.HeaderUtil;
import com.wharleyinc.quiz.utils.ResponseUtil;
import com.wharleyinc.quiz.web.rest.errors.BadRequestAlertException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * REST controller for managing {@link QueOption}.
 */
@RestController
@RequestMapping("/api")
public class QueOptionResource {

    private final Logger log = LoggerFactory.getLogger(QueOptionResource.class);

    private static final String ENTITY_NAME = "queOption";

    @Value("${spring.application.name}")
    private String applicationName;

    private final QueOptionService queOptionService;

    private final QueOptionRepository queOptionRepository;

    public QueOptionResource(QueOptionService queOptionService, QueOptionRepository queOptionRepository) {
        this.queOptionService = queOptionService;
        this.queOptionRepository = queOptionRepository;
    }

    /**
     * {@code POST  /que-options} : Create a new queOption.
     *
     * @param queOption the queOption to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new queOption, or with status {@code 400 (Bad Request)} if the queOption has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/que-options")
    public ResponseEntity<QueOption> createQueOption(@RequestBody QueOption queOption) throws URISyntaxException {
        log.debug("REST request to save QueOption : {}", queOption);
        if (queOption.getId() != null) {
            throw new BadRequestAlertException("A new queOption cannot already have an ID", ENTITY_NAME, "idexists");
        }
        QueOption result = queOptionService.save(queOption);
        return ResponseEntity
                .created(new URI("/api/que-options/" + result.getId()))
                .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
                .body(result);
    }

    /**
     * {@code PUT  /que-options/:id} : Updates an existing queOption.
     *
     * @param id        the id of the queOption to save.
     * @param queOption the queOption to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated queOption,
     * or with status {@code 400 (Bad Request)} if the queOption is not valid,
     * or with status {@code 500 (Internal Server Error)} if the queOption couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/que-options/{id}")
    public ResponseEntity<QueOption> updateQueOption(
            @PathVariable(value = "id", required = false) final Long id,
            @RequestBody QueOption queOption
    ) throws URISyntaxException {
        log.debug("REST request to update QueOption : {}, {}", id, queOption);
        if (queOption.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, queOption.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!queOptionRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        QueOption result = queOptionService.save(queOption);
        return ResponseEntity
                .ok()
                .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, queOption.getId().toString()))
                .body(result);
    }

    /**
     * {@code PATCH  /que-options/:id} : Partial updates given fields of an existing queOption, field will ignore if it is null
     *
     * @param id        the id of the queOption to save.
     * @param queOption the queOption to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated queOption,
     * or with status {@code 400 (Bad Request)} if the queOption is not valid,
     * or with status {@code 404 (Not Found)} if the queOption is not found,
     * or with status {@code 500 (Internal Server Error)} if the queOption couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/que-options/{id}", consumes = {"application/json", "application/merge-patch+json"})
    public ResponseEntity<QueOption> partialUpdateQueOption(
            @PathVariable(value = "id", required = false) final Long id,
            @RequestBody QueOption queOption
    ) throws URISyntaxException {
        log.debug("REST request to partial update QueOption partially : {}, {}", id, queOption);
        if (queOption.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, queOption.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!queOptionRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<QueOption> result = queOptionService.partialUpdate(queOption);

        return ResponseUtil.wrapOrNotFound(
                result,
                HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, queOption.getId().toString())
        );
    }

    /**
     * {@code GET  /que-options} : get all the queOptions.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of queOptions in body.
     */
    @GetMapping("/que-options")
    public List<QueOption> getAllQueOptions() {
        log.debug("REST request to get all QueOptions");
        return queOptionService.findAll();
    }

    /**
     * {@code GET  /que-options/:id} : get the "id" queOption.
     *
     * @param id the id of the queOption to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the queOption, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/que-options/{id}")
    public ResponseEntity<QueOption> getQueOption(@PathVariable Long id) {
        log.debug("REST request to get QueOption : {}", id);
        Optional<QueOption> queOption = queOptionService.findOne(id);
        return ResponseUtil.wrapOrNotFound(queOption);
    }

    /**
     * {@code DELETE  /que-options/:id} : delete the "id" queOption.
     *
     * @param id the id of the queOption to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/que-options/{id}")
    public ResponseEntity<Void> deleteQueOption(@PathVariable Long id) {
        log.debug("REST request to delete QueOption : {}", id);
        queOptionService.delete(id);
        return ResponseEntity
                .noContent()
                .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
                .build();
    }
}
