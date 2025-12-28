package com.mulemind.provider;

import com.mulemind.config.LlmConfiguration;
import com.mulemind.model.CompletionRequest;
import com.mulemind.model.CompletionResponse;
import com.mulemind.model.Message;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.apache.hc.client5.http.classic.methods.HttpPost;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.CloseableHttpResponse;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.core5.http.ContentType;
import org.apache.hc.core5.http.io.entity.StringEntity;
import org.apache.hc.core5.http.io.entity.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Anthropic Claude API provider implementation
 */
public class AnthropicProvider implements LlmProviderClient {

    private static final Logger LOGGER = LoggerFactory.getLogger(AnthropicProvider.class);
    private static final String MESSAGES_ENDPOINT = "/messages";
    private static final String ANTHROPIC_VERSION = "2023-06-01";
    
    private final LlmConfiguration configuration;
    private final ObjectMapper objectMapper;
    private final CloseableHttpClient httpClient;

    public AnthropicProvider(LlmConfiguration configuration) {
        this.configuration = configuration;
        this.objectMapper = new ObjectMapper();
        this.httpClient = HttpClients.createDefault();
    }

    @Override
    public CompletionResponse complete(CompletionRequest request) throws LlmProviderException {
        String endpoint = configuration.getApiBaseUrl() + MESSAGES_ENDPOINT;
        
        HttpPost httpPost = new HttpPost(endpoint);
        httpPost.setHeader("x-api-key", configuration.getApiKey());
        httpPost.setHeader("anthropic-version", ANTHROPIC_VERSION);
        httpPost.setHeader("Content-Type", "application/json");
        
        try {
            // Convert OpenAI-style request to Anthropic format
            ObjectNode anthropicRequest = convertToAnthropicFormat(request);
            
            String jsonRequest = objectMapper.writeValueAsString(anthropicRequest);
            LOGGER.debug("Anthropic Request: {}", jsonRequest);
            
            httpPost.setEntity(new StringEntity(jsonRequest, ContentType.APPLICATION_JSON));
            
            try (CloseableHttpResponse response = httpClient.execute(httpPost)) {
                int statusCode = response.getCode();
                String responseBody = EntityUtils.toString(response.getEntity());
                
                LOGGER.debug("Anthropic Response ({}): {}", statusCode, responseBody);
                
                if (statusCode >= 200 && statusCode < 300) {
                    return convertFromAnthropicFormat(responseBody);
                } else {
                    throw new LlmProviderException(
                        "Anthropic API error: " + responseBody,
                        statusCode,
                        "Anthropic"
                    );
                }
            }
        } catch (IOException e) {
            throw new LlmProviderException(
                "Failed to communicate with Anthropic API: " + e.getMessage(),
                e,
                "Anthropic"
            );
        }
    }

    /**
     * Convert OpenAI-style request to Anthropic format
     */
    private ObjectNode convertToAnthropicFormat(CompletionRequest request) {
        ObjectNode anthropicRequest = objectMapper.createObjectNode();
        
        // Model
        String model = request.getModel() != null ? request.getModel() : configuration.getEffectiveModel();
        anthropicRequest.put("model", model);
        
        // Max tokens (required by Anthropic)
        Integer maxTokens = request.getMaxTokens() != null ? request.getMaxTokens() : 
                           configuration.getMaxTokens() != null ? configuration.getMaxTokens() : 1024;
        anthropicRequest.put("max_tokens", maxTokens);
        
        // Temperature
        Double temperature = request.getTemperature() != null ? request.getTemperature() : configuration.getTemperature();
        anthropicRequest.put("temperature", temperature);
        
        // Messages - separate system from user/assistant messages
        String systemMessage = null;
        List<Message> conversationMessages = new ArrayList<>();
        
        for (Message msg : request.getMessages()) {
            if ("system".equals(msg.getRole())) {
                systemMessage = msg.getContent();
            } else {
                conversationMessages.add(msg);
            }
        }
        
        if (systemMessage != null) {
            anthropicRequest.put("system", systemMessage);
        }
        
        anthropicRequest.set("messages", objectMapper.valueToTree(conversationMessages));
        
        return anthropicRequest;
    }

    /**
     * Convert Anthropic response to OpenAI-style format
     */
    private CompletionResponse convertFromAnthropicFormat(String responseBody) throws IOException {
        JsonNode anthropicResponse = objectMapper.readTree(responseBody);
        
        CompletionResponse response = new CompletionResponse();
        response.setId(anthropicResponse.get("id").asText());
        response.setModel(anthropicResponse.get("model").asText());
        response.setObject("chat.completion");
        
        // Extract content
        JsonNode contentArray = anthropicResponse.get("content");
        String content = "";
        if (contentArray != null && contentArray.isArray() && contentArray.size() > 0) {
            content = contentArray.get(0).get("text").asText();
        }
        
        // Create choice
        CompletionResponse.Choice choice = new CompletionResponse.Choice();
        choice.setIndex(0);
        choice.setMessage(Message.assistant(content));
        choice.setFinishReason(anthropicResponse.get("stop_reason").asText());
        
        List<CompletionResponse.Choice> choices = new ArrayList<>();
        choices.add(choice);
        response.setChoices(choices);
        
        // Usage
        JsonNode usageNode = anthropicResponse.get("usage");
        if (usageNode != null) {
            CompletionResponse.Usage usage = new CompletionResponse.Usage();
            usage.setPromptTokens(usageNode.get("input_tokens").asInt());
            usage.setCompletionTokens(usageNode.get("output_tokens").asInt());
            usage.setTotalTokens(usage.getPromptTokens() + usage.getCompletionTokens());
            response.setUsage(usage);
        }
        
        return response;
    }

    @Override
    public boolean testConnection() {
        try {
            CompletionRequest testRequest = CompletionRequest.builder()
                .model(configuration.getEffectiveModel())
                .addUserMessage("Hello")
                .maxTokens(5)
                .build();
            
            CompletionResponse response = complete(testRequest);
            return response != null && response.getContent() != null;
        } catch (Exception e) {
            LOGGER.error("Connection test failed", e);
            return false;
        }
    }

    @Override
    public LlmConfiguration getConfiguration() {
        return configuration;
    }
}
