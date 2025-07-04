package com.example.testjava17.config;

import com.example.testjava17.util.Constant;
import com.zaxxer.hikari.HikariDataSource;
import jakarta.persistence.EntityManagerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;

@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(
        basePackages = Constant.SPRING_SCAN_REPOSITORY.FYNA_PACKAGE_REPOSITORY,
        entityManagerFactoryRef = "fynaEntityManagerFactory",
        transactionManagerRef = "fynaTransactionManager"
)
public class FynaConfig {
    @Primary
    @Bean(name = "fynaDataSource")
    @ConfigurationProperties(prefix = "spring.datasource.fyna")
    public DataSource fynaDataSource() {
        return DataSourceBuilder.create()
                .type(HikariDataSource.class)
                .build();
    }

    @Primary
    @Bean(name = "fynaEntityManagerFactory")
    public LocalContainerEntityManagerFactoryBean fynaEntityManagerFactory(
            EntityManagerFactoryBuilder builder,
            @Qualifier("fynaDataSource") DataSource dataSource) {
        return builder
                .dataSource(dataSource)
                .packages(Constant.SPRING_SCAN_ENTITY.FYNA_PACKAGE_ENTITIES)
                .persistenceUnit("fynaPU")
                .build();
    }

    @Primary
    @Bean(name = "fynaTransactionManager")
    public PlatformTransactionManager fynaTransactionManager(
            @Qualifier("fynaEntityManagerFactory") EntityManagerFactory emf) {
        return new JpaTransactionManager(emf);
    }
}
