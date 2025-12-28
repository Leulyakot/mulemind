package com.mulemind.config;

import org.mule.runtime.extension.api.annotation.Configuration;
import org.mule.runtime.extension.api.annotation.Operations;
import org.mule.runtime.extension.api.annotation.connectivity.ConnectionProviders;
import org.mule.runtime.extension.api.annotation.param.Parameter;
import org.mule.runtime.extension.api.annotation.param.Optional;
import org.mule.runtime.extension.api.annotation.param.display.DisplayName;
import org.mule.runtime.extension.api.annotation.param.display.Summary;
import org.mule.runtime.extension.api.annotation.param.display.Example;
import com.mulemind.operations.LlmOperations;

/**
 * Configuration for LLM Connector
 */
@Configuration(name = "config")
@Operations(LlmOperations.class)
public class LlmConfiguration {

    @Parameter
    @DisplayName("Provider")
    @Summary("The LLM provider to use")
    private LlmProvider provider;

    @Parameter
    @DisplayName("API Key")
    @Summary("API key for authentication")
    @Example("sk-...")
    private String apiKey;

    @Parameter
    @Optional
    @DisplayName("Model")
    @Summary("The model to use (e.g., gpt-4, claude-3-opus-20240229)")
    @Example("gpt-4-turbo-preview")
    private String model;

    @Parameter
    @Optional(defaultValue = "https://api.openai.com/v1")
    @DisplayName("API Base URL")
    @Summary("Base URL for the API endpoint")
    private String apiBaseUrl;

    @Parameter
    @Optional(defaultValue = "30")
    @DisplayName("Timeout (seconds)")
    @Summary("Request timeout in seconds")
    private int timeoutSeconds;

    @Parameter
    @Optional(defaultValue = "false")
    @DisplayName("Enable Streaming")
    @Summary("Enable streaming responses")
    private boolean streamingEnabled;

    @Parameter
    @Optional(defaultValue = "1.0")
    @DisplayName("Temperature")
    @Summary("Sampling temperature (0.0 to 2.0)")
    private double temperature;

    @Parameter
    @Optional
    @DisplayName("Max Tokens")
    @Summary("Maximum tokens to generate")
    private Integer maxTokens;

    // Getters
    public LlmProvider getProvider() {
        return provider;
    }

    public String getApiKey() {
        return apiKey;
    }

    public String getModel() {
        return model;
    }

    public String getApiBaseUrl() {
        return apiBaseUrl;
    }

    public int getTimeoutSeconds() {
        return timeoutSeconds;
    }

    public boolean isStreamingEnabled() {
        return streamingEnabled;
    }

    public double getTemperature() {
        return temperature;
    }

    public Integer getMaxTokens() {
        return maxTokens;
    }

    /**
     * Get the effective model name
     */
    public String getEffectiveModel() {
        if (model != null && !model.isEmpty()) {
            return model;
        }
        return provider.getDefaultModel();
    }
}
