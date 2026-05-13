package com.emile.fintech.config;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.zaxxer.hikari.HikariDataSource;

import jakarta.persistence.EntityManagerFactory;

@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(entityManagerFactoryRef="transactionEntityManagerFactory",
transactionManagerRef="transactionTransactionManager", basePackages={"com.emile.fintech.transactions.repos"}) 
public class TransactionDataSourceConfig {

	/* @Value injects the JPA data source properties from application.properies in place of Spring boot auto-configuration b/c 
	   auto-configuration ignores every property except spring.datasource. */ 
	   @Value("${spring.datasource-transactions.username}")
	   private String username;
   
	   @Value("${spring.datasource-transactions.password}")
	   private String password;
   
	   @Value("${spring.datasource-transactions.url}")
	   private String url;

  @Bean(name = "transactionDataSourceProperties") // returns a bean/object using the properties in application.properties.
  @ConfigurationProperties("spring.datasource-transactions") // maps the properties in application.properties this method.  
  public DataSourceProperties transactionDataSourceProperties() {
	  return new DataSourceProperties();	
  }

  @Bean(name = "transactionDataSource")
  public DataSource transactionDataSource() /* builds a data source of type Hikari, using the
											  'transactionDataSourceProperties' bean/object. */
  {
	  return transactionDataSourceProperties().initializeDataSourceBuilder().type(HikariDataSource.class).build();
  }

  @Bean(name = "transactionEntityManagerFactory")
  public LocalContainerEntityManagerFactoryBean transactionEntityManagerFactory(
 			EntityManagerFactoryBuilder builder) /* builds an Entity Manager Factory Bean configured w/
          										transactionDataSource. The package of the entity, transaction, is 
          										scanned in the process of the build. */
	{
	  // the name given to persistenceUnit is to differentiate between the other persistenceUnit of the other microservices.
	  return builder.dataSource(transactionDataSource()).packages("com.emile.fintech.transactions.model").persistenceUnit("transactionDataSource").build();
	}

  @Bean(name="transactionTransactionManager")
  public PlatformTransactionManager transactionTransactionManager(@Qualifier("transactionEntityManagerFactory")
  EntityManagerFactory transactionEntityManagerFactory) /* @Qualifier distinguishes between the many Entity
														  Manager Factories across the microservices and injects the bean
														  passed as its parameter into EntityManagerFactory to configure
														  the bean from a LocalContainerEntityManagerFactoryBean to an
														  EntityManagerFactory, prior to the method returning a 
														  TransactionManager. */
  {
	  return new JpaTransactionManager(transactionEntityManagerFactory);
  }
}
