package fr.blossom.core.validation;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Maël Gargadennnec on 15/06/2017.
 */
public class FieldValidationContraints {
  private final List<FieldValidationConstraint> contraints;

  public FieldValidationContraints() {
    this.contraints = new ArrayList<>();
  }

  public void addConstraint(FieldValidationConstraint constraint) {
    this.contraints.add(constraint);
  }

  public List<FieldValidationConstraint> getContraints() {
    return contraints;
  }
}
