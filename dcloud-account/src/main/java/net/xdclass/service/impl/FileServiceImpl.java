package net.xdclass.service.impl;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;

import com.aliyun.oss.model.PutObjectResult;
import lombok.extern.slf4j.Slf4j;
import net.xdclass.config.OssConfig;

import net.xdclass.service.FileService;
import net.xdclass.utils.CommonUtil;


import net.xdclass.utils.JsonData;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;


import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * @Author: MrWang
 * @Contact: 1850195602@qq.com
 * @Date: 2023/5/6 0006 23:03
 * @Version: 1.0
 * @Description:
 */
@Service
@Slf4j

public class FileServiceImpl implements FileService {

    @Autowired
    private OssConfig OssConfig;


    @Override
    public JsonData uploadImg(MultipartFile file) {

        //获取相关配置
        String bucketname = OssConfig.getBucketname();
        String endpoint = OssConfig.getEndpoint();
        String accessKeyId = OssConfig.getAccessKeyId();
        String accessKeySecret = OssConfig.getAccessKeySecret();
        String objectName = OssConfig.getObjectName();
        //创建OSS对象
        OSS ossClient = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);

        //获取原生文件名  xxx.jpg
        String originalFileName = file.getOriginalFilename();

        //JDK8的日期格式
        LocalDateTime ldt = LocalDateTime.now();
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd");

        //拼装路径,oss上存储的路径  user/2022/12/1/sdfdsafsdfdsf.jpg
        String folder = dtf.format(ldt);
        //todo:此方法是每上传一个文件都会生成一个新的路径拼接;
       // String fileName = CommonUtil.generateUUID();
      //  String extension = originalFileName.substring(originalFileName.lastIndexOf("."));

        // 在OSS上的bucket下创建 2023 这个文件夹
        String newFileName = "2023/"+folder+"/"+ originalFileName;
        //
        try
        {
            // todo：判断文件内容是否存在。。。。。
            boolean flag = ossClient.doesObjectExist(bucketname, newFileName);
            if(!flag){
                PutObjectResult putObjectResult = ossClient.putObject(bucketname,newFileName,file.getInputStream());
                //拼装返回路径
                if(putObjectResult != null){
                    String imgUrl = "https://"+bucketname+"."+endpoint+"/"+newFileName;
                    JsonData.buildCodeAndMsg(1,imgUrl);
                }
            }else{
                return JsonData.buildError("oss上传文件已存在,请勿再上传");
            }
        }
        catch (Exception ex)
        {
           ex.printStackTrace();
        }

        return null;
    }

}