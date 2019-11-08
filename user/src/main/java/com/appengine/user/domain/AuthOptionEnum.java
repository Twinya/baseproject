package com.appengine.user.domain;

import com.appengine.user.service.SpringConverterableEnum;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum AuthOptionEnum implements SpringConverterableEnum {
    WHITE("ipwhite"),
    BLACK("ipblack"),
    SMSBLACK("smsblack");

    private String value;

    AuthOptionEnum(String value) {
        this.value = value;
    }

    public String getValue(){
        return value;
    }

}
