package com.Beendo.Configuration;

import java.util.Properties;

import org.apache.commons.dbcp.BasicDataSource;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.orm.hibernate4.HibernateTransactionManager;
import org.springframework.orm.hibernate4.LocalSessionFactoryBean;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EnableTransactionManagement
public class DatabaseConfiguration {

	@Bean(name="dataSource")
	public BasicDataSource dataSource(){
		
		BasicDataSource basicDataSource = new BasicDataSource();
		basicDataSource.setDriverClassName("com.mysql.jdbc.Driver");
//		basicDataSource.setUrl("jdbc:mysql://162.144.198.59:3306/janjua_TestDb");

//		basicDataSource.setUrl("jdbc:mysql://127.0.0.1:3306/janjua_TestDb");		
//		basicDataSource.setUsername("janjua_admin");
//		basicDataSource.setPassword("7kcvfRSMJ4qP");
		basicDataSource.setUrl("jdbc:mysql://127.0.0.1:3307/janjua_TestDb");
		basicDataSource.setUsername("admin");
		basicDataSource.setPassword("admin");
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
