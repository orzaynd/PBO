package dev.oryzaa.projekpbo.web.dao;

import dev.oryzaa.projekpbo.web.config.DBConnection;
import dev.oryzaa.projekpbo.web.model.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserDao {

    public User findByUsername(String username) throws SQLException {
        String sql = "SELECT id_user, username, password, nama_lengkap, role FROM users WHERE username = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, username);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    User u = new User();
                    u.setId(rs.getInt("id_user"));
                    u.setUsername(rs.getString("username"));
                    u.setPassword(rs.getString("password"));
                    u.setNamaLengkap(rs.getString("nama_lengkap"));
                    u.setRole(rs.getString("role"));
                    return u;
                }
            }
        }
        return null;
    }
}
