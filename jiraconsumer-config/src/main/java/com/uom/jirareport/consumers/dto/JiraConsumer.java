package com.uom.jirareport.consumers.dto;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Created by fotarik on 13/02/2017.
 */
@Entity
@Table(name = "jira_consumer")
@Getter
public class JiraConsumer implements Serializable {

    @EmbeddedId
    private JiraConsumerKey jiraConsumerKey;

    @Column(name = "consumer_key")
    private String consumerKey;

    @Column(name = "private_key")
    private String privateKey;

    @Column(name = "jira_rest_url")
    private String jiraRestUrl;
}