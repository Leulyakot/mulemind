package com.mulemind.operations;

import com.mulemind.config.LlmConfiguration;
import com.mulemind.model.CompletionRequest;
import com.mulemind.model.CompletionResponse;
import com.mulemind.model.Message;
import com.mulemind.provider.LlmProviderClient;
import com.mulemind.provider.LlmProviderException;
import com.mulemind.provider.LlmProviderFactory;
import org.mule.runtime.extension.api.annotation.param.Config;
import org.mule.runtime.extension.api.annotation.param.Optional;
import org.mule.runtime.extension.api.annotation.param.display.DisplayName;
import org.mule.runtime.extension.api.annotation.param.display.Summary;
import org.mule.runtime.extension.api.annotation.param.display.Text;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * LLM Connector Operations
 */
public class LlmOperations {

    private static final Logger LOGGER = LoggerFactory.getLogger(LlmOperations.class);

    /**
     * Chat Completion - Send a chat message to the LLM
     *
     * @param configuration the connector configuration
     * @param userMessage the user's message
     * @param systemPrompt optional system prompt to set context
     * @param conversationHistory optional conversation history
     * @param temperature optional temperature override
     * @param maxTokens optional max tokens override
     * @return the LLM's response
     */
    @DisplayName("Chat Completion")
    @Summary("Send a chat message to the LLM and get a response")
    public String chatCompletion(
            @Config LlmConfiguration configuration,
            @DisplayName("User Message") @Text String userMessage,
            @Optional @DisplayName("System Prompt") @Text String systemPrompt,
            @Optional @DisplayName("Conversation History") List<Map<String, String>> conversationHistory,
            @Optional @DisplayName("Temperature") Double temperature,
            @Optional @DisplayName("Max Tokens") Integer maxTokens
    ) throws LlmProviderException {
        
        LOGGER.info("Executing chat completion with provider: {}", configuration.getProvider());
        
        LlmProviderClient provider = LlmProviderFactory.createProvider(configuration);
        
        CompletionRequest.Builder requestBuilder = CompletionRequest.builder();
        
        // Add system prompt if provided
        if (systemPrompt != null && !systemPrompt.trim().isEmpty()) {
            requestBuilder.addSystemMessage(systemPrompt);
        }
        
        // Add conversation history if provided
        if (conversationHistory != null) {
            for (Map<String, String> msg : conversationHistory) {
                String role = msg.get("role");
                String content = msg.get("content");
                if (role != null && content != null) {
                    requestBuilder.addMessage(role, content);
                }
            }
        }
        
        // Add current user message
        requestBuilder.addUserMessage(userMessage);
        
        // Override temperature if provided
        if (temperature != null) {
            requestBuilder.temperature(temperature);
        }
        
        // Override max tokens if provided
        if (maxTokens != null) {
            requestBuilder.maxTokens(maxTokens);
        }
        
        CompletionRequest request = requestBuilder.build();
        CompletionResponse response = provider.complete(request);
        
        LOGGER.info("Chat completion successful. Tokens used: {}", response.getUsage());
        
        return response.getContent();
    }

    /**
     * Advanced Chat - Send a chat request with full control
     *
     * @param configuration the connector configuration
     * @param messages list of messages in the conversation
     * @param model optional model override
     * @param temperature optional temperature
     * @param maxTokens optional max tokens
     * @param topP optional top P
     * @param frequencyPenalty optional frequency penalty
     * @param presencePenalty optional presence penalty
     * @return the complete response object
     */
    @DisplayName("Advanced Chat")
    @Summary("Send a chat request with full control over all parameters")
    public CompletionResponse advancedChat(
            @Config LlmConfiguration configuration,
            @DisplayName("Messages") List<Map<String, String>> messages,
            @Optional @DisplayName("Model Override") String model,
            @Optional @DisplayName("Temperature") Double temperature,
            @Optional @DisplayName("Max Tokens") Integer maxTokens,
            @Optional @DisplayName("Top P") Double topP,
            @Optional @DisplayName("Frequency Penalty") Double frequencyPenalty,
            @Optional @DisplayName("Presence Penalty") Double presencePenalty
    ) throws LlmProviderException {
        
        LOGGER.info("Executing advanced chat with provider: {}", configuration.getProvider());
        
        LlmProviderClient provider = LlmProviderFactory.createProvider(configuration);
        
        CompletionRequest.Builder requestBuilder = CompletionRequest.builder();
        
        // Add all messages
        for (Map<String, String> msg : messages) {
            String role = msg.get("role");
            String content = msg.get("content");
            if (role != null && content != null) {
                requestBuilder.addMessage(role, content);
            }
        }
        
        // Apply all optional parameters
        if (model != null) requestBuilder.model(model);
        if (temperature != null) requestBuilder.temperature(temperature);
        if (maxTokens != null) requestBuilder.maxTokens(maxTokens);
        if (topP != null) requestBuilder.topP(topP);
        if (frequencyPenalty != null) requestBuilder.frequencyPenalty(frequencyPenalty);
        if (presencePenalty != null) requestBuilder.presencePenalty(presencePenalty);
        
        CompletionRequest request = requestBuilder.build();
        CompletionResponse response = provider.complete(request);
        
        LOGGER.info("Advanced chat successful. Tokens used: {}", response.getUsage());
        
        return response;
    }

    /**
     * Simple Prompt - Send a simple prompt and get a response
     *
     * @param configuration the connector configuration
     * @param prompt the prompt text
     * @return the LLM's response
     */
    @DisplayName("Simple Prompt")
    @Summary("Send a simple prompt to the LLM")
    public String simplePrompt(
            @Config LlmConfiguration configuration,
            @DisplayName("Prompt") @Text String prompt
    ) throws LlmProviderException {
        
        LOGGER.info("Executing simple prompt with provider: {}", configuration.getProvider());
        
        return chatCompletion(configuration, prompt, null, null, null, null);
    }

    /**
     * Test Connection - Test the connection to the LLM provider
     *
     * @param configuration the connector configuration
     * @return connection status message
     */
    @DisplayName("Test Connection")
    @Summary("Test the connection to the configured LLM provider")
    public String testConnection(@Config LlmConfiguration configuration) {
        LOGGER.info("Testing connection to provider: {}", configuration.getProvider());
        
        try {
            LlmProviderClient provider = LlmProviderFactory.createProvider(configuration);
            boolean success = provider.testConnection();
            
            if (success) {
                String message = "Successfully connected to " + configuration.getProvider().getDisplayName();
                LOGGER.info(message);
                return message;
            } else {
                String message = "Failed to connect to " + configuration.getProvider().getDisplayName();
                LOGGER.error(message);
                return message;
            }
        } catch (Exception e) {
            String message = "Connection test failed: " + e.getMessage();
            LOGGER.error(message, e);
            return message;
        }
    }
}
