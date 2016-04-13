package fr.mgargadennec.blossom.core.common.model.common;

import java.io.Serializable;
import java.util.Date;


public interface IBlossomEntityDTO<S extends Serializable> extends BlossomIdentifiable<S> {

  S getId();

  Date getDateCreation();

  Date getDateModification();

  String getUserCreation();

  String getUserModification();

}