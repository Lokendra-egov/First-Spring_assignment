package com.springBoot.Postgres;


import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;

@Repository
public class UserRepository {
    private JdbcTemplate jdbcTemplate;

    public UserRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @PostConstruct
    public void createTable() {
        jdbcTemplate.execute(
                "CREATE TABLE IF NOT EXISTS egov_user (id SERIAL, name VARCHAR(255), gender VARCHAR(255), mobile_number VARCHAR(255), address VARCHAR(255), active BOOLEAN, PRIMARY KEY (id, active)) PARTITION BY LIST (active);");
        jdbcTemplate.execute("CREATE TABLE IF NOT EXISTS egov_active_user PARTITION OF egov_user FOR VALUES IN (TRUE);");
        jdbcTemplate.execute("CREATE TABLE IF NOT EXISTS egov_inactive_user PARTITION OF egov_user FOR VALUES IN (FALSE);");
    }


    public void create(egovUser egovUser) {
        String sql = "INSERT INTO egov_user (id, name, gender, mobile_number, address, active) " +
                "VALUES (?, ?, ?, ?, ?, ?)";
        jdbcTemplate.update(sql, egovUser.getId(), egovUser.getName(), egovUser.getGender(),
                egovUser.getMobileNumber(), egovUser.getAddress(), egovUser.isActive());
    }

    public List<egovUser> search(UserSearchCriteria criteria) {
        // Select the schema based on the active status of the user

        // Build the SQL query based on the search criteria
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

        return jdbcTemplate.query(sql, paramsArray, new BeanPropertyRowMapper<>(egovUser.class));
    }

    public void update(egovUser egovUser) {
        String sql = "UPDATE egov_user SET name = ?, gender = ?, mobile_number = ?, address = ?, active = ? WHERE id = ?";
        jdbcTemplate.update(sql, egovUser.getName(), egovUser.getGender(), egovUser.getMobileNumber(),
                egovUser.getAddress(), egovUser.isActive(), egovUser.getId());
    }

    public void delete(egovUser egovUser) {
        String sql = "DELETE FROM egov_user WHERE id = ?";
        jdbcTemplate.update(sql, egovUser.getId());
    }


}
