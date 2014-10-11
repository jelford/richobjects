package com.jameselford.richobject.testsupport;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import com.jameselford.richobject.RichObjectRepositoryFactoryBean;

@Configuration
@EnableJpaRepositories(basePackages = "com.jameselford.richobject.testsupport.domainobjects", repositoryFactoryBeanClass = RichObjectRepositoryFactoryBean.class)
public class RichRepositoryConfiguration {
}
