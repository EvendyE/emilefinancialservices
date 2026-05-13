package com.emile.fintech.config;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.zaxxer.hikari.HikariDataSource;

import jakarta.persistence.EntityManagerFactory;

@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(entityManagerFactoryRef="accountEntityManagerFactory",
transactionManagerRef="accountTransactionManager", basePackages={"com.emile.fintech.accounts.repos"})
public class AccountDataSourceConfig {

  @Primary  	
  @Bean(name = "accountDataSourceProperties") // returns a bean/object using the properties in application.properties.
  @ConfigurationProperties("spring.datasource") // maps the properties in application.properties this method.  
  public DataSourceProperties accountDataSourceProperties() {
	  return new DataSourceProperties();	
  }

  @Primary
  @Bean(name = "accountDataSource")
  public DataSource accountDataSource() /* builds a data source of type Hikari, using the
											  'accountDataSourceProperties' bean/object. */
  {
	  return accountDataSourceProperties().initializeDataSourceBuilder().type(HikariDataSource.class).build();
  }

  @Primary
  @Bean(name = "accountEntityManagerFactory")
  public LocalContainerEntityManagerFactoryBean accountEntityManagerFactory(
 			EntityManagerFactoryBuilder builder) /* builds an Entity Manager Factory Bean configured w/
          										accountDataSource. The package of the entity, account, is 
          										scanned in the process of the build. */
  {
	  // the name given to persistenceUnit is to differentiate between the other persistenceUnit of the other microservices.
	  return builder.dataSource(accountDataSource()).packages("com.emile.fintech.accounts.model").persistenceUnit("accountDataSource").build();
  }

  @Primary
  @Bean(name="accountTransactionManager")
  public PlatformTransactionManager accountTransactionManager(@Qualifier("accountEntityManagerFactory")
  EntityManagerFactory accountEntityManagerFactory) /* @Qualifier distinguishes between the many Entity
														  Manager Factories across the microservices and injects the bean
														  passed as its parameter into EntityManagerFactory to configure
														  the bean from a LocalContainerEntityManagerFactoryBean to an
														  EntityManagerFactory, prior to the method returning a 
														  TransactionManager. */
  {
	  return new JpaTransactionManager(accountEntityManagerFactory);
  }
}
