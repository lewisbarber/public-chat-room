package com.lb.chat.room.config;

import java.util.Properties;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.Database;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EnableJpaRepositories(basePackages = { "com.lb.chat.room.repository" })
@EnableTransactionManagement
@ComponentScan(basePackages = { "com.lb.chat.room.service",
		"com.lb.chat.room.repository", "com.lb.chat.room.controller" })
@PropertySource("classpath:application-dev.properties")
public class DatabaseConfig {

	@Autowired
	private Environment env;

	@Autowired
	private DataSource dataSource;

	@Bean
	public DataSource dataSource() {

		DriverManagerDataSource dataSource = new DriverManagerDataSource();

		dataSource.setDriverClassName(env
				.getRequiredProperty("dataSource.driverClass"));
		dataSource.setUrl(env.getRequiredProperty("dataSource.jdbcUrl"));
		dataSource.setUsername(env.getRequiredProperty("dataSource.user"));
		dataSource.setPassword(env.getRequiredProperty("dataSource.password"));

		return dataSource;

	}

	@Bean
	public LocalContainerEntityManagerFactoryBean entityManagerFactory() {

		String showSql = env.getRequiredProperty("showSql");
		String generateDdl = env.getRequiredProperty("generateDdl");

		HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
		vendorAdapter.setDatabase(Database.MYSQL);
		if (env.getRequiredProperty("dataSource.engine").equals("ORACLE")) {
			vendorAdapter.setDatabase(Database.ORACLE);
		}
		vendorAdapter.setGenerateDdl(Boolean.parseBoolean(generateDdl));
		vendorAdapter.setShowSql(Boolean.parseBoolean(showSql));

		LocalContainerEntityManagerFactoryBean factory = new LocalContainerEntityManagerFactoryBean();
		factory.setJpaVendorAdapter(vendorAdapter);
		factory.setPackagesToScan("com.lb.chat.room.model");
		factory.setDataSource(dataSource());
		factory.setJpaProperties(jpaProperties());

		return factory;

	}

	private Properties jpaProperties() {

		String showSql = env.getRequiredProperty("showSql");

		Properties properties = new Properties();

		properties.put("hibernate.dialect",
				env.getRequiredProperty("dataSource.hibernateDialect"));
		properties.put("hibernate.show_sql", showSql);
		if (env.getRequiredProperty("dataSource.engine").equals("ORACLE")) {
			properties.put("hibernate.default_schema",
					env.getRequiredProperty("dataSource.defaultSchema"));
		}

		return properties;

	}

	@Bean
	public PlatformTransactionManager transactionManager() {

		JpaTransactionManager txManager = new JpaTransactionManager();
		txManager.setEntityManagerFactory(entityManagerFactory().getObject());

		return txManager;

	}

}