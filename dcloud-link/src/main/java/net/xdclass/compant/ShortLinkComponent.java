package net.xdclass.compant;

import net.xdclass.startegy.ShardingDBConfig;
import net.xdclass.startegy.ShardingTableConfig;
import net.xdclass.utils.CommonUtil;
import org.springframework.stereotype.Component;

/**
 * @author : [Administrator]
 * @version : [v1.0]
 * @description :  短链生成组件ShortLinkComponent封装
 * @createTime : [2023/5/23 0023 21:23]
 * @updateUser : [Administrator]
 * @updateTime : [2023/5/23 0023 21:23]
 * @updateRemark : [说明本次修改内容]
 */
@Component
public class ShortLinkComponent {
    /*
    生成短链码;一个长连接可对应抖音、朋友圈、快手的各个短链
     */
    //todo:62个字符;
    private static final String CHARS = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
    public String createShortLinkCode(String param){

        long hash32 = CommonUtil.murmurHash32(param);
        String encode = encodeToBase62(hash32);
       //生成对于的短链码;
        String shortLinkCode=ShardingDBConfig.getRandomDBPrefix(encode)+encode+ ShardingTableConfig.getRandomTablePrefix(encode);

        return shortLinkCode;
    }
    //todo:十进制转62进制;
    private String  encodeToBase62(long num){
        //todo:StringBuuffer线程安全,stringBuild线程不安全;
        StringBuffer sb = new StringBuffer();
        do {
            int i = (int)(num%62);
            sb.append(CHARS.charAt(i));
            num=num/62;
        }while (num>0);
        String value = sb.reverse().toString();
        return value;
    }
}
