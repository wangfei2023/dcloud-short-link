package net.xdclass.service.impl;

import lombok.extern.slf4j.Slf4j;
import net.xdclass.enums.AuthTypeEnum;
import net.xdclass.enums.BizCodeEnum;
import net.xdclass.enums.SendCodeEnum;
import net.xdclass.manager.AccountManager;
import net.xdclass.mapper.AccountMapper;
import net.xdclass.model.AccountDO;
import net.xdclass.model.LoginUser;
import net.xdclass.request.AccountLoginRequest;
import net.xdclass.request.AccountRegisterRequest;
import net.xdclass.service.AccountService;
import net.xdclass.service.NotifyService;
import net.xdclass.utils.CommonUtil;
import net.xdclass.utils.JWTUtil;
import net.xdclass.utils.JsonData;
import org.apache.commons.codec.digest.Md5Crypt;
import org.apache.commons.lang3.ObjectUtils;
import org.bouncycastle.jcajce.provider.digest.MD5;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import static javax.swing.text.html.parser.DTDConstants.MD;

/**
 * @Author: MrWang
 * @Contact: 1850195602@qq.com
 * @Date: 2023/5/2 0002 0:09
 * @Version: 1.0
 * @Description:
 */
@Service
@Slf4j
public class AccountServiceImpl implements AccountService {
 @Autowired
 private NotifyService notifyService;
 @Autowired
 private AccountManager accountManager;
    @Override
    public JsonData register(AccountRegisterRequest accountRegisterRequest) {

         //todo:手机验证码验证
       boolean checkCode=false;
       if (!ObjectUtils.isEmpty(accountRegisterRequest.getPhone())){
            checkCode = notifyService.CheckCode(SendCodeEnum.USER_REGISTER, accountRegisterRequest.getPhone(), accountRegisterRequest.getCode());
       }
       if (!checkCode){
           return JsonData.buildResult(BizCodeEnum.CODE_ERROR);
       }
        //todo: 密码加密（TODO）
        AccountDO accountDO = new AccountDO();
        BeanUtils.copyProperties(accountRegisterRequest,accountDO);
        //用户认证权限;
        accountDO.setAuth(AuthTypeEnum.DEFALULT.name());
        //生成唯一性账号;模拟;
        accountDO.setAccountNo(CommonUtil.getCurrentTimestamp());
        //设置盐
        accountDO.setSecret("$1$"+CommonUtil.getStringNumRandom(8));
        //设置密钥(盐+密码)
        String crypt = Md5Crypt.md5Crypt( accountDO.getPwd().getBytes(),accountDO.getSecret());
        accountDO.setPwd(crypt);
        //todo: 账号唯一性检查(TODO),保证手机号唯一,在键表的时候建立唯一性索引;

        //todo: 插入数据库
        int rows = accountManager.insert(accountDO);
        log.info("注册用户={}",rows);
        //todo: 新注册用户福利发放(TODO)
        userRegisterInitTask(accountDO);
        return JsonData.buildSuccess();
    }
//用户登录,并生成对应的秘钥;
    @Override
    public JsonData login(AccountLoginRequest accountLoginRequest) {
        List<AccountDO> accountDOList = accountManager.findByPhone(accountLoginRequest.getPhone());
        if (!ObjectUtils.isEmpty(accountDOList) && accountDOList.size()==1){
            AccountDO accountDO = accountDOList.get(0);
            String secret = accountDO.getSecret();
            String md5Crypt = Md5Crypt.md5Crypt(accountLoginRequest.getPwd().getBytes(), secret);
            if (md5Crypt.equals(accountDO.getPwd())){
                //生成对应的token;
                LoginUser loginUser = LoginUser.builder().build();
                BeanUtils.copyProperties(accountDO,loginUser);
                String token = JWTUtil.genJsonWebToken(loginUser);
                return JsonData.buildSuccess(token);
            }else {
                return JsonData.buildResult(BizCodeEnum.ACCOUNT_PWD_ERROR);
            }

        }else {
            return JsonData.buildResult(BizCodeEnum.ACCOUNT_UNREGISTER);
        }

    }
    //注册成功的用户发放福利;
    private void userRegisterInitTask(AccountDO accountDO){

    }
    /*
    测试盐—+秘钥(密码进行加密测试)
     */
    public static void main(String[] args) {
        //todo:前端发送请求
        AccountRegisterRequest request = new AccountRegisterRequest();
        request.setPwd("wf896901");
        AccountDO accountDO = new AccountDO();
        BeanUtils.copyProperties(request,accountDO);
        accountDO.setSecret("$1$"+CommonUtil.getStringNumRandom(8));
        //todo:给用户注册密码加密;
        String cryptPwd = Md5Crypt.md5Crypt(accountDO.getPwd().getBytes(), accountDO.getSecret());
        accountDO.setPwd(cryptPwd);
        System.out.println(accountDO.getPwd());
    }
}