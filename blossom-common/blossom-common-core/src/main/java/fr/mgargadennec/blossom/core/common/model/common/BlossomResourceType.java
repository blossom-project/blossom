package fr.mgargadennec.blossom.core.common.model.common;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * Definit le nom de la resource. Valeur utilis�e par le module security pour d�finir les droits d'acc�s.
 *
 */
@Documented
@Target(TYPE)
@Retention(RUNTIME)
@Inherited
public @interface BlossomResourceType {
  /**
   * 
   * @return la constante associ�e � cette entit�
   */
  String value();
}