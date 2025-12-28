package com.mulemind.provider;

import com.mulemind.config.LlmConfiguration;
import com.mulemind.model.CompletionRequest;
import com.mulemind.model.CompletionResponse;

/**
 * Interface for LLM providers
 */
public interface LlmProviderClient {

    /**
     * Send a completion request to the LLM
     *
     * @param request the completion request
     * @return the completion response
     * @throws LlmProviderException if an error occurs
     */
    CompletionResponse complete(CompletionRequest request) throws LlmProviderException;

    /**
     * Test the connection to the provider
     *
     * @return true if connection is successful
     */
    boolean testConnection();

    /**
     * Get the provider configuration
     *
     * @return the configuration
     */
    LlmConfiguration getConfiguration();
}
