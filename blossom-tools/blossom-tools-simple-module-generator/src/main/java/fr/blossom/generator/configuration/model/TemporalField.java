package fr.blossom.generator.configuration.model;

import javax.persistence.TemporalType;

public interface TemporalField extends Field {

  TemporalType getTemporalType();

}
