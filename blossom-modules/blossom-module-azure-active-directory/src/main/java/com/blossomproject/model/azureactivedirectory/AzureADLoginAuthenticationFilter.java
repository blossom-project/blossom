package com.blossomproject.model.azureactivedirectory;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.tomcat.util.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

public class AzureADLoginAuthenticationFilter extends GenericFilterBean {
    private static Logger logger = LoggerFactory.getLogger(AzureADLoginAuthenticationFilter.class);
    private final AuthenticationManager authenticationManager;
    private final String clientId;
    private final String clientSecret;
    private final String azureCallback;
    private final String tenantId;

    public AzureADLoginAuthenticationFilter(AuthenticationManager authenticationManager, String clientId, String clientSecret, String azureCallback, String tenantId) {
        this.authenticationManager = authenticationManager;
        this.clientId = clientId;
        this.clientSecret = clientSecret;
        this.azureCallback = azureCallback;
        this.tenantId = tenantId;
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        if(req.getRequestURI().contains("azure")){
            AzureADIdTokenResponse idToken = getAzureIdentity(request.getParameter("code"),req.getRequestURL().toString().split("/blossom")[0]+azureCallback, request.getParameter("state"));
            if(idToken!=null){
                try {
                    Authentication auth = authenticationManager.authenticate(new PreAuthenticationAzureToken(idToken.getUserPrincipalName()));

                    if (auth != null) {
                        SecurityContextHolder.getContext().setAuthentication(auth);
                    }
                }catch (Exception e){
                    logger.debug("Authentication error: ", e);
                    HttpSession session = ((HttpServletRequest) request).getSession(false);
                    session.setAttribute("errorAzure", e.getMessage());
                    ((HttpServletResponse) response).sendRedirect("/blossom/login");
                    return;
                }
            }else {
                ((HttpServletResponse) response).sendError(401);
                return;
            }
        }

        chain.doFilter(request,response);

    }


    private AzureADIdTokenResponse getAzureIdentity(String code,String redirectUri, String redirectPath){
        RestTemplate restTemplate = new RestTemplate();
        ObjectMapper mapper = new ObjectMapper();
        String url = "https://login.microsoftonline.com/"+tenantId+"/oauth2/token";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> params = new LinkedMultiValueMap<String, String>();
        params.add("client_id", clientId);
        params.add("client_secret",clientSecret);
        params.add("code", code);
        params.add("redirect_uri", redirectUri);
        params.add("response_mode", "query");
        params.add("grant_type", "authorization_code");
        params.add("resource",clientId);
        params.add("state",redirectPath);


        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<MultiValueMap<String, String>>(params, headers);

        try {
            ResponseEntity<AzureADLoginResponse> oidcAccessTokenResponseEntity = restTemplate.postForEntity(url, request, AzureADLoginResponse.class);
            return mapper.readValue(
                    new String(Base64.decodeBase64(oidcAccessTokenResponseEntity.getBody().getIdToken().split("\\.")[1]), "UTF-8"),
                    AzureADIdTokenResponse.class);
        } catch (HttpClientErrorException e) {
            logger.error("Error with response: {}", e.getResponseBodyAsString(), e);
        } catch (Exception e) {
            logger.error("", e);
        }

        return null;
    }
}
