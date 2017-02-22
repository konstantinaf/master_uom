package com.uom.jirareport.consumers.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * Created by fotarik on 13/02/2017.
 */
@Configuration
@EnableJpaRepositories(entityManagerFactoryRef = DaoConfigFactory.ENTITY_MANAGER_WRITE,
        basePackages = {DaoConfigFactory.JIRA_CONSUMER_DAO_PACKAGE})
public class JiraConsumerWriteDaoContext {

}
