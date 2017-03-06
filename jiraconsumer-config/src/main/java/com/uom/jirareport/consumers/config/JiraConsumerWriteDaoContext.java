package com.uom.jirareport.consumers.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

/**
 * Created by fotarik on 13/02/2017.
 */
@Configuration
@EnableJpaRepositories(entityManagerFactoryRef = DaoConfigFactory.ENTITY_MANAGER_WRITE,
        basePackages = {DaoConfigFactory.JIRA_CONSUMER_DAO_PACKAGE})
public class JiraConsumerWriteDaoContext {

}
