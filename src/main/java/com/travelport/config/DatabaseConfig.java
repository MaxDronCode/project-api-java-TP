package com.travelport.config;

import org.apache.commons.dbcp2.BasicDataSource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.orm.hibernate5.HibernateTransactionManager;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.transaction.TransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;
import java.util.Properties;

@Configuration
@EnableTransactionManagement
@PropertySource("classpath:application.properties")
public class DatabaseConfig {

    @Value("${app.db.username}")
    private String username;

    @Value("${app.db.password}")
    private String password;

    @Value("${app.db.driverClassname}")
    private String driverClassName;

    @Value("${app.db.url}")
    private String url;

    @Bean
    public DataSource dataSource() {
        var ds = new BasicDataSource();
        ds.setUsername(username);
        ds.setPassword(password);
        ds.setDriverClassName(driverClassName);
        ds.setUrl(url);
        return ds;
    }

    @Bean
    public LocalSessionFactoryBean hibernateSessionFactory() {
        var sessionFactory = new LocalSessionFactoryBean();
        sessionFactory.setDataSource(dataSource());
        sessionFactory.setPackagesToScan("com.travelport.entities");
        var prop = new Properties();
        prop.put("hibernate.show_sql", "true");
        sessionFactory.setHibernateProperties(prop);

        return sessionFactory;
    }

    @Bean
    public TransactionManager transactionManager() {
        var transactionManager = new HibernateTransactionManager();
        transactionManager.setSessionFactory(hibernateSessionFactory().getObject());
        return transactionManager;
    }
}