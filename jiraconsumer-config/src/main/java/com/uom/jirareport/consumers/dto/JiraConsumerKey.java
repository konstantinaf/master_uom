package com.uom.jirareport.consumers.dto;

import lombok.Getter;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;

/**
 * Created by fotarik on 21/02/2017.
 */
@Embeddable
@Getter
public class JiraConsumerKey implements Serializable{

    @Column(name="domain_name")
    private String domainName;

    @Column(name="jira_url")
    private String jiraUrl;
}
