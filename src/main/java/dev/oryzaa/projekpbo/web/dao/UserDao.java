package dev.oryzaa.projekpbo.web.dao;

import dev.oryzaa.projekpbo.web.model.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Data Access Object for User entity.
 */
public class UserDao extends BaseDao {

    /**
     * Find a user by username.
     *
     * @param username the username to search
     * @return the user if found, null otherwise
     * @throws SQLException if database operation fails
     */
    public User findByUsername(String username) throws SQLException {
        String sql = "SELECT id_user, username, password, nama_lengkap, role FROM users WHERE username = ?";
        
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setString(1, username);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapResultSet(rs);
                }
            }
        }
        return null;
    }

    private User mapResultSet(ResultSet rs) throws SQLException {
        return new User(
            rs.getInt("id_user"),
            rs.getString("username"),
            rs.getString("password"),
            rs.getString("nama_lengkap"),
            rs.getString("role")
        );
    }
}
