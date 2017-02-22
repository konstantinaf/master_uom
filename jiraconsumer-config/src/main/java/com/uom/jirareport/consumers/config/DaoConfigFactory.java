package com.uom.jirareport.consumers.config;

import com.uom.jirareport.consumers.exceptions.ConfigurationException;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import java.util.Properties;

/**
 * Created by fotarik on 13/02/2017.
 */
public class DaoConfigFactory {

    //jndi lookups
    public static final String JUPITER1_JNDI_READ = "jdbc/Jupiter1DS";

    public static final String ENTITY_MANAGER_WRITE = "jiraEntityManagerFactoryBean";

    public static final String JIRA_CONSUMER_DTO_PACKAGE = "com.uom.jirareport.consumers.dto";
    public static final String JIRA_CONSUMER_DAO_PACKAGE = "com.uom.jirareport.consumers.dao";

    private DaoConfigFactory() {
    }

    public static DataSource lookupJndiDataSource(String jndiUrl) {
        try {
            InitialContext context = new InitialContext();
            return (DataSource) context.lookup("java:comp/env/" + jndiUrl);
        } catch (NamingException ex) {
            throw new ConfigurationException(ex);
        }
    }

    public static LocalContainerEntityManagerFactoryBean createEntityManagerFactoryBean(DataSource dataSource,
                                                                                        String persistenceUnitName,
                                                                                        String... modelPackages) {
        LocalContainerEntityManagerFactoryBean entityManagerFactoryBean = new LocalContainerEntityManagerFactoryBean();
        entityManagerFactoryBean.setDataSource(dataSource);
        entityManagerFactoryBean.setPackagesToScan(modelPackages);
        entityManagerFactoryBean.setJpaVendorAdapter(createHibernateJpaVendorAdapter());
        entityManagerFactoryBean.setJpaProperties(createJpaProperties());
        entityManagerFactoryBean.setPersistenceUnitName(persistenceUnitName);
        return entityManagerFactoryBean;
    }

    private static JpaVendorAdapter createHibernateJpaVendorAdapter() {
        HibernateJpaVendorAdapter adapter = new HibernateJpaVendorAdapter();
        adapter.setGenerateDdl(false);
        adapter.setShowSql(false);
        adapter.setDatabasePlatform("org.hibernate.dialect.MySQL5Dialect");
        return adapter;
    }

    private static Properties createJpaProperties() {
        Properties properties = new Properties();
        properties.put("hibernate.cache.use_second_level_cache", false);
        properties.put("hibernate.cache.use_query_cache", false);
        properties.put("hibernate.show_sql", false);

        return properties;
    }

    public static PlatformTransactionManager createJpaTransactionManger(LocalContainerEntityManagerFactoryBean entityManagerFactoryBean) {
        JpaTransactionManager txManager = new JpaTransactionManager();
        txManager.setEntityManagerFactory(entityManagerFactoryBean.getObject());
        txManager.setDataSource(entityManagerFactoryBean.getDataSource());
        txManager.setGlobalRollbackOnParticipationFailure(false);
        return txManager;
    }

}
