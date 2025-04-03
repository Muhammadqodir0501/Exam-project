package uz.pdp.demo9.config;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import lombok.Getter;

public class DbConfig {
    @Getter
    private static final HikariDataSource dataSource;
    static {
        try {
            Class.forName("org.postgresql.Driver");
            HikariConfig config = new HikariConfig();
            config.setJdbcUrl("jdbc:postgresql://localhost:5432/postgres");
            config.setUsername("postgres");
            config.setPassword("0501");
            config.setMinimumIdle(5);
            config.setMaximumPoolSize(10);
            config.setConnectionTimeout(30000);
            dataSource = new HikariDataSource(config);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Database connection initialization failed", e);
        }
    }

}
