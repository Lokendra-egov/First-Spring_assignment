package com.springBoot.Postgres;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.jdbc.core.RowMapper;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

public class egovUserRowMapper implements RowMapper<egovUser> {

    public egovUser mapRow(ResultSet rs, int rowNum) throws SQLException {
        UUID id = (UUID) rs.getObject("id");
        String name = rs.getString("name");
        String gender = rs.getString("gender");
        String mobileNumber = rs.getString("mobile_number");
        String addressJson = rs.getString("address");
        boolean active = Boolean.parseBoolean(rs.getString("active"));
        long createdTime = rs.getLong("created_time");

        ObjectMapper objectMapper = new ObjectMapper();
        Address address;
        try {
            address = objectMapper.readValue(addressJson, Address.class);
        } catch (IOException e) {
            throw new SQLException("Failed to deserialize JSON address", e);
        }

        return new egovUser(id, name, gender, mobileNumber, active, createdTime, address);
    }
}
