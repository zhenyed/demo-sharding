package io.demo.sharding;

import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;

/**
 * 分片算法：=、>=、<=、>、<、BETWEEN、IN
 */
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@SpringBootTest
class SelectYearTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Test
    @Order(1)
    @DisplayName("yyyy-MM-dd HH:mm:ss")
    void testDate() {
        String sql = "select * from demo_year where create_time = '2022-01-01 12:34:56'";
        jdbcTemplate.queryForList(sql);
    }

    @Test
    @Order(2)
    @DisplayName("yyyy-MM-dd")
    void testDateTime() {
        String sql = "select * from demo_year where create_time = '2022-12-13'";
        jdbcTemplate.queryForList(sql);
    }

    @Test
    @Order(3)
    @DisplayName("大于")
    void testMoreThan() {
        String sql = "select * from demo_year where create_time > '2022-12-13'";
        jdbcTemplate.queryForList(sql);
    }

    @Test
    @Order(4)
    @DisplayName("小于")
    void testLessThan() {
        String sql = "select * from demo_year where create_time < '2022-12-13'";
        jdbcTemplate.queryForList(sql);
    }

    @Test
    @Order(5)
    @DisplayName("大于小于")
    void testMoreThanAndLessThan() {
        String sql = "select * from demo_year where create_time > '2021-01-01' and create_time < '2022-12-13'";
        jdbcTemplate.queryForList(sql);
    }

    @Test
    @Order(6)
    @DisplayName("between")
    void testBetween() {
        String sql = "select * from demo_year where create_time between '2021-12-13' and '2022-11-11'";
        jdbcTemplate.queryForList(sql);
    }

}
