package de.dynamicfiles.projects.testing.icanseealiens.jsp.jpa.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import javax.ejb.TransactionAttributeType;
import javax.interceptor.InterceptorBinding;

@InterceptorBinding
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.TYPE})
/**
 * Has to be placed in jsf-beans, because the Tx begins there!
 */
public @interface Transactional {

    TransactionAttributeType value() default TransactionAttributeType.REQUIRED;
}
