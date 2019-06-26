package com.pengyou.request;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class EmployeeRequest {

    private Integer id;

    @NotBlank
    private String userName;

    private String password;

    @NotBlank
    private String posName;

    private Integer age;

    private String mobile;

    private String profile;
}
