package net.xdclass.utils;

import org.apache.shardingsphere.core.strategy.keygen.SnowflakeShardingKeyGenerator;

/**
 * @author : [Administrator]
 * @version : [v1.0]
 * @description : 利用Sharding-Jdbc封装id生成器
 * @createTime : [2023/5/22 0022 22:23]
 * @updateUser : [Administrator]
 * @updateTime : [2023/5/22 0022 22:23]
 * @updateRemark : [说明本次修改内容]
 */

public class IDUtil {
    private static SnowflakeShardingKeyGenerator shardingKeyGenerator = new SnowflakeShardingKeyGenerator();

    /**
     * 雪花算法生成器,配置workId，避免重复
     * 解决问题：（1）如果服务器的时钟回拨，就会导致生成的 id 重复
     * （2）代码里面做了系统时间同步
     *
     * 10进制 654334919987691526
     * 64位 0000100100010100101010100010010010010110000000000000000000000110
     *
     * {@link SnowFlakeWordIdConfig}
     *
     * @return
     */
    public static Comparable<?> geneSnowFlakeID(){
        return shardingKeyGenerator.generateKey();
    }
}
