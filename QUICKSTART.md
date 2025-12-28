# Quick Start Guide - MuleMind Connector

Get up and running with the MuleMind Connector in 5 minutes!

## Prerequisites

- ✅ Java 17 or higher installed
- ✅ Maven 3.6+ installed
- ✅ MuleSoft Anypoint Studio (recommended) or any IDE
- ✅ An API key from OpenAI or Anthropic

## Step 1: Get an API Key

### OpenAI
1. Go to https://platform.openai.com/api-keys
2. Click "Create new secret key"
3. Copy the key (starts with `sk-`)

### Anthropic
1. Go to https://console.anthropic.com/settings/keys
2. Click "Create Key"
3. Copy the key (starts with `sk-ant-`)

## Step 2: Build the Connector

```bash
# Clone or download the connector
git clone https://github.com/yourusername/mulemind-connector.git
cd mulemind-connector

# Build and install to local Maven repository
mvn clean install -DskipTests
```

You should see:
```
[INFO] BUILD SUCCESS
```

## Step 3: Create a New Mule Application

### Using Anypoint Studio:

1. **File → New → Mule Project**
2. Name: `llm-demo`
3. Click **Finish**

### Add the Connector Dependency:

Open `pom.xml` and add:

```xml
<dependency>
    <groupId>com.mulemind</groupId>
    <artifactId>mulemind-connector</artifactId>
    <version>1.0.0-SNAPSHOT</version>
    <classifier>mule-plugin</classifier>
</dependency>
```

## Step 4: Configure the Connector

Create `src/main/resources/application.properties`:

```properties
# Replace with your actual API key!
openai.api.key=sk-your-key-here

# Or use Anthropic
# anthropic.api.key=sk-ant-your-key-here
```

## Step 5: Create Your First Flow

Create `src/main/mule/llm-demo.xml`:

```xml
<?xml version="1.0" encoding="UTF-8"?>
<mule xmlns="http://www.mulesoft.org/schema/mule/core"
      xmlns:http="http://www.mulesoft.org/schema/mule/http"
      xmlns:llm="http://www.mulesoft.org/schema/mule/llm"
      xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
      xsi:schemaLocation="
        http://www.mulesoft.org/schema/mule/core http://www.mulesoft.org/schema/mule/core/current/mule.xsd
        http://www.mulesoft.org/schema/mule/http http://www.mulesoft.org/schema/mule/http/current/mule-http.xsd
        http://www.mulesoft.org/schema/mule/llm http://www.mulesoft.org/schema/mule/llm/current/mule-llm.xsd">

    <!-- HTTP Listener -->
    <http:listener-config name="HTTP_Listener_config">
        <http:listener-connection host="0.0.0.0" port="8081"/>
    </http:listener-config>

    <!-- LLM Config -->
    <llm:config name="LLM_Config">
        <llm:connection 
            provider="OPENAI" 
            apiKey="${openai.api.key}" 
            model="gpt-4-turbo-preview"/>
    </llm:config>

    <!-- Simple Flow -->
    <flow name="askLlmFlow">
        <http:listener config-ref="HTTP_Listener_config" path="/ask"/>
        
        <llm:simple-prompt config-ref="LLM_Config" prompt="#[payload]"/>
        
        <set-payload value='#[output application/json --- {"response": payload}]'/>
    </flow>

</mule>
```

## Step 6: Run Your Application

### In Anypoint Studio:
1. Right-click the project
2. Select **Run As → Mule Application**

### From Command Line:
```bash
mvn mule:run
```

## Step 7: Test It!

Open a new terminal and run:

```bash
# Simple test
curl -X POST http://localhost:8081/ask \
  -H "Content-Type: text/plain" \
  -d "What is the capital of France?"
```

You should get:

```json
{
  "response": "The capital of France is Paris."
}
```

## 🎉 Success!

You've successfully:
- ✅ Built the connector
- ✅ Created a Mule app
- ✅ Configured an LLM provider
- ✅ Made your first LLM call!

## Next Steps

### Try More Operations

**Chat with System Prompt:**
```xml
<llm:chat-completion 
    config-ref="LLM_Config" 
    userMessage="#[payload.message]"
    systemPrompt="You are a helpful assistant"
    temperature="0.7"/>
```

**Advanced Chat:**
```xml
<llm:advanced-chat config-ref="LLM_Config">
    <llm:messages>#[
        [
            {role: "system", content: "You are helpful"},
            {role: "user", content: payload.message}
        ]
    ]</llm:messages>
</llm:advanced-chat>
```

### Switch Providers

Change to Anthropic (Claude):

```xml
<llm:config name="LLM_Config">
    <llm:connection 
        provider="ANTHROPIC" 
        apiKey="${anthropic.api.key}" 
        model="claude-3-5-sonnet-20241022"/>
</llm:config>
```

### Explore Examples

Check out `examples/example-flows.xml` for:
- Customer support bots
- Content generation
- Multi-turn conversations
- Provider comparison
- And more!

## Troubleshooting

### Issue: "Connection refused"
**Solution**: Make sure the Mule app is running and listening on port 8081

### Issue: "Invalid API key"
**Solution**: Double-check your API key in `application.properties`

### Issue: "Provider not found"
**Solution**: Ensure provider is one of: OPENAI, ANTHROPIC, GOOGLE, AWS_BEDROCK, AZURE_OPENAI

### Issue: "Timeout"
**Solution**: Increase timeout in configuration:
```xml
<llm:connection ... timeoutSeconds="60"/>
```

## Tips

1. **Start simple** - Use `simple-prompt` first
2. **Use properties** - Never hardcode API keys
3. **Monitor usage** - Track tokens in responses
4. **Set limits** - Use `maxTokens` to control costs
5. **Temperature matters** - Lower (0.1-0.3) for factual, higher (0.7-1.0) for creative

## Resources

- 📖 [Full Documentation](README.md)
- 🏗️ [Architecture Guide](ARCHITECTURE.md)
- 🤝 [Contributing Guide](CONTRIBUTING.md)
- 💡 [Example Flows](examples/example-flows.xml)

## Getting Help

- 🐛 [Report Issues](https://github.com/yourusername/mulemind-connector/issues)
- 💬 [Discussions](https://github.com/yourusername/mulemind-connector/discussions)
- 📧 Email: support@example.com

---

**Happy Building! 🚀**
