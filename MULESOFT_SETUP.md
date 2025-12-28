# MuleSoft Dependency Setup Guide

## Overview

This MuleSoft connector requires access to MuleSoft's Enterprise repositories, which are protected and require authentication.

## Prerequisites

1. **Anypoint Platform Account**: You need an active Anypoint Platform account
   - Sign up at: https://anypoint.mulesoft.com/login/signup

2. **Maven 3.6+**: Ensure Maven is installed
   ```bash
   mvn --version
   ```

3. **Java 8 or Java 11**: MuleSoft 4.x requires Java 8 or 11
   ```bash
   java -version
   ```

## Configuration Steps

### Option 1: Using Anypoint Platform Credentials (Recommended)

1. **Set Environment Variables**:
   ```bash
   export ANYPOINT_USERNAME="your-anypoint-username"
   export ANYPOINT_PASSWORD="your-anypoint-password"
   ```

2. **Or add to your shell profile** (~/.bashrc, ~/.zshrc, etc.):
   ```bash
   echo 'export ANYPOINT_USERNAME="your-anypoint-username"' >> ~/.bashrc
   echo 'export ANYPOINT_PASSWORD="your-anypoint-password"' >> ~/.bashrc
   source ~/.bashrc
   ```

3. **Verify Maven settings**: The `~/.m2/settings.xml` file has been created with proper configuration.

### Option 2: Using MuleSoft EE Repository Credentials

If you have direct access to MuleSoft Enterprise Edition repositories:

1. **Set Environment Variables**:
   ```bash
   export MULESOFT_USERNAME="your-mulesoft-ee-username"
   export MULESOFT_PASSWORD="your-mulesoft-ee-password"
   ```

### Option 3: Using Community Edition (Limited Functionality)

For development without enterprise features, you can use MuleSoft CE:

1. Update `pom.xml` to use community edition dependencies
2. Some enterprise features may not be available

## Verify Setup

1. **Test Maven can access repositories**:
   ```bash
   mvn dependency:resolve
   ```

2. **Build the project**:
   ```bash
   mvn clean compile
   ```

3. **Package the connector**:
   ```bash
   mvn clean package
   ```

## Troubleshooting

### Error: "403 Forbidden" when accessing MuleSoft repositories

**Cause**: Missing or invalid credentials

**Solutions**:
1. Verify your Anypoint Platform credentials are correct
2. Check that environment variables are set: `echo $ANYPOINT_USERNAME`
3. Ensure your account has access to the necessary repositories
4. Try using your Anypoint organization ID as username

### Error: "Unknown packaging: mule-extension"

**Cause**: The `mule-extensions-maven-plugin` is not properly configured

**Solution**:
1. Ensure the plugin repositories are configured in `pom.xml`
2. Verify Maven can access MuleSoft repositories
3. Check that the plugin version matches your Mule version

### Error: "Non-resolvable parent POM"

**Cause**: Cannot download the parent POM from MuleSoft repositories

**Solution**:
1. Verify credentials are configured
2. Check network connectivity to `repository.mulesoft.org`
3. Try using Anypoint Exchange: `https://maven.anypoint.mulesoft.com/api/v3/maven`

### Network/Proxy Issues

If you're behind a corporate proxy:

1. **Add proxy configuration to `~/.m2/settings.xml`**:
   ```xml
   <proxies>
     <proxy>
       <id>corporate-proxy</id>
       <active>true</active>
       <protocol>http</protocol>
       <host>proxy.company.com</host>
       <port>8080</port>
       <username>proxyuser</username>
       <password>proxypass</password>
       <nonProxyHosts>localhost|127.0.0.1</nonProxyHosts>
     </proxy>
   </proxies>
   ```

## Alternative: Use Local Repository

If you have the dependencies cached locally or in a corporate repository:

1. **Configure corporate Maven repository in `pom.xml`**:
   ```xml
   <repositories>
     <repository>
       <id>corporate-repo</id>
       <url>https://your-corporate-repo.com/maven</url>
     </repository>
   </repositories>
   ```

## Getting Anypoint Platform Access

1. **Free Trial**: Sign up for a free trial at https://anypoint.mulesoft.com
2. **Enterprise**: Contact MuleSoft sales for enterprise access
3. **Community**: Use MuleSoft Community Edition (limited features)

## References

- [MuleSoft Maven Configuration](https://docs.mulesoft.com/mule-runtime/latest/maven-reference)
- [Anypoint Exchange Maven Facade](https://docs.mulesoft.com/exchange/to-publish-assets-maven)
- [MuleSoft SDK Documentation](https://docs.mulesoft.com/mule-sdk/latest/)

## Support

For issues specific to MuleSoft repository access:
- Contact MuleSoft Support: https://help.mulesoft.com
- Check MuleSoft Forums: https://help.mulesoft.com/s/forum
