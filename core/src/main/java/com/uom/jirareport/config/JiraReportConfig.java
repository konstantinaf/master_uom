package com.uom.jirareport.config;

import com.uom.jirareport.consumers.config.JiraConsumerConfigDaoContext;
import com.uom.jirareport.consumers.config.JiraConsumerWriteDaoContext;
import org.springframework.context.annotation.*;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

/**
 * Created by fotarik on 13/02/2017.
 */
@Configuration
//@Import({JiraConsumerWriteDaoContext.class, JiraConsumerConfigDaoContext.class})
@ComponentScan(basePackages = {
        "com.uom.jirareport.consumers.services",
        "com.uom.jirareport.controller"//,
        //"com.uom.jirareport.consumers.dto",
        //"com.uom.jirareport.consumers.dao"
}, excludeFilters = @ComponentScan.Filter(type = FilterType.ANNOTATION, value = Configuration.class))
@EnableAspectJAutoProxy(proxyTargetClass = true)
@EnableWebMvc
public class JiraReportConfig {
}
