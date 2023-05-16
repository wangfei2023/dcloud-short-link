package net.xdclass.interceptor;

import com.alibaba.nacos.common.utils.HttpMethod;
import com.alibaba.nacos.common.utils.StringUtils;
import groovy.util.logging.Slf4j;
import io.jsonwebtoken.Claims;
import net.xdclass.enums.BizCodeEnum;
import net.xdclass.model.LoginUser;
import net.xdclass.utils.CommonUtil;
import net.xdclass.utils.JWTUtil;
import net.xdclass.utils.JsonData;
import org.springframework.http.HttpStatus;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
//todo:：账号微服务登录拦截器开发和用户信息传递
@Slf4j
public class LoginInterceptor  implements HandlerInterceptor {
    public static ThreadLocal<LoginUser> threadLocal=new ThreadLocal<>();
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        //请求方法;
        if (HttpMethod.OPTIONS.toString().equalsIgnoreCase(request.getMethod())) {
            response.setStatus(HttpStatus.NO_CONTENT.value());
            return true;
        }
        //获取请求头的方式;
        String accessToken = request.getHeader("token");
        if (StringUtils.isBlank(accessToken)) {
            accessToken = request.getParameter("token");
        }
       //解密token
        if (StringUtils.isNotBlank(accessToken)){
            Claims claims = JWTUtil.checkJWT(accessToken);
            if (claims==null){
                CommonUtil.sendJsonMessage(response,JsonData.buildResult(BizCodeEnum.ACCOUNT_UNLOGIN));
                return false;
            }
            //claims不为空;......链式调用;
            Long accountNo = Long.parseLong(claims.get("account_no").toString());
            String headImg = (String) claims.get("head_img");
            String username = (String) claims.get("username");
            String mail = (String) claims.get("mail");
            String phone = (String) claims.get("phone");
            String auth = (String) claims.get("auth");
            LoginUser loginUser = LoginUser.builder().
                    accountNo(accountNo).
                    auth(auth).
                    headImg(headImg).
                    username(username).
                    mail(mail).
                    phone(phone).build();
            //方式一;
            //request.setAttribute("loginUser",loginUser);
            threadLocal.set(loginUser);
            return true;
        }
        return false;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        //防止内存泄漏;
        threadLocal.remove();
    }
}
