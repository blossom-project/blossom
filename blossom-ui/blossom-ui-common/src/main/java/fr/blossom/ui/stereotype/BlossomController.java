package fr.blossom.ui.stereotype;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.springframework.core.annotation.AliasFor;
import org.springframework.stereotype.Controller;

/**
 * Created by Maël Gargadennnec on 04/05/2017.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Controller
public @interface BlossomController {
  @AliasFor(annotation = Controller.class, attribute = "value")
  String value() default "";
}
