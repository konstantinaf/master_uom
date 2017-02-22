package com.uom.jirareport.consumers.dao;

import com.uom.jirareport.consumers.dto.JiraConsumer;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Created by fotarik on 17/02/2017.
 */
public interface JiraConsumerRepository extends WriteJpaRepository<JiraConsumer, Long> {

    @Query("SELECT a FROM JiraConsumer a WHERE a.jiraConsumerKey.domainName =:domainName")
    JiraConsumer findJiraConsumerByHomeUrl(@Param("domainName") String domainName);

}
