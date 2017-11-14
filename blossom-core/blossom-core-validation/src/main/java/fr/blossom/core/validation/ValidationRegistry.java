package fr.blossom.core.validation;

import java.util.Collection;

/**
 * Created by Maël Gargadennnec on 15/06/2017.
 */
public interface ValidationRegistry {

  BeanValidationConstraints getContraintsFor(Class<?> clazz);

  boolean hasContraintsFor(Class<?> delimiter);

  Collection<BeanValidationConstraints> getContraints();

  void addContraints(Class<?> clazz, BeanValidationConstraints constraints);
}
