package com.pengyou.annotation;



import com.pengyou.validate.SexValidate;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = {SexValidate.class})
@Target({ElementType.FIELD,ElementType.ANNOTATION_TYPE,ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface SexAnnotation {
    String message() default "性别效验取值为:1=男;2=女";
    Class<?>[] groups() default {};
    Class<? extends Payload> [] payload() default {};
}
