package com.appengine.user.domain.form;

import com.appengine.common.entity.form.BaseForm;
import com.appengine.sms.domain.SMSrecord;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@EqualsAndHashCode(callSuper = true)
@ApiModel
@Data
public class SMSForm extends BaseForm<SMSrecord> {
    @NotBlank
    @Pattern(regexp = "^[1](([3|5|8|][\\d])|([4][4,5,6,7,8,9])|([6][2,5,6,7])|([7][^9])|([9][1,8,9]))[\\d]{8}$", message = "手机号码格式错误")
    @ApiModelProperty(value = "手机号码", example = "18888888888")
    private String mobile;
    @NotBlank
    @ApiModelProperty(value = "签名", example = "csyy")
    private String sign;
    @ApiModelProperty(value = "from", example = "csyy")
    private String from = "";
    @ApiModelProperty(value = "类型", example = "0")
    private Integer type = 0;
}
