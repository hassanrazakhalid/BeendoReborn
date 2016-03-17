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

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

@Configuration
@EnableTransactionManagement//(proxyTargetClass=true)
public class DatabaseConfiguration {
// NEw Code is below    
//	@Bean(name="dataSource")
//	public BasicDataSource dataSource(){
//		// user dbcp apache datasource
//		BasicDataSource basicDataSource = new BasicDataSource();
//		basicDataSource.setDriverClassName("com.mysql.jdbc.Driver");
////		basicDataSource.setUrl("jdbc:mysql://162.214.3.88:3306/janjua_TestDb");
//
////		basicDataSource.setUrl("jdbc:mysql://127.0.0.1:3306/janjua_TestDb");		
////		basicDataSource.setUsername("janjua_admin");
////		basicDataSource.setPassword("7kcvfRSMJ4qP");
//		basicDataSource.setUrl("jdbc:mysql://127.0.0.1:3307/janjua_TestDb");
//		basicDataSource.setUsername("admin");
//		basicDataSource.setPassword("admin");
//		return basicDataSource;
//	}
	
	@Bean(name="dataSource")
	public HikariDataSource dataSource(){
		// user dbcp apache datasource
		HikariDataSource basicDataSource = new HikariDataSource();
//		basicDataSource.setDriverClassName("com.mysql.jdbc.jdbc2.optional.MysqlDataSource");
//		basicDataSource.setda("com.mysql.jdbc.jdbc2.optional.MysqlDataSource");
//		basicDataSource.setMaximumPoolSize(20);
//		basicDataSource.setIdleTimeout(30000);
//		basicDataSource.setJdbcUrl("jdbc:mysql://127.0.0.1:3307/janjua_TestDb");
//		basicDataSource.setUsername("admin");
//		basicDataSource.setPassword("admin");
		return basicDataSource;
		
//		BasicDataSource basicDataSource = new BasicDataSource();
//		basicDataSource.setDriverClassName("com.mysql.jdbc.Driver");
//		basicDataSource.setUrl("jdbc:mysql://162.214.3.88:3306/janjua_TestDb");

//		basicDataSource.setUrl("jdbc:mysql://127.0.0.1:3306/janjua_TestDb");		
//		basicDataSource.setUsername("janjua_admin");
//		basicDataSource.setPassword("7kcvfRSMJ4qP");
//		basicDataSource.setUrl("jdbc:mysql://127.0.0.1:3307/janjua_TestDb");
//		basicDataSource.setUsername("admin");
//		basicDataSource.setPassword("admin");
//		return basicDataSource;
	}
	
	private Properties hikariProperties(){
				
		Properties prop = new Properties();
		prop.setProperty("hibernate.connection.provider_class", "com.zaxxer.hikari.hibernate.HikariConnectionProvider");
		prop.setProperty("hibernate.hikari.dataSourceClassName", "com.mysql.jdbc.jdbc2.optional.MysqlDataSource");

		// Server Db Settings
		
//		prop.setProperty("hibernate.hikari.dataSource.url", "jdbc:mysql://127.0.0.1:3306/janjua_partracker_prod");
//		prop.setProperty("hibernate.hikari.dataSource.user", "janjua_admin");
//		prop.setProperty("hibernate.hikari.dataSource.password", "7kcvfRSMJ4qP");
		
		// Local Db settings
//		janjua_partracker_prod
		prop.setProperty("hibernate.hikari.dataSource.url", "jdbc:mysql://127.0.0.1:3307/janjua_TestDb");
		prop.setProperty("hibernate.hikari.dataSource.user", "admin");
		prop.setProperty("hibernate.hikari.dataSource.password", "admin");
		//
		prop.setProperty("hibernate.hikari.dataSource.cachePrepStmts", "true");
		prop.setProperty("hibernate.hikari.dataSource.prepStmtCacheSize", "250");
		prop.setProperty("hibernate.hikari.dataSource.prepStmtCacheSqlLimit", "2048");
		
//		prop.setProperty("hibernate.dialect", "org.hibernate.dialect.MySQLDialect");
		prop.setProperty("hibernate.cache.provider_class", "org.hibernate.cache.NoCacheProvider");
		prop.setProperty("hibernate.hbm2ddl.auto", "validate");
		prop.setProperty("hibernate.show_sql", "true");
		return prop;

		
	}
	

//    validate: validate that the schema matches, make no changes to the schema of the database, you probably want this for production.
//    update: update the schema to reflect the entities being persisted
//    create: creates the schema necessary for your entities, destroying any previous data.
//    create-drop: create the schema as in create above, but also drop the schema at the end of the session. This is great in early development or for testing.

	
//	public Properties hibernateProperties(){
//		
//		Properties properties = new Properties();
////		properties.setProperty("hibernate.dialect", "org.hibernate.dialect.MySQLDialect");
//		properties.setProperty("hibernate.connection.provider_class", "com.zaxxer.hikari.hibernate.HikariConnectionProvider");
////		properties.setProperty("hibernate.connection.pool_size", "10");
//		properties.setProperty("hibernate.show_sql", "true");
//		properties.setProperty("hibernate.cache.provider_class", "org.hibernate.cache.NoCacheProvider");
//		properties.setProperty("hibernate.hbm2ddl.auto", "validate");
// 
//		return properties;		
//	}
	
	@Autowired
	@Bean(name = "sessionFactory")
	public LocalSessionFactoryBean sessionFactory(HikariDataSource dataSource){
		
		LocalSessionFactoryBean sessionFactory = new LocalSessionFactoryBean();
		sessionFactory.setPackagesToScan("com.Beendo.Entities");
//		sessionFactory.setDataSource(dataSource);
		sessionFactory.setHibernateProperties(hikariProperties());
		
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
