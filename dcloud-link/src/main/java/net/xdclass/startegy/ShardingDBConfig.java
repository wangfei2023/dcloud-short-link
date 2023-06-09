package net.xdclass.startegy;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * @author : [Administrator]
 * @version : [v1.0]
 * @description : [一句话描述该类的功能]
 * @createTime : [2023/5/28 0028 16:21]
 * @updateUser : [Administrator]
 * @updateTime : [2023/5/28 0028 16:21]
 * @updateRemark : [说明本次修改内容]
 */

public class ShardingDBConfig {

    /**
     * 存储数据库位置编号
     */
    private static final List<String> dbprefixList=new ArrayList<>();
    private static final Random random=new Random();
    //配置启用那些库的前缀
    static {
        dbprefixList.add("0");
        dbprefixList.add("1");
        dbprefixList.add("a");
        //加权的方式；
//        dbprefixList.add("1");
//        dbprefixList.add("1");
//        dbprefixList.add("1");
    }
    public static String getRandomDBPrefix(String code){
        //int nextInt = random.nextInt(dbprefixList.size());
//       生成固定的短链码
        int hashCode = code.hashCode();
       int index= Math.abs(hashCode)%dbprefixList.size();
       return  dbprefixList.get(index);
    }
}
