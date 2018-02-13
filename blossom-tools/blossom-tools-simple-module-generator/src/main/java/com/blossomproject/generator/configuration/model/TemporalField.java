package com.blossomproject.generator.configuration.model;

import javax.persistence.TemporalType;

public interface TemporalField extends Field {

  TemporalType getTemporalType();

}
