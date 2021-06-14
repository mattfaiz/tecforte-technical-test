package com.tecforte.blog.service;

import com.tecforte.blog.domain.Entry;
import com.tecforte.blog.repository.EntryRepository;
import com.tecforte.blog.service.dto.EntryDTO;
import com.tecforte.blog.service.mapper.EntryMapper;
import com.tecforte.blog.web.rest.errors.BadRequestAlertException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Service Implementation for managing {@link Entry}.
 */
@Service
@Transactional
public class EntryService extends Throwable{

    private final Logger log = LoggerFactory.getLogger(EntryService.class);

    private final EntryRepository entryRepository;

    private final EntryMapper entryMapper;

    public EntryService(EntryRepository entryRepository, EntryMapper entryMapper) {
        this.entryRepository = entryRepository;
        this.entryMapper = entryMapper;
    }

    /**
     * Save a entry.
     *
     * @param entryDTO the entity to save.
     * @return the persisted entity.
     */
    public EntryDTO save(EntryDTO entryDTO) throws BadRequestAlertException{

            log.debug("Request to save Entry : {}", entryDTO);
            Entry entry = entryMapper.toEntity(entryDTO);
            if (entryDTO.getBlog().isPositive()) {
                if (entryDTO.getEmoji().equals("SAD") || entryDTO.getEmoji().equals("ANGRY")) {
                    throw new BadRequestAlertException("Invalid Emoji", "entry", "invalidEmoji");
                }
            }else
            {
                    if (entryDTO.getEmoji().equals("LIKE") || entryDTO.getEmoji().equals("HAHA")) {
                        throw new BadRequestAlertException("Invalid Emoji", "entry", "invalidEmoji");
                    }

            }

            if (entryDTO.getBlog().isPositive()) {
                if (entryDTO.getContent().contains("SAD")||entryDTO.getContent().contains("FEAR")||entryDTO.getContent().contains("LONELY")){
                    throw new BadRequestAlertException("Invalid Emoji", "entry", "invalidEmoji");
                }
            }else
            if (entryDTO.getContent().contains("LOVE")||entryDTO.getContent().contains("HAPPY")||entryDTO.getContent().contains("TRUST")){
                throw new BadRequestAlertException("Invalid Emoji", "entry", "invalidEmoji");
            }
            entry = entryRepository.save(entry);
            return entryMapper.toDto(entry);

    }

    /**
     * Get all the entries.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<EntryDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Entries");
        return entryRepository.findAll(pageable)
            .map(entryMapper::toDto);
    }


    /**
     * Get one entry by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<EntryDTO> findOne(Long id) {
        log.debug("Request to get Entry : {}", id);
        return entryRepository.findById(id)
            .map(entryMapper::toDto);
    }

    /**
     * Delete the entry by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Entry : {}", id);
        entryRepository.deleteById(id);
    }
}
