# MuleMind Connector - Architecture

## Overview

The MuleMind Connector is designed as a lightweight, extensible framework for integrating various Large Language Model (LLM) providers into MuleSoft applications. The architecture emphasizes simplicity, performance, and maintainability.

## Design Principles

1. **Provider Agnostic**: Abstract common patterns across different LLM providers
2. **Minimal Dependencies**: Keep the dependency footprint small
3. **Modern Java**: Leverage Java 17+ features for clean, efficient code
4. **Extensible**: Easy to add new providers without modifying core code
5. **MuleSoft Native**: Follow MuleSoft connector development best practices

## Architecture Diagram

```
┌─────────────────────────────────────────────────────────────┐
│                    Mule Application                          │
│  ┌────────────┐  ┌────────────┐  ┌────────────┐            │
│  │   Flow 1   │  │   Flow 2   │  │   Flow N   │            │
│  └─────┬──────┘  └─────┬──────┘  └─────┬──────┘            │
│        │                │                │                   │
│        └────────────────┴────────────────┘                   │
│                         │                                    │
└─────────────────────────┼────────────────────────────────────┘
                          │
                          ▼
┌─────────────────────────────────────────────────────────────┐
│              MuleMind Connector                          │
│                                                              │
│  ┌──────────────────────────────────────────────────────┐  │
│  │              LlmOperations                            │  │
│  │  • chatCompletion()                                   │  │
│  │  • advancedChat()                                     │  │
│  │  • simplePrompt()                                     │  │
│  │  • testConnection()                                   │  │
│  └────────────────┬─────────────────────────────────────┘  │
│                   │                                          │
│  ┌────────────────▼─────────────────────────────────────┐  │
│  │         LlmProviderFactory                            │  │
│  │  Creates appropriate provider based on configuration  │  │
│  └────────────────┬─────────────────────────────────────┘  │
│                   │                                          │
│  ┌────────────────▼─────────────────────────────────────┐  │
│  │         LlmProviderClient (Interface)                 │  │
│  │  • complete(request)                                  │  │
│  │  • testConnection()                                   │  │
│  └─────┬────────────────────────────────────────────────┘  │
│        │                                                    │
│  ┌─────▼──────┬──────────┬──────────┬──────────┐          │
│  │  OpenAI    │Anthropic │  Google  │   AWS    │          │
│  │  Provider  │ Provider │ Provider │ Bedrock  │          │
│  └────────────┴──────────┴──────────┴──────────┘          │
│                                                              │
└──────────────────────────┬───────────────────────────────────┘
                           │
                           ▼
              ┌────────────────────────┐
              │   External LLM APIs    │
              │  • OpenAI              │
              │  • Anthropic           │
              │  • Google              │
              │  • AWS Bedrock         │
              └────────────────────────┘
```

## Core Components

### 1. Configuration Layer

**LlmConfiguration**
- Holds connector configuration (provider, API key, model, etc.)
- Provides defaults and validation
- Used by all operations

**LlmProvider (Enum)**
- Defines supported providers
- Stores provider-specific defaults
- Maps to concrete implementations

### 2. Operations Layer

**LlmOperations**
- Exposes MuleSoft operations to users
- Handles parameter validation and transformation
- Delegates to appropriate provider
- Operations:
  - `simplePrompt`: Basic prompt → response
  - `chatCompletion`: Single-turn conversation with options
  - `advancedChat`: Multi-turn with full control
  - `testConnection`: Validate configuration

### 3. Provider Abstraction Layer

**LlmProviderClient (Interface)**
```java
public interface LlmProviderClient {
    CompletionResponse complete(CompletionRequest request);
    boolean testConnection();
    LlmConfiguration getConfiguration();
}
```

**LlmProviderFactory**
- Factory pattern for provider instantiation
- Returns correct provider based on configuration
- Centralizes provider creation logic

### 4. Provider Implementation Layer

Each provider implements `LlmProviderClient`:

**OpenAiProvider**
- Direct OpenAI API integration
- Handles OpenAI-specific request/response format
- Uses standard HTTP client

**AnthropicProvider**
- Anthropic Claude API integration
- Translates between OpenAI format and Anthropic format
- Handles Anthropic-specific headers

**Future Providers**
- GoogleProvider (Gemini)
- AwsBedrockProvider
- AzureOpenAiProvider

### 5. Model Layer

**CompletionRequest**
- Universal request format
- Builder pattern for easy construction
- Mapped to provider-specific format

**CompletionResponse**
- Universal response format
- Parsed from provider-specific responses
- Includes usage/token information

