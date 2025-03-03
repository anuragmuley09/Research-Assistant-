package com.research.assistant.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.research.assistant.entity.GeminiResponse;
import com.research.assistant.entity.ResearchRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;

import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;


import java.util.Map;

@Service
@Slf4j
public class ResearchService2 {

    @Value("${gemini.api.url}")
    private String geminiAPIURL;

    @Value("${gemini.api.key}")
    private String geminiAPIKEY;

    private final WebClient webClient;
    private final ObjectMapper objectMapper;


    public ResearchService2(WebClient.Builder webClientBuilder, ObjectMapper objectMapper) {
        this.webClient = webClientBuilder.build();
        this.objectMapper = objectMapper;
    }

    public String processContent(ResearchRequest researchRequest) {
        // 1. Build Prompt
        String prompt = buildPrompt(researchRequest);

        // 2. Query the AI model API
        Map<String, Object> requestBody = getQuery(prompt);

        String response = webClient.post()
                .uri(geminiAPIURL+geminiAPIKEY)
                .bodyValue(requestBody)
                .retrieve()
                .bodyToMono(String.class)
                .block();


        // 3. Parse response and return

        return extractTextFromResponse(response);



    }

    private String extractTextFromResponse(String response) {
        try {
            GeminiResponse geminiResponse = objectMapper.readValue(response, GeminiResponse.class);
            if (geminiResponse.getCandidates() != null && !geminiResponse.getCandidates().isEmpty()) {
                GeminiResponse.Candidate firstCandidate = geminiResponse.getCandidates().get(0);
                if (firstCandidate.getContent() != null
                        && firstCandidate.getContent().getParts() != null
                        && !firstCandidate.getContent().getParts().isEmpty()) {
                    return firstCandidate.getContent().getParts().get(0).getText();
                }
            }
            return "No content found in response.";
        } catch (Exception e) {
            log.error("Error parsing response", e);
            return "Error Parsing Response: " + e.getMessage();
        }
    }

    private String buildPrompt(ResearchRequest researchRequest) {
        StringBuilder prompt = new StringBuilder();
        switch (researchRequest.getOperation()) {
            case "summarize":
                prompt.append("Provide a clear and concise summary of the following text in a few sentences: \n\n");
                break;
            case "suggest":
                prompt.append("Based on the following content, suggest related topics and further reading. Use headings and bullet points: \n\n");
                break;
            default:
                throw new IllegalArgumentException("Unknown operation: " + researchRequest.getOperation());
        }

        prompt.append(researchRequest.getContent());
        return prompt.toString();
    }

    private Map<String, Object> getQuery(String prompt) {
        return Map.of(
                "contents", new Object[]{
                        Map.of("parts", new Object[]{
                                Map.of("text", prompt)
                        })
                }
        );
    }
}
