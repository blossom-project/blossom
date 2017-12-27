package fr.blossom.sample.configuration;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ws.client.core.support.WebServiceGatewaySupport;
import org.springframework.ws.soap.client.core.SoapActionCallback;
import whois.wsdl.GetWhoIS;
import whois.wsdl.GetWhoISResponse;

public class WhoIsClient extends WebServiceGatewaySupport {

  private static final Logger log = LoggerFactory.getLogger(WhoIsClient.class);

  public GetWhoISResponse getWhoIs(String hostname) {

    GetWhoIS request = new GetWhoIS();
    request.setHostName(hostname);

    log.info("Requesting whois for {}", hostname);

    GetWhoISResponse response = (GetWhoISResponse) getWebServiceTemplate()
      .marshalSendAndReceive("http://www.webservicex.com/whois.asmx",
        request,
        new SoapActionCallback("http://www.webservicex.net/GetWhoIS"));

    return response;
  }

}
