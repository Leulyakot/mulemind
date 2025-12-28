# Contributing to MuleMind Connector

Thank you for your interest in contributing to the MuleMind Connector! This document provides guidelines and instructions for contributing.

## 🎯 How Can I Contribute?

### Reporting Bugs

Before creating bug reports, please check existing issues to avoid duplicates. When creating a bug report, include:

- **Clear title and description**
- **Steps to reproduce** the behavior
- **Expected vs actual behavior**
- **Environment details** (Java version, MuleSoft version, OS)
- **Configuration used** (sanitized, no API keys!)
- **Error messages and stack traces**

### Suggesting Enhancements

Enhancement suggestions are tracked as GitHub issues. When creating an enhancement suggestion, include:

- **Clear title and description**
- **Use case** - why is this enhancement needed?
- **Proposed solution** or approach
- **Alternatives considered**

### Pull Requests

1. **Fork the repo** and create your branch from `main`
2. **Add tests** for any new functionality
3. **Update documentation** as needed
4. **Follow code style** (see below)
5. **Ensure tests pass** before submitting
6. **Write a clear PR description**

## 🏗️ Development Setup

### Prerequisites

- Java 17 or higher
- Maven 3.6+
- Git
- MuleSoft Anypoint Studio (optional, for testing)

### Getting Started

```bash
# Clone your fork
git clone https://github.com/YOUR_USERNAME/mulemind-connector.git
cd mulemind-connector

# Create a feature branch
git checkout -b feature/your-feature-name

# Build the project
mvn clean install
```

## 📝 Code Style Guidelines

### Java Code

- Use **Java 17 features** where appropriate
- Follow **standard Java naming conventions**
- Keep methods **focused and small** (< 50 lines)
- Add **JavaDoc** for public methods and classes
- Use **meaningful variable names**

### Example:

```java
/**
 * Sends a completion request to the LLM provider
 *
 * @param request the completion request
 * @return the completion response
 * @throws LlmProviderException if the request fails
 */
public CompletionResponse complete(CompletionRequest request) throws LlmProviderException {
    // Implementation
}
```

### Testing

- Write **unit tests** for all new functionality
- Use **descriptive test names** that explain what is being tested
- Aim for **high code coverage** (>80%)
- Include **integration tests** for provider implementations

```java
@Test
void testOpenAiProviderSuccessfulCompletion() {
    // Given
    CompletionRequest request = CompletionRequest.builder()
        .model("gpt-4")
        .addUserMessage("Hello")
        .build();
    
    // When
    CompletionResponse response = provider.complete(request);
    
    // Then
    assertNotNull(response);
    assertNotNull(response.getContent());
}
```

## 🔄 Git Workflow

### Branch Naming

- `feature/` - New features
- `fix/` - Bug fixes
- `docs/` - Documentation updates
- `refactor/` - Code refactoring
- `test/` - Test improvements

Examples:
- `feature/google-provider`
- `fix/openai-timeout-handling`
- `docs/update-readme-examples`

### Commit Messages

Follow [Conventional Commits](https://www.conventionalcommits.org/):

```
<type>: <description>

[optional body]

[optional footer]
```

Types:
- `feat`: New feature
- `fix`: Bug fix
- `docs`: Documentation changes
- `style`: Code style changes (formatting)
- `refactor`: Code refactoring
- `test`: Adding or updating tests
- `chore`: Maintenance tasks

Examples:
```
feat: add Google Gemini provider support

fix: handle timeout errors in OpenAI provider

docs: update configuration examples in README
```

## 🧪 Testing Guidelines

### Running Tests

```bash
# Run all tests
mvn test

# Run specific test class
mvn test -Dtest=OpenAiProviderTest

# Run with coverage
mvn clean test jacoco:report
```

### Integration Tests

For testing actual API calls:

1. **Use test API keys** (never commit real keys)
2. **Mock external calls** in unit tests
3. **Use test accounts** with limited quotas
4. Create a `test.properties` file (gitignored):

```properties
openai.test.api.key=sk-test-...
anthropic.test.api.key=sk-ant-test-...
```

## 📚 Adding New Providers

To add support for a new LLM provider:

1. **Create provider class** implementing `LlmProviderClient`
2. **Add to `LlmProvider` enum**
3. **Update `LlmProviderFactory`**
4. **Write comprehensive tests**
5. **Update documentation**

Example structure:

```java
public class GoogleProvider implements LlmProviderClient {
    
    @Override
    public CompletionResponse complete(CompletionRequest request) 
            throws LlmProviderException {
        // Implementation
    }
    
    @Override
    public boolean testConnection() {
        // Implementation
    }
    
    @Override
    public LlmConfiguration getConfiguration() {
        return configuration;
    }
}
```

## 📖 Documentation

### What to Document

- **Public APIs** - All public methods and classes
- **Configuration options** - New parameters and their purpose
- **Examples** - Real-world usage scenarios
- **Migration guides** - For breaking changes

### Where to Document

- **JavaDoc** - In the code
- **README.md** - User-facing documentation
- **CHANGELOG.md** - Version history and changes
- **Wiki** - Detailed guides and tutorials

## ✅ Checklist Before Submitting PR

- [ ] Code compiles without errors
- [ ] All tests pass
- [ ] New tests added for new functionality
- [ ] Documentation updated
- [ ] Code follows style guidelines
- [ ] Commit messages follow conventions
- [ ] No API keys or secrets in code
- [ ] PR description is clear and complete

## 🤔 Questions?

- **GitHub Discussions** - For general questions
- **GitHub Issues** - For bugs and feature requests
- **Email** - support@example.com

## 📜 Code of Conduct

Be respectful, inclusive, and professional. We're all here to build something great together!

---

Thank you for contributing to MuleMind Connector! 🎉
