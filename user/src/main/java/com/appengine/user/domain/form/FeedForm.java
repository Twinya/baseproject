package com.appengine.user.domain.form;

import com.appengine.common.entity.form.BaseForm;
import com.appengine.user.domain.po.FeedBackRecord;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotBlank;

@EqualsAndHashCode(callSuper = true)
@ApiModel
@Data
public class FeedForm extends BaseForm<FeedBackRecord> {
    @NotBlank(message = "反馈内容不能为空")
    @ApiModelProperty(value = "反馈内容")
    private String content;
    @NotBlank
    @ApiModelProperty(value = "手机品牌")
    private String brand;
    @NotBlank
    @ApiModelProperty(value = "手机系统")
    private String os;
    @NotBlank
    @ApiModelProperty(value = "渠道")
    private String channel;
    @NotBlank
    @ApiModelProperty(value = "app版本")
    private String version;
}
