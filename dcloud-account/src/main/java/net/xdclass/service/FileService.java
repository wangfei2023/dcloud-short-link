package net.xdclass.service;


import net.xdclass.utils.JsonData;
import org.springframework.web.multipart.MultipartFile;



/**
 * @Author: MrWang
 * @Contact: 1850195602@qq.com
 * @Date: 2023/5/6 0006 22:53
 * @Version: 1.0
 * @Description:
 */

public interface FileService {
    /**
     * 测试上传文件
     */
    JsonData uploadImg(MultipartFile file);


}