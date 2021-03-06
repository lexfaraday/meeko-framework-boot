package org.meeko.sit.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.springframework.beans.factory.annotation.Autowired;

/**
 * MeekoTestFlow annotation uses for a follow all steps
 * This steps publish in a WebSocket with name /meeko/flow
 * @author Alej4ndro G0m3z.
 *
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@Autowired
public @interface MeekoTestFlow {
}
