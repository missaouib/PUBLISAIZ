package publisaiz.api.validators;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = IpValidator.class)
@interface Ip {

    String message() default "THIS IS NOT IP";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

}