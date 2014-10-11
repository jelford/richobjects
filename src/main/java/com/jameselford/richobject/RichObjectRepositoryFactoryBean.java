package com.jameselford.richobject;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.data.jpa.repository.support.JpaRepositoryFactoryBean;
import org.springframework.data.repository.core.support.RepositoryFactorySupport;

import javax.persistence.EntityManager;
import java.io.Serializable;

public class RichObjectRepositoryFactoryBean<ROI extends T, ET, R extends RichObjectRepository<T, ROI, ET, ID>, T, ID extends Serializable> extends JpaRepositoryFactoryBean<R, T, ID> {

    private final ApplicationContext applicationContext;

    @Autowired
    RichObjectRepositoryFactoryBean(ApplicationContext ac) {
        applicationContext = ac;
    }

    protected RepositoryFactorySupport createRepositoryFactory(EntityManager entityManager) {
        return new RichObjectRepositoryFactory(entityManager, applicationContext);
    }

}
