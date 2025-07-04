package com.example.testjava17.config;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;


@Component
public class ConnectDb {
    @Autowired
    @Qualifier("walletDataSource")
    private DataSource walletDataSource;

    @Autowired
    @Qualifier("fynaDataSource")
    private DataSource fynaDataSource;

    @PostConstruct
    public void connectDB() {
        System.out.println("start connection");
        try (Connection conn = walletDataSource.getConnection()) {
            System.out.println("Kết nối wallet thành công!");
            conn.setAutoCommit(false);
        } catch (SQLException e) {
            System.err.println("Lỗi kết nối Oracle: " + e.getMessage());
        }

        try (Connection conn = fynaDataSource.getConnection()) {
            System.out.println("Kết nối fyna thành công!");
            conn.setAutoCommit(true);

        } catch (SQLException e) {
            System.err.println("Lỗi kết nối Oracle: " + e.getMessage());
        }
        System.out.println("end connection");
    }
}
