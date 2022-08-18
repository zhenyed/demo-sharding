package io.demo.sharding.algorithm.standard;

import cn.hutool.core.date.DateUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.shardingsphere.api.sharding.standard.PreciseShardingAlgorithm;
import org.apache.shardingsphere.api.sharding.standard.PreciseShardingValue;

import java.util.Collection;
import java.util.Date;

/**
 * 精确分片算法：按年分表
 */
@Slf4j
public class YearPreciseShardingAlgorithm<T extends Comparable<?>> implements PreciseShardingAlgorithm<T> {

    @Override
    public String doSharding(Collection<String> availableTargetNames, PreciseShardingValue<T> shardingValue) {
        T value = shardingValue.getValue();
        if (value instanceof Date) {
            return shardingValue.getLogicTableName() + "_" + DateUtil.year((Date) value);
        } else {
            return shardingValue.getLogicTableName() + "_" + DateUtil.year(DateUtil.parse(value.toString()));
        }
    }
}