package net.xdclass.config;

import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author : [Administrator]
 * @version : [v1.0]
 * @description : 分页配置
 * @createTime : [2023/5/29 0029 23:38]
 * @updateUser : [Administrator]
 * @updateTime : [2023/5/29 0029 23:38]
 * @updateRemark : [说明本次修改内容]
 */
@Configuration
public class MybatisPlusPageConfig {
    @Bean
    public MybatisPlusInterceptor mybatisPlusInterceptor(){
        MybatisPlusInterceptor mybatisPlusInterceptor = new MybatisPlusInterceptor();
        mybatisPlusInterceptor.addInnerInterceptor(new PaginationInnerInterceptor());
        return mybatisPlusInterceptor;
    }
}
