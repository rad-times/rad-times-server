package com.spotcheck.spotcheck_server.service;

import com.spotcheck.spotcheck_server.model.Keyword;
import com.spotcheck.spotcheck_server.repository.KeywordRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Set;

@Service
public class KeywordService {
    private final KeywordRepository keywordRepository;

    public KeywordService(KeywordRepository keywordRepository) {
        this.keywordRepository = keywordRepository;
    }

    // Get a keyword by ID
    public Optional<Keyword> getKeywordById(Integer id) {
        try {
            return keywordRepository.findById(id);
        } catch (Exception e) {
            // Handle exception or log the error
            throw new RuntimeException("Failed to fetch keyword by ID: " + e.getMessage());
        }
    }
}
