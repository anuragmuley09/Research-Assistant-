package com.research.assistant.repo;

import com.research.assistant.entity.SavedNote;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SavedNotesRepo extends MongoRepository<SavedNote, String> {
    Optional<List<String>> findByArticleLink(String articleLink);
}
