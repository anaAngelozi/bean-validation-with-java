package br.com.acrossatto.pocvalidation.validations;

import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

@Target({ PARAMETER })
@Retention(RUNTIME)
@Constraint(validatedBy = RequestValidator.class)
@Documented
public @interface RequestValid {

	String message() default "{Request.invalid}";

	Class<?>[] groups() default {};

	Class<? extends Payload>[] payload() default {};
	
}