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
@EnableTransactionManagement
public class DatabaseConfiguration {
 
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
		
//		<prop key="hibernate.connection.provider_class">com.zaxxer.hikari.hibernate.HikariConnectionProvider</prop>
//		<prop key="hibernate.hikari.dataSourceClassName">com.mysql.jdbc.jdbc2.optional.MysqlDataSource</prop>
//		<prop key="hibernate.hikari.dataSource.url">${database.connection}</prop>
//		<prop key="hibernate.hikari.dataSource.user">${database.username}</prop>
//		<prop key="hibernate.hikari.dataSource.password">${database.password}</prop>
//		<prop key="hibernate.hikari.dataSource.cachePrepStmts">true</prop>
//		<prop key="hibernate.hikari.dataSource.prepStmtCacheSize">250</prop>
//		<prop key="hibernate.hikari.dataSource.prepStmtCacheSqlLimit">2048</prop>
		
		Properties prop = new Properties();
		prop.setProperty("hibernate.connection.provider_class", "com.zaxxer.hikari.hibernate.HikariConnectionProvider");
		prop.setProperty("hibernate.hikari.dataSourceClassName", "com.mysql.jdbc.jdbc2.optional.MysqlDataSource");
		prop.setProperty("hibernate.hikari.dataSource.url", "jdbc:mysql://127.0.0.1:3307/janjua_TestDb");
		prop.setProperty("hibernate.hikari.dataSource.user", "admin");
		prop.setProperty("hibernate.hikari.dataSource.password", "admin");
		prop.setProperty("hibernate.hikari.dataSource.cachePrepStmts", "true");
		prop.setProperty("hibernate.hikari.dataSource.prepStmtCacheSize", "250");
		prop.setProperty("hibernate.hikari.dataSource.prepStmtCacheSqlLimit", "2048");
		
//		prop.setProperty("hibernate.dialect", "org.hibernate.dialect.MySQLDialect");
		prop.setProperty("hibernate.cache.provider_class", "org.hibernate.cache.NoCacheProvider");
		prop.setProperty("hibernate.hbm2ddl.auto", "update");
		prop.setProperty("hibernate.show_sql", "true");
		return prop;

		
	}
	
	public Properties hibernateProperties(){
		
		Properties properties = new Properties();
//		properties.setProperty("hibernate.dialect", "org.hibernate.dialect.MySQLDialect");
		properties.setProperty("hibernate.connection.provider_class", "com.zaxxer.hikari.hibernate.HikariConnectionProvider");
//		properties.setProperty("hibernate.connection.pool_size", "10");
		properties.setProperty("hibernate.show_sql", "true");
		properties.setProperty("hibernate.cache.provider_class", "org.hibernate.cache.NoCacheProvider");
		properties.setProperty("hibernate.hbm2ddl.auto", "update");
 
		return properties;		
	}
	
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
