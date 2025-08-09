package org.example.testifyproject.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/db")
public class DatabaseTestController {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @GetMapping("/test")
    public String testConnection() {
        try {
            String result = jdbcTemplate.queryForObject("SELECT version()", String.class);
            return "Kết nối thành công: " + result;
        } catch (Exception e) {
            return "Kết nối thất bại: " + e.getMessage();
        }
    }
}
