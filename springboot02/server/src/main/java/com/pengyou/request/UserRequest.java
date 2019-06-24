package com.pengyou.request;

import com.pengyou.annotation.SexAnnotation;
import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class UserRequest {

    /*@NotBlank
    private String name;

    @NotNull
    private Integer sex;

    @NotNull
    @Min(18)
    private Integer age;*/  //v1


    @NotBlank
    private String name;

    @SexAnnotation
    private Integer sex;

    @NotNull
    @Min(18)
    private Integer age;
}
