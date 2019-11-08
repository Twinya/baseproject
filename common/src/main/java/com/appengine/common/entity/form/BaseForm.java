package com.appengine.common.entity.form;

import com.appengine.common.entity.po.BasePo;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;

@Slf4j
@Data
public class BaseForm<T extends BasePo> {

    /**
     * From转化为Po，进行后续业务处理
     *
     * @param clazz
     * @return
     */
    public T toPo(Class<T> clazz) {
        T t = null;
        try {
            t = clazz.newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            log.error("Po NewInstance Error");
        }
        BeanUtils.copyProperties(this, t);
        return t;
    }


}
