package com.pengyou.enums;

/**
 * Created by Administrator on 2018/9/25.
 */
public enum WorkBookVersion {

    WorkBook2003Xls("xls","xls后缀名结尾-2003版本workBook"),
    WorkBook2007Xlsx("xlsx","xlsx后缀名结尾-2007版本workBook");

    private String code;
    private String name;

    WorkBookVersion(String code, String name) {
        this.code = code;
        this.name = name;
    }

    public String getCode() {
        return code;
    }
    public void setCode(String code) {
        this.code = code;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public static WorkBookVersion valueOfSuffix(String suffix) {
        if (WorkBook2003Xls.getCode().equalsIgnoreCase(suffix)){
            return WorkBook2003Xls;
        }else if (WorkBook2007Xlsx.getCode().equalsIgnoreCase(suffix)) {
            return WorkBook2007Xlsx;
        }else{
            return null;
        }
    }
}
