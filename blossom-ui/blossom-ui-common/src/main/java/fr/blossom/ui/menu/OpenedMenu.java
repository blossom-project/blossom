package fr.blossom.ui.menu;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Created by Maël Gargadennnec on 08/06/2017.
 */
@Retention(value=RUNTIME)
@Target(value={ElementType.TYPE, ElementType.METHOD})
public @interface OpenedMenu {

  String value() default "";

}
