package com.research.assistant.controller;

import com.research.assistant.entity.SavedNote;
import com.research.assistant.service.SavedNotesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/saved")
@CrossOrigin(origins = "*")
public class SavedNotesController {

    @Autowired
    private SavedNotesService savedNotesService;

    @GetMapping("/{articleLink}")
    public ResponseEntity<List<String>> getALlNotesOfURL(@PathVariable String articleLink){
        List<String> allNotes = savedNotesService.getAllNotes(articleLink);

        return (allNotes != null) ?
                new ResponseEntity<>(allNotes, HttpStatus.OK) :
                new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PostMapping("/add-note")
    public ResponseEntity<SavedNote> saveNotes(@RequestBody SavedNote savedNote){
        if(savedNote.getArticleLink() == null || savedNote.getNotes() == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        SavedNote newNote = savedNotesService.addNote(savedNote);
        return new ResponseEntity<>(newNote, HttpStatus.CREATED);
    }






}
