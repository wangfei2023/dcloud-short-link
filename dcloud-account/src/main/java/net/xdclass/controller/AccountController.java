package net.xdclass.controller;


import net.xdclass.request.UserLoginRequest;
import net.xdclass.service.FileService;
import net.xdclass.utils.JsonData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.xml.transform.Result;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author MrWang
 * @since 2023-05-01
 */
@RestController
@RequestMapping("/api/v1/account")
public class AccountController {
//    @Autowired
//    private UserService userService;
    @Autowired
    private FileService fileService;
// @PostMapping("login")
//    public JsonData Userlogin(UserLoginRequest userLoginRequest){
//     JsonData jsonData = userService.login(userLoginRequest);
//     return jsonData;
//    }
    @PostMapping("/fileoss")
    public JsonData uploadOssFile(MultipartFile file) {
        //获取上传文件 MultipartFile(图片，文件都可以)
        //返回上传到oss的路径
        String url = fileService.uploadFileAvatar(file);
        return JsonData.buildCodeAndMsg(1,url);
    }
}

