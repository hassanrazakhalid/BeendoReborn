package com.Beendo.Configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Properties;

import org.apache.commons.dbcp.BasicDataSource;
import org.hibernate.SessionFactory;
import org.springframework.orm.hibernate4.LocalSessionFactoryBean;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.orm.hibernate4.HibernateTransactionManager;

@Configuration
@EnableTransactionManagement
public class DatabaseConfiguration {

	@Bean(name="dataSource")
	public BasicDataSource dataSource(){
		
		BasicDataSource basicDataSource = new BasicDataSource();
		basicDataSource.setDriverClassName("com.mysql.jdbc.Driver");
/*		basicDataSource.setUrl("jdbc:mysql://127.0.0.1:3306/janjua_TestDb");
		basicDataSource.setUsername("janjua_Admin");
		basicDataSource.setPassword(")th*@(i_IW43");*/
		basicDataSource.setUrl("jdbc:mysql://127.0.0.1:3306/janjua_TestDb");
		basicDataSource.setUsername("root");
		basicDataSource.setPassword("sa123");
		return basicDataSource;
	}
	
	public Properties hibernateProperties(){
		
		Properties properties = new Properties();
		properties.setProperty("hibernate.dialect", "org.hibernate.dialect.MySQLDialect");
		properties.setProperty("hibernate.connection.pool_size", "10");
		properties.setProperty("hibernate.show_sql", "true");
		properties.setProperty("hibernate.cache.provider_class", "org.hibernate.cache.NoCacheProvider");
		properties.setProperty("hibernate.hbm2ddl.auto", "update");

		return properties;
				
	}
	
	@Autowired
	@Bean(name = "sessionFactory")
	public LocalSessionFactoryBean sessionFactory(BasicDataSource dataSource){
		
		LocalSessionFactoryBean sessionFactory = new LocalSessionFactoryBean();
		sessionFactory.setPackagesToScan("com.Beendo.Entities");
		sessionFactory.setDataSource(dataSource);
		sessionFactory.setHibernateProperties(hibernateProperties());
		
		return sessionFactory;
	}
	
	@Autowired
	@Bean
	public HibernateTransactionManager transactionManager(SessionFactory sessionFactory){
		
		HibernateTransactionManager transctionManager = new HibernateTransactionManager();
		transctionManager.setSessionFactory(sessionFactory);
		return transctionManager;
	}
}
