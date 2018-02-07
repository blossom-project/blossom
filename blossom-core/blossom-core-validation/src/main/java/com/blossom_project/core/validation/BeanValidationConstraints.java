package com.blossom_project.core.validation;

import org.springframework.plugin.core.Plugin;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Maël Gargadennnec on 15/06/2017.
 */
public class BeanValidationConstraints implements Plugin<Class<?>> {
  private final Class<?> clazz;
  private final Map<String, FieldValidationContraints> contraints;

  public BeanValidationConstraints(Class<?> clazz) {
    this.clazz = clazz;
    this.contraints = new HashMap<>();
  }

  @PostConstruct
  public void initialize(){

  }

  @Override
  public boolean supports(Class<?> delimiter) {
    return clazz.isAssignableFrom(delimiter);
  }
}
