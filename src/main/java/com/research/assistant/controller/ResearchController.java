package com.research.assistant.controller;


import com.research.assistant.entity.ResearchRequest;
import com.research.assistant.service.ResearchService;
import com.research.assistant.service.ResearchService2;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/research")
@CrossOrigin(origins = "*") // allow any front-end client to access these apis
@AllArgsConstructor
public class ResearchController {

    private final ResearchService researchService;
    private final ResearchService2 researchService2;


    @PostMapping("/process")
    public ResponseEntity<String> processContent(@RequestBody ResearchRequest researchRequest) {
        String result = researchService.processContent(researchRequest);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @PostMapping("/process2")
    public ResponseEntity<String> processContent2(@RequestBody ResearchRequest researchRequest) {
        String result = researchService2.processContent(researchRequest);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

}
