/*
 * Copyright (c) 2018. brzezinski
 * author: Michal Brzezinski
 ******************************************************************************/

package publisaiz.controller.api.validators;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.regex.Pattern;

public class IpValidator implements ConstraintValidator<Ip, String> {

    public static final Pattern IP_PATTERN = Pattern.compile(
            "^(([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\.){3}([01]?\\d\\d?|2[0-4]\\d|25[0-5])$");
    Logger logger = LoggerFactory.getLogger(IpValidator.class);

    @Override
    public void initialize(Ip constraintAnnotation) {

    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        context.disableDefaultConstraintViolation();
        context
                .buildConstraintViolationWithTemplate(String.format("IP [%s] NOT VALID", value))
                .addConstraintViolation();
        if (value == null)
            return true;
        boolean res = IP_PATTERN.matcher(value).matches();
        if (!res)
            logger.warn("ip not valid: [{}]", value);
        return IP_PATTERN.matcher(value).matches();
    }
}
