package com.appengine.pay.dao;

import org.apache.commons.lang3.StringUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;

import javax.annotation.Resource;
import javax.sql.DataSource;

@Configuration
@EnableJpaRepositories(entityManagerFactoryRef = "payEntityManagerFactory", transactionManagerRef = "payTransactionManager")
public class PayDbConfig {

	@Resource(name = "payDataSource")
	private DataSource dataSource;

	@Bean
	PlatformTransactionManager payTransactionManager() {
		return new JpaTransactionManager(payEntityManagerFactory().getObject());
	}

	@Bean
	LocalContainerEntityManagerFactoryBean payEntityManagerFactory() {

		HibernateJpaVendorAdapter jpaVendorAdapter = new HibernateJpaVendorAdapter();
		jpaVendorAdapter.setGenerateDdl(true);

		LocalContainerEntityManagerFactoryBean factoryBean = new LocalContainerEntityManagerFactoryBean();

		factoryBean.setDataSource(dataSource);
		factoryBean.setJpaVendorAdapter(jpaVendorAdapter);
		//此处应包含当前模块的domain类
		String packageName = PayDbConfig.class.getPackage().getName();
		factoryBean.setPackagesToScan(StringUtils.substring(packageName, 0, StringUtils.lastIndexOf(packageName, '.')));

		return factoryBean;
	}

}
