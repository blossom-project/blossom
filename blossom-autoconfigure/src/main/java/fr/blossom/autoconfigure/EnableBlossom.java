package fr.blossom.autoconfigure;

import org.springframework.boot.autoconfigure.AutoConfigurationPackage;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
@AutoConfigurationPackage
@Configuration
@EnableAutoConfiguration
@ComponentScan
@Import(EnableBlossomImportSelector.class)
public @interface EnableBlossom {

	Class<?>[] exclude() default {};

	String[] excludeName() default {};

}
