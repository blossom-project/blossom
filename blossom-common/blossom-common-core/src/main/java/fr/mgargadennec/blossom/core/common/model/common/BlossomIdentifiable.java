package fr.mgargadennec.blossom.core.common.model.common;

import java.io.Serializable;

public interface BlossomIdentifiable<ID extends Serializable> {

  ID getId();

}
