package net.xdclass.controller;

import lombok.extern.slf4j.Slf4j;
import net.xdclass.enums.ShortLinkStateEnum;
import net.xdclass.service.LogService;
import net.xdclass.service.ShortLinkService;
import net.xdclass.utils.CommonUtil;
import net.xdclass.vo.ShortLinkVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author : [Administrator]
 * @version : [v1.0]
 * @description : [一句话描述该类的功能]
 * @createTime : [2023/5/28 0028 22:29]
 * @updateUser : [Administrator]
 * @updateTime : [2023/5/28 0028 22:29]
 * @updateRemark : [说明本次修改内容]
 */
@Controller
@Slf4j
public class LinkApiController {
    @Autowired
    private ShortLinkService shortLinkService;

    @Autowired
    private LogService logService;

    /**
     * 解析 301还是302，这边是返回http code是302
     * <p>
     * 知识点⼀，为什么要⽤ 301 跳转⽽不是 302 呐？
     * <p>
     * 301 是永久᯿定向，302 是临时᯿定向。
     * <p>
     * 短地址⼀经⽣成就不会变化，所以⽤ 301 是同时对服务器压
     ⼒也会有⼀定减少
     * <p>
     * 但是如果使⽤了 301，⽆法统计到短地址被点击的次数。
     * <p>
     * 所以选择302虽然会增加服务器压⼒，但是有很多数据可以获
     取进⾏分析
     *
     * @param linkCode
     * @return
     */
    @GetMapping(path = "/{shortLinkCode}")
    public void dispath(@PathVariable("shortLinkCode") String linkCode,
                        HttpServletRequest request,
                        HttpServletResponse response
    ){
        //获取短链码;
        try {
            //判断短链码是否合规;
            log.info("前端传入短链码={}",linkCode);
            if (isLetterDigit(linkCode)){
                ShortLinkVo shortLinkVo=shortLinkService.parseLinkCode(linkCode);
                if (shortLinkVo!=null){
                    logService.recodeShortLinkLog(request,linkCode,shortLinkVo.getAccountNo());
                }
                //判断是否过期可用;
                if (isVisitable(shortLinkVo)) {
                    String originalUrl = CommonUtil.removeUrlPrefix(shortLinkVo.getOriginalUrl());
                    response.setHeader("Location",originalUrl);
                    //302跳转;
                    response.setStatus(HttpStatus.FOUND.value());

                }else{
                    response.setStatus(HttpStatus.NOT_FOUND.value());
                    return;
                }
            }
        } catch (Exception e) {
            response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
        }
    }
    /**

     *@描述 短链码仅包括数字和字母;

     *@参数

     *@返回值
     *
     *@创建时间  2023/5/28 0028


     */
    private static  boolean isVisitable(ShortLinkVo shortLinkVO){
        if ((shortLinkVO != null &&
                shortLinkVO.getExpired().getTime() >
                        CommonUtil.getCurrentTimestamp())) {
            if
            (ShortLinkStateEnum.ACTIVE.name().equalsIgnoreCase(shortLinkVO.getState())) {
                return true;
            }
        } else if ((shortLinkVO != null &&
                shortLinkVO.getExpired().getTime() == -1)) {
            if
            (ShortLinkStateEnum.ACTIVE.name().equalsIgnoreCase(shortLinkVO.getState())) {
                return true;
            }
        }
        return false;
    }
    private static boolean isLetterDigit(String str){
        String regex="^[a-z0-9A-Z]+$";
        return str.matches(regex);
    }

}
