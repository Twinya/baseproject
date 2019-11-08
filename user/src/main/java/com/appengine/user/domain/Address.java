package com.appengine.user.domain;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.Size;

@Data
@ApiModel
public class Address {
    @Size(min = 2)
    @ApiModelProperty(value = "省")
    private String province = "";
    @Size(min = 2)
    @ApiModelProperty(value = "市")
    private String city = "";
    @Size(min = 2)
    @ApiModelProperty(value = "区")
    private String district = "";
    @ApiModelProperty(value = "街道")
    private String street = "";
    @ApiModelProperty(value = "街道号")
    private String streetNum = "";
}
