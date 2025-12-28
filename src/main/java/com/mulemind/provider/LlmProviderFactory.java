package com.mulemind.provider;

import com.mulemind.config.LlmConfiguration;
import com.mulemind.config.LlmProvider;

/**
 * Factory for creating LLM provider clients
 */
public class LlmProviderFactory {

    public static LlmProviderClient createProvider(LlmConfiguration configuration) {
        LlmProvider provider = configuration.getProvider();
        
        return switch (provider) {
            case OPENAI -> new OpenAiProvider(configuration);
            case ANTHROPIC -> new AnthropicProvider(configuration);
            case GOOGLE -> throw new UnsupportedOperationException("Google provider not yet implemented");
            case AWS_BEDROCK -> throw new UnsupportedOperationException("AWS Bedrock provider not yet implemented");
            case AZURE_OPENAI -> throw new UnsupportedOperationException("Azure OpenAI provider not yet implemented");
        };
    }
}
