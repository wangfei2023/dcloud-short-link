package net.xdclass.startegy;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * @author : [Administrator]
 * @version : [v1.0]
 * @description : [一句话描述该类的功能]
 * @createTime : [2023/5/28 0028 16:33]
 * @updateUser : [Administrator]
 * @updateTime : [2023/5/28 0028 16:33]
 * @updateRemark : [说明本次修改内容]
 */

public class ShardingTableConfig {
    /**
     * 存储表位置编号
     */
    private static final List<String> tableprefixList=new ArrayList<>();
    private static final Random random=new Random();
    //配置启用那些库的前缀
    static {
        tableprefixList.add("0");
        tableprefixList.add("a");
    }
    public static String getRandomTablePrefix(String code){
        //int nextInt = random.nextInt(tableprefixList.size());
        int hashCode = code.hashCode();
        int index = Math.abs(hashCode)%tableprefixList.size();
        return tableprefixList.get(index);
    }
}
