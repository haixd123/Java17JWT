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
            // Tạo bảng thử
//                String sql = "CREATE TABLE test_users (id NUMBER PRIMARY KEY, name VARCHAR2(100))";
//                try (Statement stmt = conn.createStatement()) {
//                    stmt.executeUpdate(sql);
//                    System.out.println("Tạo bảng test_users thành công!");
//                } catch (SQLException e) {
//                    System.out.println("Bảng có thể đã tồn tại: " + e.getMessage());
//                }
//
//                // Insert dữ liệu
//                String insert = "INSERT INTO test_users (id, name) VALUES (?, ?)";
//                try (PreparedStatement pstmt = conn.prepareStatement(insert)) {
//                    pstmt.setInt(1, 1);
//                    pstmt.setString(2, "Hai Nguyen");
//                    pstmt.executeUpdate();
//                    System.out.println("Thêm dữ liệu thành công!");
//                }
//
//                // Query
//                String query = "SELECT * FROM test_users";
//                try (Statement stmt = conn.createStatement();
//                     ResultSet rs = stmt.executeQuery(query)) {
//                    while (rs.next()) {
//                        System.out.println(rs.getInt("id") + " - " + rs.getString("name"));
//                    }
//                }

        } catch (SQLException e) {
            System.err.println("Lỗi kết nối Oracle: " + e.getMessage());
        }
        System.out.println("end connection");
    }
}
