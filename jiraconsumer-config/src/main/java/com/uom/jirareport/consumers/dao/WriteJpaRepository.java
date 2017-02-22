package com.uom.jirareport.consumers.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

import java.io.Serializable;

/**
 * Created by fotarik on 17/02/2017.
 */
@NoRepositoryBean
public interface WriteJpaRepository <T extends Serializable, ID extends Serializable> extends JpaRepository<T, ID> {
}
