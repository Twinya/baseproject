package com.appengine.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * @Author fuyou
 * @Date 2019/2/15 14:56
 */
@Getter
@Setter
@NoArgsConstructor
@Component
public class OssConfig {
    @Value("${oss.endpoint}")
    private String endpoint;
    @Value("${oss.accessKeyId}")
    private String accessKeyId;
    @Value("${oss.accessKeySecret}")
    private String accessKeySecret;
    @Value("${oss.bucketName}")
    private String bucketName;
}
