package com.mulemind.config;

/**
 * Supported LLM providers
 */
public enum LlmProvider {
    OPENAI("OpenAI", "https://api.openai.com/v1", "gpt-4-turbo-preview"),
    ANTHROPIC("Anthropic", "https://api.anthropic.com/v1", "claude-3-5-sonnet-20241022"),
    GOOGLE("Google", "https://generativelanguage.googleapis.com/v1", "gemini-pro"),
    AWS_BEDROCK("AWS Bedrock", "https://bedrock-runtime.us-east-1.amazonaws.com", "anthropic.claude-3-sonnet-20240229-v1:0"),
    AZURE_OPENAI("Azure OpenAI", null, "gpt-4");

    private final String displayName;
    private final String defaultBaseUrl;
    private final String defaultModel;

    LlmProvider(String displayName, String defaultBaseUrl, String defaultModel) {
        this.displayName = displayName;
        this.defaultBaseUrl = defaultBaseUrl;
        this.defaultModel = defaultModel;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getDefaultBaseUrl() {
        return defaultBaseUrl;
    }

    public String getDefaultModel() {
        return defaultModel;
    }
}
