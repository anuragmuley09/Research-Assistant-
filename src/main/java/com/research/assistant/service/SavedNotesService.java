package com.research.assistant.service;

import com.research.assistant.repo.SavedNotesRepo;
import com.research.assistant.entity.SavedNote;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class SavedNotesService {

    @Autowired
    private SavedNotesRepo savedNotesRepo;


    public List<String> getAllNotes(String articleLink) {
        Optional<List<String>> allNotes = savedNotesRepo.findByArticleLink(articleLink);
        return (allNotes.isPresent()) ? allNotes.get() : null;
    }

    // NEEDS REFACTORING / REWRITE.
    /*
    * Users' should be able to check their saved notes for a certain URL.
    * Whenever a users visits a certain website, of which they already took some notes,
    * then try to display it somehow on the notes panel, or f it lets 'alert' user.
    * let's keep it simple. */

    // 1. just overwrite the note.
    public SavedNote addNote(SavedNote savedNote) {
//        Optional<SavedNote> existingNote = savedNotesRepo.findById(savedNote.getArticleLink());
//        if(existingNote.isPresent()){
//            SavedNote noteToUpdate = existingNote.get();
//            noteToUpdate.getNotes().addAll(savedNote.getNotes());
//
//        }
        return null; // TEMP. CHANGE THIS.
    }
}
