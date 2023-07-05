package com.springBoot.Postgres;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;

@Repository
public class UserRepository {
    @Autowired
    private final JdbcTemplate jdbcTemplate;

    public UserRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @PostConstruct
    public void createTable() {
        jdbcTemplate.execute(
                "CREATE TABLE IF NOT EXISTS egov_user (id UUID, name VARCHAR(255), gender VARCHAR(255), mobile_number VARCHAR(255), address JSON, active BOOLEAN, created_time BIGINT, PRIMARY KEY (id, active)) PARTITION BY LIST (active);");
        jdbcTemplate.execute("CREATE TABLE IF NOT EXISTS egov_active_user PARTITION OF egov_user FOR VALUES IN (TRUE);");
        jdbcTemplate.execute("CREATE TABLE IF NOT EXISTS egov_inactive_user PARTITION OF egov_user FOR VALUES IN (FALSE);");
    }
    public void create(egovUser egovUser,String address) {
        Long x = System.currentTimeMillis();
        egovUser.setCreatedTime(x);

        String sql = "INSERT INTO egov_user (id, name, gender, mobile_number, address, active, created_time) " +
                "VALUES (?, ?, ?, ?, ?::json, ?, ?)";
       jdbcTemplate.update(sql,egovUser.getId(), egovUser.getName(), egovUser.getGender(), egovUser.getMobileNumber(),
                address, egovUser.isActive(), egovUser.getCreatedTime());
    }
    public List<egovUser> search(UserSearchCriteria criteria) {
        StringBuilder sqlBuilder = new StringBuilder("SELECT * FROM egov_user WHERE 1=1");
        List<Object> params = new ArrayList<>();

        if (criteria.getId() != null) {
            sqlBuilder.append(" AND id = ?");
            params.add(criteria.getId());
        }

        if (criteria.getMobileNumber() != null) {
            sqlBuilder.append(" AND mobile_number = ?");
            params.add(criteria.getMobileNumber());
        }

        String sql = sqlBuilder.toString();
        Object[] paramsArray = params.toArray();

        return jdbcTemplate.query(sql, paramsArray, new egovUserRowMapper());
    }


    public void update(egovUser egovUser) {
        String sql = "UPDATE egov_user SET name = ?, gender = ?, mobile_number = ?, address = ?, active = ?,created_time = ? WHERE id = ?";
        jdbcTemplate.update(sql, egovUser.getName(), egovUser.getGender(), egovUser.getMobileNumber(),
                egovUser.getAddress(), egovUser.isActive(),egovUser.getCreatedTime(), egovUser.getId());
    }

    public void delete(egovUser egovUser) {
        String sql = "DELETE FROM egov_user WHERE id = ?";
        jdbcTemplate.update(sql, egovUser.getId());
    }


}
