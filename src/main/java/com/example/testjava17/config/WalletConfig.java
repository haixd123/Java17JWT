package com.example.testjava17.config;

import com.example.testjava17.util.Constant;
import jakarta.persistence.EntityManagerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;

@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(
        basePackages = Constant.SPRING_SCAN_REPOSITORY.WALLET_PACKAGE_REPOSITORY,
        entityManagerFactoryRef = "walletEntityManagerFactory",
        transactionManagerRef = "walletTransactionManager"
)
public class WalletConfig {
    @Bean(name = "walletDataSource")
    @ConfigurationProperties(prefix = "spring.datasource.wallet")
    public DataSource walletDataSource() {
        return DataSourceBuilder.create().build();
    }

    @Bean(name = "walletEntityManagerFactory")
    public LocalContainerEntityManagerFactoryBean walletEntityManagerFactory(
            EntityManagerFactoryBuilder builder,
            @Qualifier("walletDataSource") DataSource dataSource) {
        return builder
                .dataSource(dataSource)
                .packages(Constant.SPRING_SCAN_ENTITY.WALLET_PACKAGE_ENTITIES)
                .persistenceUnit("walletPU")
                .build();
    }

    @Bean(name = "walletTransactionManager")
    public PlatformTransactionManager walletTransactionManager(
            @Qualifier("walletEntityManagerFactory") EntityManagerFactory emf) {
        return new JpaTransactionManager(emf);
    }
}
