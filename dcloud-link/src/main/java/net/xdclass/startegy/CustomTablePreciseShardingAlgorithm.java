package net.xdclass.startegy;

import org.apache.shardingsphere.api.sharding.standard.PreciseShardingAlgorithm;
import org.apache.shardingsphere.api.sharding.standard.PreciseShardingValue;

import java.util.Collection;

/**
 * @author : [Administrator]
 * @version : [v1.0]
 * @description : [一句话描述该类的功能]
 * @createTime : [2023/5/28 0028 16:10]
 * @updateUser : [Administrator]
 * @updateTime : [2023/5/28 0028 16:10]
 * @updateRemark : [说明本次修改内容]
 */

public class CustomTablePreciseShardingAlgorithm  implements PreciseShardingAlgorithm<String> {
    /**
     * @param availableTargetNames 数据源集合
     *                             在分库时值为所有分片库的集合 databaseNames
     *                             分表时为对应分片库中所有分片表的集合 tablesNames
     * @param shardingValue        分片属性，包括
     *                             logicTableName 为逻辑表，
     *                             columnName 分片健（字段），
     *                             value 为从 SQL 中解析出的分片健的值
     * @return
     */
    @Override
    public String doSharding(Collection<String> availableTargetNames, PreciseShardingValue<String> shardingValue) {
        //获取逻辑表名
        String targetName = availableTargetNames.iterator().next();

        String value = shardingValue.getValue();
        //短链码最后一位
        String codePrefix = value.substring(value.length()-1);
        //拼装actual table
        return targetName + "_" + codePrefix;

    }
}

