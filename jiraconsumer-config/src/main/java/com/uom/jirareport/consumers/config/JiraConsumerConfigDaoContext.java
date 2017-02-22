package com.uom.jirareport.consumers.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * Created by fotarik on 13/02/2017.
 */
@Configuration
@Import(value = {JndiDataSourceContext.class, JiraConsumerWriteDaoContext.class})
@EnableTransactionManagement
public class JiraConsumerConfigDaoContext {

    @Autowired
    private JndiDataSourceContext dataSourceContext;

    @Bean
    public LocalContainerEntityManagerFactoryBean jiraEntityManagerFactoryBean() {
        return DaoConfigFactory.createEntityManagerFactoryBean(dataSourceContext.jupiter1DataSourceRead(),
                DaoConfigFactory.ENTITY_MANAGER_WRITE, DaoConfigFactory.JIRA_CONSUMER_DTO_PACKAGE);
    }

    @Bean(name = "transactionManager")
    public PlatformTransactionManager transactionManager() {
        return DaoConfigFactory.createJpaTransactionManger(jiraEntityManagerFactoryBean());
    }
}
