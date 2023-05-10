package net.xdclass.controller;


import io.swagger.annotations.ApiOperation;
import net.xdclass.enums.BizCodeEnum;
import net.xdclass.oss.OssCallbackResult;
import net.xdclass.oss.OssPolicyResult;
import net.xdclass.service.FileService;
import net.xdclass.utils.JsonData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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
    @Autowired
    private FileService fileService;
    //文件上传,文件上传默认最大上传为1M,超过就会报错;
    /**
     *
     * @param multipartFile
     * @return上传文件
     */
    @PostMapping("upload")
    public JsonData uploadImg(@RequestPart("multipartFile")MultipartFile multipartFile ){
        String result = fileService.uploadImg(multipartFile);
        return result!=null?JsonData.buildSuccess():JsonData.buildResult(BizCodeEnum.FILE_UPLOAD_USER_IMG_FAIL);
    }
   // @ApiOperation(value = "oss上传签名生成")
    @RequestMapping(value = "/policy", method = RequestMethod.GET)
    public JsonData  policy(HttpServletRequest request) {
        HttpServletRequest result = fileService.policy(request);
        return JsonData.buildSuccess(result);
    }
    @ApiOperation(value = "oss上传成功回调")
    @RequestMapping(value = "callback", method = RequestMethod.POST)
    public JsonData callback(HttpServletRequest request) {
        OssCallbackResult ossCallbackResult = fileService.callback(request);
        return JsonData.buildSuccess(ossCallbackResult);

    }
    //首先，想要获取Cookie信息，那么就得先有Cookie信息，这边我们自己从头开始，先弄个Cookie吧。

    @RequestMapping(value = "/setCookies",method = RequestMethod.GET)
    public  String setCookies(HttpServletResponse response){
        //HttpServerletRequest 装请求信息类
        //HttpServerletRespionse 装相应信息的类
        Cookie cookie=new Cookie("sessionId","FileServiceImpl");
        response.addCookie(cookie);
        return "添加cookies信息成功";
    }
}

