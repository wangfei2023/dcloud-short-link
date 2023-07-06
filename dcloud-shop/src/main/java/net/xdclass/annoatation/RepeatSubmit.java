package net.xdclass.annoatation;
/**
 * @description TODO 
 * 自定义防重提交     PARAM(key为类+方法名)
 * @return
 * @author 
 * @date  
 */
import java.lang.annotation.*;

@Documented
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface RepeatSubmit {
    /**
     * @description TODO
     * 防重提交,支持两种,一个是方法参数,一个是令牌;
     * @return
     * @author
     * @date
     */
    enum Type{PARAM,TOKEN}
   /**
    * @description TODO 
    * 默认防重提交，是方法参数       
    * @return 
    * @author 
    * @date  
    */
    Type limitType()default  Type.PARAM;
   /**
    * @description TODO 
    * 加锁过期时间,默认是5s；
    * @return 
    * @author 
    * @date  
    */
    long lockTime() default 5;
}
