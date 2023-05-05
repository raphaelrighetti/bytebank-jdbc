package br.com.alura.bytebank;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import java.sql.Connection;
import java.sql.SQLException;

public class ConnectionFactory {

    public Connection getConnection() throws SQLException {
        return createDataSource().getConnection();
    }

    private HikariDataSource createDataSource() {
        HikariConfig config = new HikariConfig();

        config.setJdbcUrl("jdbc:mysql://localhost:3306/bytebank");
        config.setUsername("root");
        config.setPassword("abc@1234");
        config.setMaximumPoolSize(10);

        return new HikariDataSource(config);
    }
}
