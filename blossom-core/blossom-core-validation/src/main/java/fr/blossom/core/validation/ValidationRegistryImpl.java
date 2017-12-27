package fr.blossom.core.validation;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by Maël Gargadennnec on 15/06/2017.
 */
public class ValidationRegistryImpl implements ValidationRegistry {
  private final static Logger logger = LoggerFactory.getLogger(ValidationRegistryImpl.class);
  private Map<Class<?>, BeanValidationConstraints> constraints = new ConcurrentHashMap<>();

  @Override
  public BeanValidationConstraints getContraintsFor(Class<?> clazz) {
    if (!this.hasContraintsFor(clazz)) {
      throw new RuntimeException("No contraints found for clazz " + clazz);
    }
    return this.constraints.get(clazz);
  }

  @Override
  public boolean hasContraintsFor(Class<?> delimiter) {
    return this.constraints.containsKey(delimiter);
  }

  @Override
  public Collection<BeanValidationConstraints> getContraints() {
    return constraints.values();
  }

  @Override
  public void addContraints(Class<?> clazz, BeanValidationConstraints constraints) {
    if (logger.isWarnEnabled() && this.constraints.containsKey(clazz)) {
      logger.warn("Overriding contraints for class {}", clazz);
    }
    this.constraints.put(clazz, constraints);
  }
}
