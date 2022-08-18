package io.demo.sharding.util;

import cn.hutool.core.date.DateUtil;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.Date;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ShardingUtil {

    /**
     * EndPoint 转换 Date
     *
     * @param endpoint sharding 范围查询端点
     * @param <T>      有可能是 Date 类型，有可能是 String 类型
     * @return 都转成 Date 类型
     */
    public static <T> Date convertDate(T endpoint) {
        if (endpoint instanceof Date) {
            return (Date) endpoint;
        }
        return DateUtil.parse(endpoint.toString());
    }

}
