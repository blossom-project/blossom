package com.blossomproject.autoconfigure.ui.web;

import com.blossomproject.model.azureactivedirectory.AzureADLoginAuthenticationFilter;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConditionalOnClass(AzureADLoginAuthenticationFilter.class)
@AutoConfigureBefore(WebInterfaceAutoConfiguration.class)
@ConfigurationProperties("blossom.azure.ad")
public class AzureADAuthenticationProperties {
    private String clientId;
    private String clientSecret;
    private String tenantId = "common";


    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public String getClientSecret() {
        return clientSecret;
    }

    public void setClientSecret(String clientSecret) {
        this.clientSecret = clientSecret;
    }

    public String getTenantId() {
        return tenantId;
    }

    public void setTenantId(String tenantId) {
        this.tenantId = tenantId;
    }

}
