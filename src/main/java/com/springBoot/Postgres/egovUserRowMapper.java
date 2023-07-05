package com.springBoot.Postgres;

import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

public class egovUserRowMapper implements RowMapper<egovUser> {

    @Override
    public egovUser mapRow(ResultSet rs, int rowNum) throws SQLException {
        UUID id = (UUID) rs.getObject("id");
        String name = rs.getString("name");
        String gender = rs.getString("gender");
        String mobileNumber = rs.getString("mobile_number");
        String address = rs.getString("address");
        boolean active = rs.getBoolean("active");
        Long createdTime = rs.getLong("created_time");

        return new egovUser(id, name, gender, mobileNumber, address, active, createdTime);
    }
}


