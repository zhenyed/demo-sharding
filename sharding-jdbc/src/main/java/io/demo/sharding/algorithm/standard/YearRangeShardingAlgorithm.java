package io.demo.sharding.algorithm.standard;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ReUtil;
import com.google.common.collect.Lists;
import com.google.common.collect.Range;
import io.demo.sharding.util.ShardingUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.shardingsphere.api.sharding.standard.RangeShardingAlgorithm;
import org.apache.shardingsphere.api.sharding.standard.RangeShardingValue;

import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.List;

/**
 * 范围分片算法：按年分表
 */
@Slf4j
public class YearRangeShardingAlgorithm<T extends Comparable<?>> implements RangeShardingAlgorithm<T> {

    @Override
    public Collection<String> doSharding(Collection<String> availableTargetNames, RangeShardingValue<T> shardingValues) {
        List<String> result = Lists.newLinkedList(availableTargetNames);
        String firstTableName = result.get(0);

        // 处理范围条件
        Range<T> range = shardingValues.getValueRange();
        if (range != null) {
            // 移除小于最小值
            if (range.hasLowerBound()) {
                Date lowerEndPoint = ShardingUtil.convertDate(range.lowerEndpoint());
                result.removeIf(i -> Integer.parseInt(ReUtil.getGroup0("_[0-9]{4}", i).substring(1, 5)) < DateUtil.year(lowerEndPoint));
            }
            // 移除大于最大值
            if (range.hasUpperBound()) {
                Date upperEndpoint = ShardingUtil.convertDate(range.upperEndpoint());
                result.removeIf(i -> Integer.parseInt(ReUtil.getGroup0("_[0-9]{4}", i).substring(1, 5)) > DateUtil.year(upperEndpoint));
            }
        }

        // 避免当所有表都不符合条件而出现报错，随便返回一个表
        if (CollectionUtils.isEmpty(result)) {
            return Collections.singletonList(firstTableName);
        }
        return result;
    }
}