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
@EnableJpaRepositories(entityManagerFactoryRef="userEntityManagerFactory",
transactionManagerRef="userTransactionManager", basePackages={"com.emile.fintech.user.repos"})
public class UserDataSourceConfig {

	/* @Value injects the JPA data source properties from application.properies in place of Spring boot auto-configuration b/c 
	 * auto-configuration ignores every property except spring.datasource. */ 
	@Value("${spring.datasource-user.username}")
	private String username;

	@Value("${spring.datasource-user.password}")
	private String password;

	@Value("${spring.datasource-user.url}")
	private String url;

  @Bean(name = "userDataSourceProperties") // returns a bean/object using the properties in application.properties.
  @ConfigurationProperties("spring.datasource-user") // maps the data fields to this method.  
  public DataSourceProperties userDataSourceProperties() {
	  return new DataSourceProperties();	
  }

  @Bean(name = "userDataSource")
  public DataSource userDataSource() /* builds a data source of type Hikari, using the
									  * 'userDataSourceProperties' bean/object. */
  {
	  return userDataSourceProperties().initializeDataSourceBuilder().type(HikariDataSource.class).build();
  }

  @Bean(name = "userEntityManagerFactory")
  public LocalContainerEntityManagerFactoryBean userEntityManagerFactory(
 			EntityManagerFactoryBuilder builder) /* builds an Entity Manager Factory Bean configured w/
          										  * userDataSource. The package of the entity, user, is 
          										  * scanned in the process of the build. */
	{
	  // the name given to persistenceUnit is to differentiate between the other persistenceUnit of the other microservices.
	  return builder.dataSource(userDataSource()).packages("com.emile.fintech.user.model").persistenceUnit("userDataSource").build();
	}

  @Bean(name="userTransactionManager")
  public PlatformTransactionManager userTransactionManager(@Qualifier("userEntityManagerFactory")
  EntityManagerFactory userEntityManagerFactory) /* @Qualifier distinguishes between the many Entity
												  * Manager Factories across the microservices and injects the bean
												  * passed as its parameter into EntityManagerFactory to configure
												  * the bean from a LocalContainerEntityManagerFactoryBean to an
												  * EntityManagerFactory, prior to the method returning a 
												  * TransactionManager. */
  {
	  return new JpaTransactionManager(userEntityManagerFactory);
  }
}
