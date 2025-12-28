package com.mulemind;

import org.mule.runtime.extension.api.annotation.Extension;
import org.mule.runtime.extension.api.annotation.Configurations;
import org.mule.runtime.extension.api.annotation.dsl.xml.Xml;
import com.mulemind.config.LlmConfiguration;

/**
 * MuleMind Connector
 * 
 * Lightning-fast LLM integration for MuleSoft
 * 
 * A lightweight, modern connector for integrating with LLM APIs including:
 * - OpenAI (GPT-4, GPT-3.5, GPT-4 Turbo)
 * - Anthropic (Claude 3.5 Sonnet, Claude 3 Opus)
 * - Google (Gemini) - Coming Soon
 * - AWS Bedrock - Coming Soon
 * - Azure OpenAI - Coming Soon
 * 
 * Built with Java 17+ for optimal performance and modern features.
 * 
 * @author MuleMind Team
 * @version 1.0.0
 */
@Extension(name = "MuleMind", vendor = "MuleMind")
@Configurations(LlmConfiguration.class)
@Xml(prefix = "mulemind")
public class MuleMindConnector {
    
    public static final String VERSION = "1.0.0";
    public static final String NAME = "MuleMind Connector";
    
}
