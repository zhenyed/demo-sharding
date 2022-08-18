package io.demo.sharding;

import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@SpringBootTest
class SelectKeyTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Test
    @Order(1)
    @DisplayName("不含分表键查询")
    void testSelectFullRoute() {
        String sql = "select * from demo_number";
        jdbcTemplate.queryForList(sql);
    }

    @Test
    @Order(2)
    @DisplayName("指定分表键查询")
    void testSelectShardingKey() {
        String sql = "select * from demo_number where id = 123";
        jdbcTemplate.queryForList(sql);
    }

    @Test
    @Order(3)
    @DisplayName("范围查询")
    void testSelectRange() {
        String sql = "select * from demo_number where id in (7,123)";
        jdbcTemplate.queryForList(sql);
    }

    @Test
    @Order(4)
    @DisplayName("分页查询-含分表键")
    void testSelectPageByShardingKey() {
        String sql = "select * from demo_number where id = 123 limit 20,10";
        jdbcTemplate.queryForList(sql);
    }

    @Test
    @Order(5)
    @DisplayName("分页查询-不含分表键")
    void testSelectPageByNoShardingKey() {
        String sql = "select * from demo_number where name = '123' limit 200,10";
        jdbcTemplate.queryForList(sql);
    }

    @Test
    @Order(6)
    @DisplayName("链表查询-已配置绑定表规则")
    void testSelectJoinByShardingKey() {
        String sql = "select * from demo_number a " +
                "left join demo_number_rel b " +
                "on a.id = b.number_id";
        jdbcTemplate.queryForList(sql);
    }

    @Test
    @Order(7)
    @DisplayName("链表查询-未配置绑定表规则")
    void testSelectJoinByNoShardingKey() {
        String sql = "select * from demo_number a " +
                "left join demo_number_rel_tmp b " +
                "on a.id = b.number_id";
        jdbcTemplate.queryForList(sql);
    }

}
