package fr.blossom.ui.web;

import fr.blossom.ui.stereotype.BlossomController;
import java.util.NoSuchElementException;
import javax.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.NoHandlerFoundException;

@ControllerAdvice(annotations = BlossomController.class)
public class ErrorControllerAdvice {
  private final static Logger logger = LoggerFactory.getLogger(ErrorControllerAdvice.class);

  @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
  @ExceptionHandler(Throwable.class)
  public ModelAndView handleDefault(HttpServletRequest req, Throwable throwable) {
    return new ModelAndView("error/default");
  }

  @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
  @ResponseStatus(HttpStatus.METHOD_NOT_ALLOWED)
  public ModelAndView handleMethodNotSupported(HttpServletRequest req,
    HttpRequestMethodNotSupportedException exception) {
    return new ModelAndView("error/405");
  }

  @ExceptionHandler(NoHandlerFoundException.class)
  @ResponseStatus(HttpStatus.NOT_FOUND)
  public ModelAndView handleNotFound(HttpServletRequest req, NoHandlerFoundException exception) {
    return new ModelAndView("error/404");
  }

  @ExceptionHandler(NoSuchElementException.class)
  @ResponseStatus(HttpStatus.NOT_FOUND)
  public ModelAndView handleNotFound(HttpServletRequest req, NoSuchElementException exception) {
    return new ModelAndView("error/404");
  }

  @ExceptionHandler(AccessDeniedException.class)
  @ResponseStatus(HttpStatus.FORBIDDEN)
  public ModelAndView handleForbidden(HttpServletRequest req, AccessDeniedException exception) {
    return new ModelAndView("error/403");
  }
}
