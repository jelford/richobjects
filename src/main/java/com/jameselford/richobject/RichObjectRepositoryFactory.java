package com.jameselford.richobject;

import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.context.ApplicationContext;
import org.springframework.data.jpa.repository.support.JpaRepositoryFactory;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.core.RepositoryMetadata;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
* Created by jelford on 11/10/14.
*/
class RichObjectRepositoryFactory extends JpaRepositoryFactory {

    private final ApplicationContext applicationContext;

    RichObjectRepositoryFactory(EntityManager entityManager, ApplicationContext applicationContext) {
        super(entityManager);
        this.applicationContext = applicationContext;
    }

    @SuppressWarnings("unchecked")
    @Override
    protected Object getTargetRepository(RepositoryMetadata metadata) {

        final Type[] repositoryGenericInterfaces = getRepostoryGenericInterfaces(metadata);

        ParameterizedType repositoryType = getRichObjectRepositoryType(metadata, repositoryGenericInterfaces);

        final Class<?> domainType = metadata.getDomainType();
        final Type implementationType = repositoryType.getActualTypeArguments()[1];
        final Type entityType = repositoryType.getActualTypeArguments()[2];

        checkPrototypeExistsBeforeProceeding(domainType);

        final CrudRepository entityRepository = getEntityRepository(entityType);

        return new RichObjectRepositoryImpl((Class)domainType, (Class)implementationType, (Class)entityType, entityRepository, applicationContext);
    }

    private void checkPrototypeExistsBeforeProceeding(final Class<?> domainType) {
        boolean prototypeExists = false;
        final String[] beanNamesForType = applicationContext.getBeanNamesForType(domainType);
        for (String beanName : beanNamesForType) {
            if (applicationContext.isPrototype(beanName)) {
                prototypeExists =  true;
            }
        }
        if (!prototypeExists) {
            throw new NoSuchBeanDefinitionException(domainType, "Can't find a bean prototype for " + domainType + " - will not be able to instantiate from repository");
        }
    }

    private ParameterizedType getRichObjectRepositoryType(final RepositoryMetadata metadata, final Type[] repositoryGenericInterfaces) {
        ParameterizedType repositoryType = null;
        for (Type repositoryGenericInterface : repositoryGenericInterfaces) {
            if (repositoryGenericInterface instanceof ParameterizedType) {
                final Type rawType = ((ParameterizedType) repositoryGenericInterface).getRawType();
                if (rawType instanceof Class &&
                        RichObjectRepository.class.isAssignableFrom((Class) rawType)) {

                    repositoryType = (ParameterizedType) repositoryGenericInterface;
                }
            }
        }
        if (repositoryType == null) {
            throw new RuntimeException("Can't identify RichObjectRepository type from repository meta: " + metadata);
        }
        return repositoryType;
    }

    private Type[] getRepostoryGenericInterfaces(final RepositoryMetadata metadata) {
        final Type[] repositoryGenericInterfaces = metadata.getRepositoryInterface().getGenericInterfaces();
        if (repositoryGenericInterfaces.length < 1) {
            throw new RuntimeException("Don't recognize " + metadata.getRepositoryInterface() + " as a Repository (does it extend " + Repository.class.getName() + "?)");
        }
        return repositoryGenericInterfaces;
    }

    private CrudRepository getEntityRepository(Type entityType) {
        return applicationContext.getBean(beanName(entityType) + "Repository", CrudRepository.class);

    }

    private String beanName(Type type) {
        final String typeName = ((Class<?>)type).getSimpleName();
        return typeName.substring(0, 1).toLowerCase() + typeName.substring(1);
    }


    @Override
    protected Class<?> getRepositoryBaseClass(RepositoryMetadata metadata) {
        return RichObjectRepository.class;
    }
}
