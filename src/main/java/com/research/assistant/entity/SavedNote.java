package com.research.assistant.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

import lombok.Setter;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Document(collection = "SavedNotes")
@Data
@AllArgsConstructor
@Getter
@Setter
public class SavedNote {
    @Id
    private String articleLink;
    private List<String> notes;
}
