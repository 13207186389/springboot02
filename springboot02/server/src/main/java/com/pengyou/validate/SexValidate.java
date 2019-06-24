package com.pengyou.validate;

import com.pengyou.annotation.SexAnnotation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.HashSet;
import java.util.Set;

public class SexValidate implements ConstraintValidator<SexAnnotation,Integer> {

    Set<Integer> sexArr;

    public void initialize(SexAnnotation constraintAnnotation) {
        sexArr=new HashSet<Integer>();
        sexArr.add(1);
        sexArr.add(2);
    }

    public boolean isValid(Integer value, ConstraintValidatorContext context) {
        if(sexArr.contains(value)){
            return true;
        }
        return false;
    }
}
