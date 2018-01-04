package fr.blossom.generator.configuration.model;

public interface StringField extends Field {

  boolean isNotBlank();

  Integer getMaxLength();

  boolean isLob();
}
