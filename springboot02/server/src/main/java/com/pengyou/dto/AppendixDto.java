package com.pengyou.dto;

import lombok.Data;
import lombok.ToString;

import java.io.Serializable;

/**
 * Created by Administrator on 2018/9/23.
 */
@Data
@ToString
public class AppendixDto implements Serializable{

    private String moduleType;

    private String location;



}