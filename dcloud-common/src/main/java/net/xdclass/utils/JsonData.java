package net.xdclass.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import lombok.*;
import net.xdclass.enums.BizCodeEnum;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.Serializable;
// todo:统一响应前端数据;
/**
 * @Author: MrWang
 * @Contact: 1850195602@qq.com
 * @Date: 2023/5/1 0001 18:05
 * @Version: 1.0
 * @Description: 接口统一协议 JsonData工具类开发
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class JsonData implements Serializable {
   private int code;
   private Object data;
    private String msg;



    /**
     *  获取远程调用数据
     *  注意事项：
     *      支持多单词下划线专驼峰（序列化和反序列化）
     *
     *
     *注： @param typeReference Jackson ObjectMapper的readValue可以将Json字符串反序列化为Java对象
     * @param <T>
     * @return
     */
//    将Object类型转为String,在转对象;
    public  <T> T  getData(TypeReference<T> typeReference){
        return JSON.parseObject(JSON.toJSONString(data),typeReference);
    }
    /**
     * 成功，不传入数据
     * @return
     */
    public static JsonData buildSuccess() {
        return new JsonData(0, null, null);
    }

    /**
     *  成功，传入数据
     * @param data
     * @return
     */
    public static JsonData buildSuccess(Object data) {
        return new JsonData(0, data, null);
    }

    /**
     * 失败，传入描述信息
     * @param msg
     * @return
     */
    public static JsonData buildError(String msg) {
        return new JsonData(-1, null, msg);
    }

    /**
     * 自定义状态码和错误信息
     * @param code
     * @param msg
     * @return
     */
    public static JsonData buildCodeAndMsg(int code, String msg) {
        return new JsonData(code, null, msg);
    }

    /**
     * 自定义文件上传状态码和错误信息
     * @param code
     * @param msg
     * @return
     */
    public static JsonData buildFileCodeAndMsg(int code,Object data, String msg) {
        return new JsonData(code, null, msg);
    }

    /**
     * 传入枚举，返回信息
     * @param codeEnum
     * @return
     */
    public static JsonData buildResult(BizCodeEnum codeEnum){
        return JsonData.buildCodeAndMsg(codeEnum.getCode(),codeEnum.getMessage());
    }
}