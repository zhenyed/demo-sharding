package io.demo.sharding.algorithm.complex;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ReUtil;
import com.google.common.collect.Lists;
import com.google.common.collect.Range;
import io.demo.sharding.util.ShardingUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.shardingsphere.api.sharding.complex.ComplexKeysShardingAlgorithm;
import org.apache.shardingsphere.api.sharding.complex.ComplexKeysShardingValue;

import java.util.*;

/**
 * 复合分片算法
 */
@Slf4j
public class YearAndUserIdShardingAlgorithm<T extends Comparable<?>> implements ComplexKeysShardingAlgorithm<T> {
    private static final int SHARDING_SIZE = 3;

    @Override
    public Collection<String> doSharding(Collection<String> availableTargetNames, ComplexKeysShardingValue<T> shardingValues) {
        Collection<String> result = Lists.newLinkedList();

        // 精确分片
        Map<String, Collection<T>> columnNameAndShardingValuesMap = shardingValues.getColumnNameAndShardingValuesMap();
        Collection<Integer> userIds = (Collection<Integer>) columnNameAndShardingValuesMap.get("user_id");
        Collection<T> createTimes = columnNameAndShardingValuesMap.get("create_time");
        if (CollectionUtils.isNotEmpty(userIds) && CollectionUtils.isNotEmpty(createTimes)) {
            // 两个分表键都存在
            for (Integer userId : userIds) {
                for (T createTime : createTimes) {
                    String suffix = "_" + DateUtil.year(ShardingUtil.convertDate(createTime)) + "_" + userId % SHARDING_SIZE;
                    for (String s : availableTargetNames) {
                        if (s.endsWith(suffix)) {
                            result.add(s);
                        }
                    }
                }
            }
        } else if (CollectionUtils.isEmpty(userIds) && CollectionUtils.isEmpty(createTimes)) {
            // 全路由：没有分表键
            result.addAll(availableTargetNames);
        } else if (CollectionUtils.isEmpty(createTimes)) {
            // 只有用户 ID 分片
            for (Integer userId : userIds) {
                String suffix = "_" + userId % SHARDING_SIZE;// t_demo_2022_1
                for (String s : availableTargetNames) {
                    if (s.endsWith(suffix)) {
                        result.add(s);
                    }
                }
            }
        } else { // CollectionUtils.isEmpty(userIds)
            // 只有时间分片
            for (T createTime : createTimes) {
                String suffix = "_" + DateUtil.year(ShardingUtil.convertDate(createTime)) + "_";
                for (String s : availableTargetNames) {
                    if (s.contains(suffix)) {
                        result.add(s);
                    }
                }
            }
        }
        
        // 范围分片
        Map<String, Range<T>> columnNameAndRangeValuesMap = shardingValues.getColumnNameAndRangeValuesMap();
        Range<T> range = columnNameAndRangeValuesMap.get("create_time");
        if (range != null) {
            // 移除小于最小值
            if (range.hasLowerBound()) {
                Date lowerEndPoint = ShardingUtil.convertDate(range.lowerEndpoint());
                result.removeIf(i -> Integer.parseInt(ReUtil.getGroup0("_[0-9]{4}_", i).substring(1, 5)) < DateUtil.year(lowerEndPoint));
            }
            // 移除大于最大值
            if (range.hasUpperBound()) {
                Date upperEndpoint = ShardingUtil.convertDate(range.upperEndpoint());
                result.removeIf(i -> Integer.parseInt(ReUtil.getGroup0("_[0-9]{4}_", i).substring(1, 5)) > DateUtil.year(upperEndpoint));
            }
        }
        
        // 避免当所有表都不符合条件而出现报错，随便返回一个表
        if (CollectionUtils.isEmpty(result)) {
            String firstTableName = availableTargetNames.iterator().next();
            return Collections.singletonList(firstTableName);
        }
        return result;
    }


}
