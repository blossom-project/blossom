package org.springframework.boot.actuate.trace.http;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Map;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class HttpTraceCreator {
  public static HttpTrace createHttpTraceMock(String uri) {
    TraceableRequest request = mock(TraceableRequest.class);
    try {
      when(request.getUri()).thenReturn(new URI(uri));
    } catch (URISyntaxException e) {
      e.printStackTrace();
    }
    return new HttpTrace(request);
  }

  public static HttpTrace createHttpTraceMock(String uri, Map<String, List<String>> requestHeaders, Map<String, List<String>> responseHeaders) {
    TraceableRequest request = mock(TraceableRequest.class);
    try {
      when(request.getUri()).thenReturn(new URI(uri));
    } catch (URISyntaxException e) {
      e.printStackTrace();
    }
    when(request.getHeaders()).thenReturn(requestHeaders);
    HttpTrace ret = new HttpTrace(request);

    TraceableResponse traceableResponse = mock(TraceableResponse.class);
    when(traceableResponse.getHeaders()).thenReturn(responseHeaders);
    HttpTrace.Response response = new HttpTrace.Response(traceableResponse);
    ret.setResponse(response);

    return ret;
  }
}
