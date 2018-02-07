package com.blossom_project.ui.web;

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;

import com.blossom_project.ui.web.error.ErrorControllerAdvice;
import java.util.NoSuchElementException;
import javax.servlet.http.HttpServletRequest;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;;
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
      .equals("blossom/error/default"));
  }

  @Test
  public void should_display_error_when_HttpRequestMethodNotSupportedException() throws Exception {
    assertTrue(advice.handleMethodNotSupported(mock(HttpServletRequest.class),
      new HttpRequestMethodNotSupportedException("GET")).getViewName().equals("blossom/error/405"));
  }

  @Test
  public void should_display_error_when_NoHandlerFoundException() throws Exception {
    assertTrue(advice.handleNotFound(mock(HttpServletRequest.class),
      new NoHandlerFoundException("GET", "/test", new HttpHeaders())).getViewName()
      .equals("blossom/error/404"));
  }

  @Test
  public void should_display_error_when_NoSuchElementException() throws Exception {
    assertTrue(advice.handleNotFound(mock(HttpServletRequest.class),
      new NoSuchElementException()).getViewName()
      .equals("blossom/error/404"));
  }

  @Test
  public void should_display_error_when_() throws Exception {
    assertTrue(advice.handleForbidden(mock(HttpServletRequest.class),
      new AccessDeniedException("")).getViewName()
      .equals("blossom/error/403"));
  }
}
