package com.company.db;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

import java.util.HashMap;
import java.util.Map;

public class EntityManagerFactoryProvider {

    private static HikariDataSource ds;
    static EntityManagerFactory emf = null;

    public static EntityManagerFactory getEMF() {
        if (emf == null) {
            HikariConfig config = new HikariConfig("db.properties");

            String jdbcUrl = (String) config.getDataSourceProperties().get("jdbcUrl");

            config.setJdbcUrl(jdbcUrl);

            config.addDataSourceProperty("cachePrepStmts", "true");
            config.addDataSourceProperty("prepStmtCacheSize", "250");
            config.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");
            ds = new HikariDataSource(config);

            Map<String, Object> properties = new HashMap();
            properties.put("datasource", ds);
            emf = Persistence.createEntityManagerFactory("JPA07", properties);
        }
        return emf;
    }
}
