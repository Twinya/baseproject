package com.appengine.oss;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.model.PutObjectResult;
import com.appengine.domain.OssConfig;
import com.appengine.tinypng.TinypngService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

/**
 * @Author fuyou
 * @Date 2018/12/21 13:33
 */
@Service
public class UploadService {
    @Autowired
    private OssConfig config;

//    public static void main(String[] args) throws Exception {
//        File file = new File("D:/banner2.png");
//        upload(file,"image/","bj2testsss.png");
//    }

    private OSS getOssClient() {
        return new OSSClientBuilder().build(config.getEndpoint(), config.getAccessKeyId(), config.getAccessKeySecret());
    }

    public PutObjectResult upload(MultipartFile multipartFile, String path, String name, boolean compress) throws IOException {
        PutObjectResult result;
        InputStream inputStream;
        if (multipartFile == null) {
            return null;
        }
        if (compress) {
            inputStream = new ByteArrayInputStream(TinypngService.compress(multipartFile.getBytes()));
        } else {
            inputStream = multipartFile.getInputStream();
        }
        OSS ossClient = getOssClient();
        result = ossClient.putObject(config.getBucketName(), path + name, inputStream);
        ossClient.shutdown();
        return result;
    }

    public PutObjectResult uploadIpa(MultipartFile multipartFile, String name) throws IOException {
        PutObjectResult result;
        InputStream inputStream;
        if (multipartFile == null) {
            return null;
        }
        inputStream = multipartFile.getInputStream();
        OSS ossClient = getOssClient();
        result = ossClient.putObject("jianlingtech", "app/" + name + ".ipa", inputStream);
        ossClient.shutdown();
        return result;
    }

    public PutObjectResult upload(File file, String path, String name) {
        PutObjectResult result;
        if (checkFile(file)) return null;
        OSS ossClient = getOssClient();
        result = ossClient.putObject(config.getBucketName(), path + name, file);
        ossClient.shutdown();
        return result;
    }

    private static boolean checkFile(File file) {
        if (file == null) {
            return true;
        }
        return false;
    }
}
