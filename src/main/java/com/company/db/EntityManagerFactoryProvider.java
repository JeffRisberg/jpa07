package com.company.db;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import jakarta.persistence.EntityManagerFactory;
import java.util.Properties;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.AvailableSettings;
import org.hibernate.cfg.Configuration;

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

      // Configure Hibernate settings
      Properties settings = new Properties();
      //settings.put(AvailableSettings.DRIVER, "org.mariadb.jdbc.Driver");  // don't need this
      settings.put(AvailableSettings.URL, jdbcUrl);
      settings.put(AvailableSettings.USER, user);
      settings.put(AvailableSettings.PASS, password);
      settings.put(AvailableSettings.DIALECT, "org.hibernate.dialect.MariaDBDialect");
      settings.put(AvailableSettings.HBM2DDL_AUTO, "update");  // Auto-create tables
      settings.put(AvailableSettings.SHOW_SQL, "true");

      // Create hibernate configuration
      Configuration configuration = new Configuration();
      configuration.setProperties(settings);

      // Register entity classes automatically
      configuration.addAnnotatedClass(com.company.domain.Charity.class);
      configuration.addAnnotatedClass(com.company.domain.Donation.class);
      configuration.addAnnotatedClass(com.company.domain.Donor.class);

      // generate a list of days of the week
      List<String> days = new ArrayList<String>();
      days.add("Sunday");
      days.add("Monday");
      days.add("Tuesday");
      days.add("Wednesday");

      


      // Build SessionFactory
      SessionFactory sessionFactory = configuration.buildSessionFactory();
      emf = sessionFactory.unwrap(EntityManagerFactory.class);
    }

    return emf;
  }
}
