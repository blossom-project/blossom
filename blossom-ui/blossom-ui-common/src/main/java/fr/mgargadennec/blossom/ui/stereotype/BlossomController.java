package fr.mgargadennec.blossom.ui.stereotype;

import org.springframework.core.annotation.AliasFor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by Maël Gargadennnec on 04/05/2017.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Controller
@RequestMapping
public @interface BlossomController {
  @AliasFor(annotation = RequestMapping.class, attribute = "value")
  String[] value() default "";
}
