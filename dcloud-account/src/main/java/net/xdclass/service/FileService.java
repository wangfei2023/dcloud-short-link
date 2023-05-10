package net.xdclass.service;

import net.xdclass.oss.OssCallbackResult;
import net.xdclass.oss.OssPolicyResult;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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
    String uploadImg(MultipartFile file);
    /**
     * oss上传策略生成
     */
    HttpServletRequest policy(HttpServletRequest request);

    /**
     * oss上传成功回调
     */
    OssCallbackResult callback(HttpServletRequest request);

}