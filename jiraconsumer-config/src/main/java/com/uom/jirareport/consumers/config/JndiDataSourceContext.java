package com.uom.jirareport.consumers.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

/**
 * Created by fotarik on 13/02/2017.
 */
@Configuration
public class JndiDataSourceContext {


    @Bean
    public DataSource jupiter1DataSourceRead(){
        return DaoConfigFactory.lookupJndiDataSource(DaoConfigFactory.JUPITER1_JNDI_READ);
    }

}
