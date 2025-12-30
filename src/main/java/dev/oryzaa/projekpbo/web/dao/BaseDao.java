package dev.oryzaa.projekpbo.web.dao;

import dev.oryzaa.projekpbo.web.config.DBConnection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * Abstract base class for all DAOs with common database operations.
 */
public abstract class BaseDao {

    protected static final Logger log = LoggerFactory.getLogger(BaseDao.class);

    /**
     * Get a database connection.
     *
     * @return a database connection
     * @throws SQLException if connection fails
     */
    protected Connection getConnection() throws SQLException {
        return DBConnection.getConnection();
    }

    /**
     * Close resources safely.
     *
     * @param resources the resources to close
     */
    protected void closeQuietly(AutoCloseable... resources) {
        for (AutoCloseable resource : resources) {
            if (resource != null) {
                try {
                    resource.close();
                } catch (Exception e) {
                    log.debug("Error closing resource", e);
                }
            }
        }
    }
}