**Message**
- Represents conversation messages
- Simple role + content structure

## Data Flow

### Simple Prompt Flow

```
User → MuleFlow → simplePrompt()
                      ↓
                  Validate input
                      ↓
              Build CompletionRequest
                      ↓
              LlmProviderFactory
                      ↓
         Get provider (e.g., OpenAI)
                      ↓
              provider.complete(request)
                      ↓
         Convert to provider format
                      ↓
              HTTP POST to API
                      ↓
           Parse API response
                      ↓
       Convert to CompletionResponse
                      ↓
           Extract content string
                      ↓
                 Return to user
```

## Extension Points

### Adding a New Provider

1. **Create Provider Class**
```java
public class NewProvider implements LlmProviderClient {
    // Implement interface methods
}
```

2. **Add to Enum**
```java
public enum LlmProvider {
    // ...
    NEW_PROVIDER("New Provider", "https://api.new.com", "model-v1")
}
```

3. **Update Factory**
```java
case NEW_PROVIDER -> new NewProvider(configuration);
```

4. **Write Tests**
```java
@Test
void testNewProviderCompletion() {
    // Test implementation
}
```

### Adding a New Operation

1. **Add to LlmOperations**
```java
@DisplayName("New Operation")
public String newOperation(@Config LlmConfiguration config, ...) {
    // Implementation
}
```

2. **Update Documentation**

## Technology Stack

| Component | Technology | Version |
|-----------|-----------|---------|
| Language | Java | 17+ |
| Build Tool | Maven | 3.6+ |
| MuleSoft | Mule SDK | 0.7.0 |
| HTTP Client | Apache HttpClient 5 | 5.2.1 |
| JSON | Jackson | 2.15.3 |
| Logging | SLF4J | 1.7.36 |
| Testing | JUnit 5 | 5.10.0 |

## Performance Considerations

1. **Connection Pooling**: HTTP client uses connection pooling
2. **Async Support**: Operations are synchronous but non-blocking
3. **Timeout Handling**: Configurable timeouts prevent hanging
4. **Memory**: Lightweight model objects minimize heap usage

## Security

1. **API Key Management**: Keys stored in configuration, not code
2. **HTTPS Only**: All API communication over TLS
3. **Input Validation**: Validate all user inputs
4. **Error Messages**: Sanitized error messages (no key leakage)

## Error Handling

```
Operation Called
      ↓
Try to Execute
      ↓
  Error Occurs?
      ↓
LlmProviderException
      ↓
Contains:
  • Error message
  • HTTP status code
  • Provider name
      ↓
Propagate to Mule
      ↓
Global Error Handler
```

## Future Enhancements

### Phase 2 (Streaming)
- Server-Sent Events (SSE) support
- Reactive streams integration
- Progress callbacks

### Phase 3 (Advanced Features)
- Response caching layer
- Token counting utilities
- Cost estimation
- Rate limiting
- Retry with exponential backoff

### Phase 4 (Enterprise Features)
- PII detection/redaction
- Audit logging
- Multi-tenancy support
- Custom prompt templates
- A/B testing support

## Comparison with MAC Project

| Aspect | MAC Project | This Connector |
|--------|------------|----------------|
| Base Framework | LangChain4j | Direct APIs |
| Java Version | 8/17 hybrid | 17+ native |
| Dependencies | Heavy | Minimal |
| Focus | AI Agents | Direct LLM calls |
| Complexity | High | Low |
| Learning Curve | Steep | Gentle |
| Use Case | Complex AI workflows | Simple LLM integration |

## Design Decisions

### Why Not Use LangChain4j?

1. **Overhead**: LangChain4j adds significant dependency weight
2. **Complexity**: Most users need simple LLM calls, not agent frameworks
3. **Performance**: Direct API calls are faster
4. **Control**: Full control over request/response handling
5. **Maintenance**: Smaller codebase is easier to maintain

### Why Provider Abstraction?

1. **Consistency**: Same interface for all providers
2. **Testability**: Easy to mock providers
3. **Flexibility**: Switch providers without code changes
4. **Extensibility**: Add new providers easily

### Why Builder Pattern?

1. **Readability**: Fluent API is easy to read
2. **Flexibility**: Optional parameters handled cleanly
3. **Validation**: Can validate before building
4. **Immutability**: Objects can be immutable after building

## Conclusion

This architecture provides a solid foundation for LLM integration in MuleSoft while remaining simple, performant, and extensible. The design allows for future enhancements without major refactoring.
