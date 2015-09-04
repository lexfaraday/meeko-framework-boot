package org.meeko.sit.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * MeekoTest annotation uses for execute tests in custom environment
 * Marks classes with @Component and @Scope prototype to create always new instances
 * @author Alej4ndro G0m3z.
 *
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Component
@Scope("prototype")
public @interface MeekoTest {
    String[]environment();
}
