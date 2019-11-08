package com.appengine.pay.domain.form;

import com.appengine.common.entity.form.BaseForm;
import com.appengine.pay.domain.po.AliPayOrder;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@EqualsAndHashCode(callSuper = true)
@ApiModel
@Data
public class UserInfoForm extends BaseForm<AliPayOrder> {
    @NotBlank
    @ApiModelProperty(value = "账号", example = "18888888888")
    private String username;
    @NotBlank
    @Size(min = 6, max = 32)
    @ApiModelProperty(value = "密码", example = "123456")
    private String password;
}
