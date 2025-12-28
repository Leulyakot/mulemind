package com.mulemind.provider;

import com.mulemind.config.LlmConfiguration;
import com.mulemind.model.CompletionRequest;
import com.mulemind.model.CompletionResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
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

/**
 * OpenAI API provider implementation
 */
public class OpenAiProvider implements LlmProviderClient {

    private static final Logger LOGGER = LoggerFactory.getLogger(OpenAiProvider.class);
    private static final String CHAT_COMPLETIONS_ENDPOINT = "/chat/completions";
    
    private final LlmConfiguration configuration;
    private final ObjectMapper objectMapper;
    private final CloseableHttpClient httpClient;

    public OpenAiProvider(LlmConfiguration configuration) {
        this.configuration = configuration;
        this.objectMapper = new ObjectMapper();
        this.httpClient = HttpClients.createDefault();
    }

    @Override
    public CompletionResponse complete(CompletionRequest request) throws LlmProviderException {
        String endpoint = configuration.getApiBaseUrl() + CHAT_COMPLETIONS_ENDPOINT;
        
        HttpPost httpPost = new HttpPost(endpoint);
        httpPost.setHeader("Authorization", "Bearer " + configuration.getApiKey());
        httpPost.setHeader("Content-Type", "application/json");
        
        try {
            // Set model from configuration if not specified in request
            if (request.getModel() == null) {
                request.setModel(configuration.getEffectiveModel());
            }
            
            // Apply configuration defaults
            if (request.getTemperature() == null) {
                request.setTemperature(configuration.getTemperature());
            }
            if (request.getMaxTokens() == null && configuration.getMaxTokens() != null) {
                request.setMaxTokens(configuration.getMaxTokens());
            }
            
            String jsonRequest = objectMapper.writeValueAsString(request);
            LOGGER.debug("OpenAI Request: {}", jsonRequest);
            
            httpPost.setEntity(new StringEntity(jsonRequest, ContentType.APPLICATION_JSON));
            
            try (CloseableHttpResponse response = httpClient.execute(httpPost)) {
                int statusCode = response.getCode();
                String responseBody = EntityUtils.toString(response.getEntity());
                
                LOGGER.debug("OpenAI Response ({}): {}", statusCode, responseBody);
                
                if (statusCode >= 200 && statusCode < 300) {
                    return objectMapper.readValue(responseBody, CompletionResponse.class);
                } else {
                    throw new LlmProviderException(
                        "OpenAI API error: " + responseBody,
                        statusCode,
                        "OpenAI"
                    );
                }
            }
        } catch (IOException e) {
            throw new LlmProviderException(
                "Failed to communicate with OpenAI API: " + e.getMessage(),
                e,
                "OpenAI"
            );
        }
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
