package com.appengine.user.domain.form;

import com.appengine.common.entity.form.BaseForm;
import com.appengine.user.domain.Address;
import com.appengine.user.domain.po.User;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@EqualsAndHashCode(callSuper = true)
@ApiModel
@Data
public class LoginForm extends BaseForm<User> {
    @NotBlank
    @ApiModelProperty(value = "账号", example = "18888888888")
    private String username;
    @Size(min = 6, max = 32)
    @ApiModelProperty(value = "密码", example = "123456")
    private String password = "";
    @ApiModelProperty(value = "验证码", example = "1234")
    private String code = "";
    @ApiModelProperty(value = "requestId", example = "1231")
    private String requestId = "";
    @NotBlank
    @ApiModelProperty(value = "渠道")
    private String channel = "";
    @NotBlank
    @ApiModelProperty(value = "registFrom")
    private String registFrom = "local";
    @ApiModelProperty(value = "手机系统")
    private String phone_os = "";
    @ApiModelProperty(value = "app")
    private String app = "";
    @ApiModelProperty(value = "brand")
    private String brand = "";
    @Valid
    @ApiModelProperty(value = "地址")
    private Address address;

}
