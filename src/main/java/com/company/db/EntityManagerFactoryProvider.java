package com.company.db;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class EntityManagerFactoryProvider {

  private static HikariDataSource ds;
  static EntityManagerFactory emf = null;

  public static EntityManagerFactory getEMF() {
    if (emf == null) {
      HikariConfig config = new HikariConfig("db.properties");

      Properties dataSourceProperties = config.getDataSourceProperties();

      String jdbcUrl = (String) dataSourceProperties.get("jdbcUrl");
      String user = (String) dataSourceProperties.get("user");
      String password = (String) dataSourceProperties.get("password");

      config.setJdbcUrl(jdbcUrl);

      config.addDataSourceProperty("cachePrepStmts", "true");
      config.addDataSourceProperty("prepStmtCacheSize", "250");
      config.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");
      ds = new HikariDataSource(config);

      Map<String, Object> properties = new HashMap();
      properties.put("jakarta.persistence.dataSource", ds);
      properties.put("jakarta.persistence.jdbc.user", user);
      properties.put("jakarta.persistence.jdbc.password", password);
      properties.put("jakarta.persistence.jdbc.url", jdbcUrl);

      properties.put("hibernate.hbm2ddl.auto", "update");
      properties.put("hibernate.dialect", "org.hibernate.dialect.MariaDBDialect");

      emf = Persistence.createEntityManagerFactory("JPA07", properties);
    }
    return emf;
  }
}
