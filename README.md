# 🧠 MuleMind Connector

**Lightning-fast LLM integration for MuleSoft**

[![Java 8+](https://img.shields.io/badge/Java-8%2B-blue.svg)](https://www.oracle.com/java/)
[![MuleSoft 4.6+](https://img.shields.io/badge/MuleSoft-4.6%2B-blue.svg)](https://www.mulesoft.com/)
[![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)](LICENSE)

MuleMind is a lightweight, modern connector that brings the power of Large Language Models directly into your MuleSoft applications. Built for speed and simplicity, it lets you integrate AI without the complexity.

## 📋 Prerequisites & Setup

**Quick Start - No Credentials Required:**

```bash
# Clone and build (works immediately)
mvn clean package
```

The default build uses JAR packaging and Maven Central only. **No MuleSoft credentials needed!**

### Requirements

1. **Java 8 or 11** - MuleSoft 4.x runtime requirement
2. **Maven 3.6+** - Standard Maven installation

### Advanced: Full Connector Mode

For MuleSoft mule-extension packaging with enterprise features:

```bash
mvn clean package -P mule-connector
```

This requires Anypoint Platform credentials.

**📖 See [MULESOFT_SETUP.md](MULESOFT_SETUP.md) for:**
- Two build modes explained (JAR vs mule-extension)
- Configuring Anypoint Platform credentials
- Troubleshooting common issues

## ✨ Why MuleMind?

- **🚀 Blazing Fast** - Direct API integration, no heavyweight frameworks
- **🎯 Simple** - Intuitive operations that just work
- **🔌 Multi-Provider** - OpenAI, Anthropic, Google, AWS Bedrock, Azure
- **⚡ Modern** - Built with Java 8+, MuleSoft SDK best practices
- **📦 Lightweight** - Minimal dependencies, maximum performance
- **🛠️ Extensible** - Easy to customize and extend

## 🎬 Quick Start

### 1. Add to your Mule app

```xml
<dependency>
    <groupId>com.mulemind</groupId>
    <artifactId>mulemind-connector</artifactId>
    <version>1.0.0-SNAPSHOT</version>
    <classifier>mule-plugin</classifier>
</dependency>
```

### 2. Configure

```xml
<mulemind:config name="MuleMind_Config">
    <mulemind:connection 
        provider="OPENAI" 
        apiKey="${openai.api.key}" 
        model="gpt-4-turbo-preview"/>
</mulemind:config>
```

### 3. Use it!

```xml
<flow name="askAI">
    <http:listener path="/ask" config-ref="HTTP_Listener_config"/>
    <mulemind:simple-prompt config-ref="MuleMind_Config" prompt="#[payload]"/>
</flow>
```

That's it! 🎉

## 🔥 Features

### Supported Providers

| Provider | Status | Models |
|----------|--------|--------|
| **OpenAI** | ✅ Ready | GPT-4, GPT-3.5, GPT-4 Turbo |
| **Anthropic** | ✅ Ready | Claude 3.5 Sonnet, Claude 3 Opus |
| **Google** | 🚧 Coming Soon | Gemini Pro, Gemini Ultra |
| **AWS Bedrock** | 🚧 Coming Soon | Multiple models |
| **Azure OpenAI** | 🚧 Coming Soon | GPT-4, GPT-3.5 |

### Operations

1. **Simple Prompt** - Quick Q&A
2. **Chat Completion** - Conversational AI with context
3. **Advanced Chat** - Full control over multi-turn conversations
4. **Test Connection** - Validate your setup

## 💡 Use Cases

- **Customer Support** - Automated, intelligent responses
- **Content Generation** - Blogs, emails, social media
- **Data Analysis** - Extract insights from text
- **Translation** - Multi-language support
- **Code Generation** - Automated coding assistance
- **Sentiment Analysis** - Understand customer feedback

## 📚 Documentation

- **[Quick Start Guide](QUICKSTART.md)** - Get running in 5 minutes
- **[Architecture Overview](ARCHITECTURE.md)** - How it works under the hood
- **[Contributing Guide](CONTRIBUTING.md)** - Help make MuleMind better
- **[Example Flows](examples/example-flows.xml)** - Real-world examples

## 🚀 Example: Customer Support Bot

```xml
<flow name="customerSupport">
    <http:listener path="/support" config-ref="HTTP_Listener"/>
    
    <mulemind:chat-completion config-ref="MuleMind_Config" 
        userMessage="#[payload.question]"
        systemPrompt="You are a helpful customer support agent. Be concise and friendly."
        temperature="0.3"
        maxTokens="500"/>
    
    <set-payload value='#[{"answer": payload, "timestamp": now()}]'/>
</flow>
```

## 🏗️ Architecture

```
MuleMind Connector
├── Lightweight Core (Java 8+)
├── Provider Abstraction Layer
├── Direct API Integration
└── Minimal Dependencies
```

**Key Principles:**
- Provider-agnostic design
- Builder pattern for requests
- Factory pattern for providers
- Clean separation of concerns

## 🤝 Contributing

We welcome contributions! See [CONTRIBUTING.md](CONTRIBUTING.md) for guidelines.

Quick contribution ideas:
- Add Google Gemini provider
- Implement streaming support
- Add response caching
- Improve error messages
- Write more examples

## 📊 Performance

- **Startup**: < 1 second
- **Request latency**: Minimal overhead (~5ms)
- **Memory**: Lightweight objects
- **Dependencies**: Only 4 core libraries

## 🔒 Security

- ✅ Secure API key management
- ✅ HTTPS-only communication
- ✅ Input validation
- ✅ Sanitized error messages
- ✅ No key leakage in logs

## 📄 License

MIT License - see [LICENSE](LICENSE) for details.

## 🙏 Acknowledgments

- Inspired by the [MAC Project](https://github.com/MuleSoft-AI-Chain-Project)
- Built for the MuleSoft community
- Powered by modern LLM APIs

## 🌟 Star Us!

If you find MuleMind useful, give us a star ⭐ on GitHub!

---

**MuleMind** - Think faster, integrate smarter. 🧠⚡
