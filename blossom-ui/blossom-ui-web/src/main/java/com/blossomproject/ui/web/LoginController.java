package com.blossomproject.ui.web;

import com.blossomproject.ui.stereotype.BlossomController;
import org.springframework.security.web.savedrequest.DefaultSavedRequest;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;
import org.springframework.security.web.savedrequest.RequestCache;
import org.springframework.security.web.savedrequest.SavedRequest;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.util.UriComponentsBuilder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Optional;

@BlossomController
@RequestMapping("/login")
public class LoginController {


  private String clientId;
  private String azureCallback;
  private String tenantId;
  private boolean hasClientSecret;

  public LoginController(String clientId, String azureCallback, String tenantId, boolean hasClientSecret) {
    this.clientId = clientId;
    this.azureCallback = azureCallback;
    this.tenantId = tenantId;
    this.hasClientSecret = hasClientSecret;
  }

  @GetMapping
  public ModelAndView getLoginPage( HttpServletRequest req, HttpServletResponse response, @RequestParam Optional<String> error, Model model) {
    if(clientId!=null && !clientId.equals("") && hasClientSecret){
      UriComponentsBuilder url = UriComponentsBuilder.fromUriString("https://login.microsoftonline.com/"+tenantId+"/oauth2/authorize");
      url.queryParam("client_id", clientId);
      url.queryParam("response_type", "code");
      url.queryParam("redirect_uri", req.getRequestURL().toString().split("/blossom")[0]+azureCallback);
      url.queryParam("response_mode", "query");


      RequestCache requestCache = new HttpSessionRequestCache();
      SavedRequest savedRequest = requestCache.getRequest(req, response);
      if (savedRequest!=null){
            url.queryParam("state", ((DefaultSavedRequest) savedRequest).getRequestURI());
      }else{
          url.queryParam("state","/blossom");
      }
        model.addAttribute("azureUrl", url.toUriString());
    }

    return new ModelAndView("blossom/login/login", "error", error);
  }






}
