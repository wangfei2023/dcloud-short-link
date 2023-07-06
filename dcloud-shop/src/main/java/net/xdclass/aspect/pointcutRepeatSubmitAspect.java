/**
 * @project dcloud-short-link
 * @description 定义切面防重提交
 * @author Administrator
 * @date 2023/7/5 0005 22:16:03
 * @version 1.0
 */

package net.xdclass.aspect;

import com.alibaba.cloud.commons.lang.StringUtils;
import lombok.extern.slf4j.Slf4j;
import net.xdclass.annoatation.RepeatSubmit;
import net.xdclass.constant.RedisKey;
import net.xdclass.enums.BizCodeEnum;
import net.xdclass.exception.BizException;
import net.xdclass.interceptor.LoginInterceptor;
import net.xdclass.model.LoginUser;
import net.xdclass.utils.CommonUtil;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.redisson.client.RedisClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.concurrent.TimeUnit;

@Aspect
@Component
@Slf4j
public class pointcutRepeatSubmitAspect {
    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Autowired
    private RedissonClient  redissonClient;
    /**
     * 定义 @Pointcut注解表达式，
     *  方式一：@annotation：当执行的方法上拥有指定的注解时生效（我们采用这）
     *  方式二：execution：一般用于指定方法的执行
     *
     * @param repeatSubmit
     */
    @Pointcut("@annotation(repeatSubmit)")
    public void pointcutNoRepeatSubmit(RepeatSubmit repeatSubmit) {

    }

    /**
     * 环绕通知, 围绕着方法执行
     * @Around 可以用来在调用一个具体方法前和调用后来完成一些具体的任务。
     *
     * 方式一：单用 @Around("execution(* net.xdclass.controller.*.*(..))")可以
     * 方式二：用@Pointcut和@Around联合注解也可以（我们采用这个）
     *
     *
     * 两种方式
     * 方式一：加锁 固定时间内不能重复提交
     * <p>
     * 方式二：先请求获取token，这边再删除token,删除成功则是第一次提交
     *
     * @param joinPoint
     * @param noRepeatSubmit
     * @return
     * @throws Throwable
     */
    @Around("pointcutNoRepeatSubmit(noRepeatSubmit)")
    public Object around(ProceedingJoinPoint joinPoint, RepeatSubmit noRepeatSubmit) throws Throwable {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        long accountNo = LoginInterceptor.threadLocal.get().getAccountNo();
        boolean res =false;
        //防重提交类型
        String type = noRepeatSubmit.limitType().name();

        if (type.equals(RepeatSubmit.Type.PARAM.name())) {
            //方式一方法参数            TODO
            //方式一方法参数
            long lockTime = noRepeatSubmit.lockTime();

            String ip = CommonUtil.getIpAddr(request);
            //获取注解
            MethodSignature signature = (MethodSignature) joinPoint.getSignature();
            Method method = signature.getMethod();
            //目标类、方法
            String className = method.getDeclaringClass().getName();
            String name = method.getName();
            String key = "order_server:repeat_submit:"+CommonUtil.MD5(String.format("%s#%s#%s#%s",accountNo,ip, className, name));

            log.info("key={}", key);

            //第一种加锁方式加锁;
            //stringRedisTemplate.opsForValue().setIfAbsent(key,"1",lockTime, TimeUnit.SECONDS);
            RLock lock = redissonClient.getLock(key);
            // 尝试加锁，最多等待0秒，上锁以后5秒自动解锁 [lockTime默认为5s, 可以自定义]
            res=lock.tryLock(0,lockTime,TimeUnit.SECONDS);
        } else {
            //方式二,令牌形式
            String requestToken = request.getHeader("request-token");
            if (StringUtils.isBlank(requestToken)) {
                throw new BizException(BizCodeEnum.ORDER_CONFIRM_TOKEN_EQUAL_FAIL);

            }
            LoginUser loginUser = LoginInterceptor.threadLocal.get();
            //"order:submit:%s:%s"
            String key = String.format(RedisKey.SUBMIT_ORDER_TOKEN_KEY, loginUser.getAccountNo(), requestToken);


            /**
             * 提交表单的token key
             * 方式一：不用lua脚本获取再判断，之前是因为 key组成是 order:submit:accountNo, value是对应的token，所以需要先获取值，再判断
             * 方式二：可以直接key是 order:submit:accountNo:token,然后直接删除成功则完成
             */
            res = stringRedisTemplate.delete(key);
            if (!res){
                log.error("订单重复提交");
                throw new BizException(BizCodeEnum.ORDER_CONFIRM_REPEAT);
            }

        }

        System.out.println("目标方法执行前");
        Object object = joinPoint.proceed();
        System.out.println("目标方法执行后");
        return object;
    }
}


