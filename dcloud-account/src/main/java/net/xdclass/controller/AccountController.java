package net.xdclass.controller;


import cn.hutool.json.JSONObject;
import com.alibaba.fastjson.JSON;
import com.google.gson.JsonObject;
import net.xdclass.enums.BizCodeEnum;
import net.xdclass.request.AccountRegisterRequest;
import net.xdclass.service.AccountService;
import net.xdclass.service.FileService;
import net.xdclass.utils.JsonData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;



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
    @Autowired
    private AccountService accountService;
    //文件上传,文件上传默认最大上传为1M,超过就会报错;
    /**
     *
     * @param multipartFile
     * @return上传文件
     */
    @PostMapping("upload")
    public JsonData uploadImg(@RequestPart("multipartFile")MultipartFile multipartFile ){
        JsonData result = fileService.uploadImg(multipartFile);
        //向用户返回信息(url地址)
     // return result!=null?JsonData.buildFileCodeAndMsg(1,multipartFile,"上传文件成功"):JsonData.buildResult(BizCodeEnum.FILE_UPLOAD_USER_IMG_FAIL);
        return result;
      //  return result!=null?JsonData.buildSuccess():JsonData.buildResult(BizCodeEnum.FILE_UPLOAD_USER_IMG_FAIL);
    }
    /**
     *
     * @param accountRegisterRequest
     * @return用户首次注册
     */
    @PostMapping("register")
    public JsonData register(@RequestBody AccountRegisterRequest accountRegisterRequest){
        JsonData jsonData=accountService.register(accountRegisterRequest);
        return jsonData;
    }
}

