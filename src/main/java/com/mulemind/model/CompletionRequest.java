package com.mulemind.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import java.util.ArrayList;

/**
 * Universal LLM completion request model
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CompletionRequest {

    private String model;
    private List<Message> messages;
    private Double temperature;
    
    @JsonProperty("max_tokens")
    private Integer maxTokens;
    
    private Boolean stream;
    
    @JsonProperty("top_p")
    private Double topP;
    
    @JsonProperty("frequency_penalty")
    private Double frequencyPenalty;
    
    @JsonProperty("presence_penalty")
    private Double presencePenalty;

    public CompletionRequest() {
        this.messages = new ArrayList<>();
    }

    public CompletionRequest(String model, List<Message> messages) {
        this.model = model;
        this.messages = messages;
    }

    // Builder pattern
    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private final CompletionRequest request = new CompletionRequest();

        public Builder model(String model) {
            request.model = model;
            return this;
        }

        public Builder addMessage(String role, String content) {
            request.messages.add(new Message(role, content));
            return this;
        }

        public Builder addSystemMessage(String content) {
            return addMessage("system", content);
        }

        public Builder addUserMessage(String content) {
            return addMessage("user", content);
        }

        public Builder addAssistantMessage(String content) {
            return addMessage("assistant", content);
        }

        public Builder temperature(Double temperature) {
            request.temperature = temperature;
            return this;
        }

        public Builder maxTokens(Integer maxTokens) {
            request.maxTokens = maxTokens;
            return this;
        }

        public Builder stream(Boolean stream) {
            request.stream = stream;
            return this;
        }

        public Builder topP(Double topP) {
            request.topP = topP;
            return this;
        }

        public Builder frequencyPenalty(Double frequencyPenalty) {
            request.frequencyPenalty = frequencyPenalty;
            return this;
        }

        public Builder presencePenalty(Double presencePenalty) {
            request.presencePenalty = presencePenalty;
            return this;
        }

        public CompletionRequest build() {
            return request;
        }
    }

    // Getters and Setters
    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public List<Message> getMessages() {
        return messages;
    }

    public void setMessages(List<Message> messages) {
        this.messages = messages;
    }

    public Double getTemperature() {
        return temperature;
    }

    public void setTemperature(Double temperature) {
        this.temperature = temperature;
    }

    public Integer getMaxTokens() {
        return maxTokens;
    }

    public void setMaxTokens(Integer maxTokens) {
        this.maxTokens = maxTokens;
    }

    public Boolean getStream() {
        return stream;
    }

    public void setStream(Boolean stream) {
        this.stream = stream;
    }

    public Double getTopP() {
        return topP;
    }

    public void setTopP(Double topP) {
        this.topP = topP;
    }

    public Double getFrequencyPenalty() {
        return frequencyPenalty;
    }

    public void setFrequencyPenalty(Double frequencyPenalty) {
        this.frequencyPenalty = frequencyPenalty;
    }

    public Double getPresencePenalty() {
        return presencePenalty;
    }

    public void setPresencePenalty(Double presencePenalty) {
        this.presencePenalty = presencePenalty;
    }
}
