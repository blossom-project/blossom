package org.springframework.boot.actuate.trace.http;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.net.URI;
import java.net.URISyntaxException;

public class HttpTraceCreator {
  public static HttpTrace createHttpTraceMock(String uri){
    TraceableRequest request = mock(TraceableRequest.class);
    try {
      when(request.getUri()).thenReturn(new URI(uri));
    } catch (URISyntaxException e) {
      e.printStackTrace();
    }
    return new HttpTrace(request);
  }
}
