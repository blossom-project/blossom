package fr.blossom.ui.web;

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;

import java.util.NoSuchElementException;
import javax.servlet.http.HttpServletRequest;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.http.HttpHeaders;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.servlet.NoHandlerFoundException;

@RunWith(MockitoJUnitRunner.class)
public class ErrorControllerAdviceTest {

  private ErrorControllerAdvice advice;

  @Before
  public void setUp() throws Exception {
    this.advice = new ErrorControllerAdvice();
  }

  @Test
  public void should_display_error_when_Throwable() throws Exception {
    assertTrue(advice.handleDefault(mock(HttpServletRequest.class), new Throwable()).getViewName()
      .equals("error/default"));
  }

  @Test
  public void should_display_error_when_HttpRequestMethodNotSupportedException() throws Exception {
    assertTrue(advice.handleMethodNotSupported(mock(HttpServletRequest.class),
      new HttpRequestMethodNotSupportedException("GET")).getViewName().equals("error/405"));
  }

  @Test
  public void should_display_error_when_NoHandlerFoundException() throws Exception {
    assertTrue(advice.handleNotFound(mock(HttpServletRequest.class),
      new NoHandlerFoundException("GET", "/test", new HttpHeaders())).getViewName()
      .equals("error/404"));
  }

  @Test
  public void should_display_error_when_NoSuchElementException() throws Exception {
    assertTrue(advice.handleNotFound(mock(HttpServletRequest.class),
      new NoSuchElementException()).getViewName()
      .equals("error/404"));
  }

  @Test
  public void should_display_error_when_() throws Exception {
    assertTrue(advice.handleForbidden(mock(HttpServletRequest.class),
      new AccessDeniedException("")).getViewName()
      .equals("error/403"));
  }
}
