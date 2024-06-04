package com.cskaoyan.mall.product.controller;

import com.cskaoyan.mall.common.result.Result;
import io.minio.BucketExistsArgs;
import io.minio.MakeBucketArgs;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import io.minio.errors.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.UUID;

@RestController
@RequestMapping("admin/product")
public class FileUploadController {
    //获取文件上传的对应地址
    @Value("${minio.endpointUrl}")
    public String endpointUrl;

    @Value("${minio.accessKey}")
    public String accessKey;

    @Value("${minio.secretKey}")
    public String secretKey;

    @Value("${minio.bucketName}")
    public String bucketName;

    //文件上传器
    @PostMapping("fileUpload")
    public Result fileUpload(MultipartFile file) throws ServerException, InsufficientDataException, ErrorResponseException, IOException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException {
        //准备获取上传的文件路径
        String url = "";

        //使用minio服务的url,端口,access key和secret key创建一个minioClient对象。
        MinioClient minioClient = MinioClient.builder().endpoint(endpointUrl).credentials(accessKey,secretKey).build();

        //检查桶是否已经存在
        boolean exists = minioClient.bucketExists(BucketExistsArgs.builder().bucket(bucketName).build());
        if (exists){
            System.out.println("bucket already exists");
        }else {
            //创建一个同名桶
            minioClient.makeBucket(MakeBucketArgs.builder().bucket(bucketName).build());
        }

        //定义文件的名称--确保文件名不会重复
        String fileName = System.currentTimeMillis()+ UUID.randomUUID().toString();
        //使用putobject上传一个文件到桶中
        InputStream inputStream = file.getInputStream();
        minioClient.putObject(
                PutObjectArgs.builder().bucket(bucketName).object(fileName).stream(inputStream,file.getSize(),-1).contentType(file.getContentType()).build()
        );
        inputStream.close();
        url=endpointUrl+"/"+bucketName+"/"+fileName;
        System.out.println("url:\t"+url);
        return Result.ok(url);
    }
}
