package io.demo.sharding;

import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@SpringBootTest
class SelectComplexTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Test
    @Order(1)
    @DisplayName("多分表键 and")
    void testAnd() {
        String sql = "select * from demo_order where create_time = '2022-01-01 12:34:56' and user_id = 2";
        jdbcTemplate.queryForList(sql);
    }

    @Test
    @Order(2)
    @DisplayName("多分表键 and2")
    void testAnd2() {
        String sql = "select * from demo_order where create_time > '2022-01-01 12:34:56' and user_id = 2";
        jdbcTemplate.queryForList(sql);
    }

    @Test
    @Order(3)
    @DisplayName("多分表键 or")
    void testOr() {
        String sql = "select * from demo_order where create_time = '2022-01-01 12:34:56' or user_id = 2";
        jdbcTemplate.queryForList(sql);
    }

    @Test
    @Order(4)
    @DisplayName("多分表键 or2")
    void testOr2() {
        String sql = "select * from demo_order where create_time > '2022-01-01 12:34:56' or user_id = 2";
        jdbcTemplate.queryForList(sql);
    }

}
