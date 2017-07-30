package com.event.sourcing.config;

import org.springframework.boot.orm.jpa.hibernate.SpringImplicitNamingStrategy;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;

import javax.sql.DataSource;
import java.util.Properties;
import java.util.UUID;

@Configuration
public class HibernateConfiguration {

    @Bean
    public DataSource dataSource() {
        return new EmbeddedDatabaseBuilder().setType(EmbeddedDatabaseType.H2)
                .setName(UUID.randomUUID().toString())
                .build();
    }

    @Bean
    public LocalSessionFactoryBean sessionFactoryBean() {
        LocalSessionFactoryBean result = new LocalSessionFactoryBean();
        result.setDataSource(dataSource());
        result.setPackagesToScan("com.event.*");

        Properties properties = new Properties();
        properties.setProperty("hibernate.hbm2ddl.auto", "create-drop");
        properties.setProperty("hibernate.dialect", "org.hibernate.dialect.H2Dialect");

        result.setHibernateProperties(properties);
        return result;
    }

    @Bean
    public SpringImplicitNamingStrategy springImplicitNamingStrategy() {
        return new SpringImplicitNamingStrategy();
    }

}
