package com.mulemind;

import com.mulemind.config.LlmConfiguration;
import com.mulemind.config.LlmProvider;
import com.mulemind.model.CompletionRequest;
import com.mulemind.model.CompletionResponse;
import com.mulemind.model.Message;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

/**
 * Unit tests for the LLM Connector models
 */
class LlmConnectorTest {

    @Test
    void testMessageCreation() {
        // Test static factory methods
        Message systemMsg = Message.system("You are helpful");
        assertEquals("system", systemMsg.getRole());
        assertEquals("You are helpful", systemMsg.getContent());

        Message userMsg = Message.user("Hello");
        assertEquals("user", userMsg.getRole());
        assertEquals("Hello", userMsg.getContent());

        Message assistantMsg = Message.assistant("Hi there!");
        assertEquals("assistant", assistantMsg.getRole());
        assertEquals("Hi there!", assistantMsg.getContent());
    }

    @Test
    void testCompletionRequestBuilder() {
        CompletionRequest request = CompletionRequest.builder()
            .model("gpt-4")
            .addSystemMessage("You are a helpful assistant")
            .addUserMessage("What is 2+2?")
            .temperature(0.7)
            .maxTokens(100)
            .build();

        assertEquals("gpt-4", request.getModel());
        assertEquals(2, request.getMessages().size());
        assertEquals("system", request.getMessages().get(0).getRole());
        assertEquals("user", request.getMessages().get(1).getRole());
        assertEquals(0.7, request.getTemperature());
        assertEquals(100, request.getMaxTokens());
    }

    @Test
    void testLlmProviderDefaults() {
        // Test OpenAI defaults
        assertEquals("gpt-4-turbo-preview", LlmProvider.OPENAI.getDefaultModel());
        assertEquals("https://api.openai.com/v1", LlmProvider.OPENAI.getDefaultBaseUrl());
        
        // Test Anthropic defaults
        assertEquals("claude-3-5-sonnet-20241022", LlmProvider.ANTHROPIC.getDefaultModel());
        assertEquals("https://api.anthropic.com/v1", LlmProvider.ANTHROPIC.getDefaultBaseUrl());
    }

    @Test
    void testCompletionResponseParsing() {
        CompletionResponse response = new CompletionResponse();
        response.setId("test-id");
        response.setModel("gpt-4");

        CompletionResponse.Choice choice = new CompletionResponse.Choice();
        choice.setIndex(0);
        choice.setMessage(Message.assistant("The answer is 4"));
        choice.setFinishReason("stop");

        response.setChoices(List.of(choice));

        CompletionResponse.Usage usage = new CompletionResponse.Usage();
        usage.setPromptTokens(10);
        usage.setCompletionTokens(5);
        usage.setTotalTokens(15);
        response.setUsage(usage);

        // Verify parsing
        assertEquals("test-id", response.getId());
        assertEquals("gpt-4", response.getModel());
        assertEquals("The answer is 4", response.getContent());
        assertEquals(15, response.getUsage().getTotalTokens());
    }
}
