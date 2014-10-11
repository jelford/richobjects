package com.jameselford.richobject;

import com.google.common.collect.FluentIterable;
import org.springframework.context.ApplicationContext;
import org.springframework.data.repository.CrudRepository;

import java.io.Serializable;
import java.lang.reflect.Field;

import static com.google.common.base.Throwables.propagate;

public class RichObjectRepositoryImpl<RichObjectType, RichObjectImplementation extends RichObjectType, EntityType, ID extends Serializable>
        implements RichObjectRepository<RichObjectType, RichObjectImplementation, EntityType, ID> {

    private final Class<RichObjectType> domainClass;
    private final CrudRepository<EntityType, ID> entityRepository;
    private final ApplicationContext applicationContext;
    private final Field entityField;


    public RichObjectRepositoryImpl(
            Class<RichObjectType> domainClass,
            Class<RichObjectImplementation> implementationClass,
            Class<EntityType> entityType,
            CrudRepository<EntityType, ID> entityRepository,
            final ApplicationContext applicationContext) {

        this.domainClass = domainClass;
        this.entityRepository = entityRepository;
        this.applicationContext = applicationContext;

        try {
            entityField = findEntityField(implementationClass, entityType);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw propagate(e);
        }

    }

    private Field findEntityField(final Class<RichObjectImplementation> domainClass, final Class<EntityType> entityType) throws NoSuchFieldException, IllegalAccessException {

        for (Field field : domainClass.getDeclaredFields()) {
            final PersistenceEntity[] entityAnnotation = field.getAnnotationsByType(PersistenceEntity.class);
            if (entityAnnotation.length > 0) {
                if (!entityType.isAssignableFrom(field.getType())) {
                    throw new RuntimeException(":(");
                }

                field.setAccessible(true);

                return field;
            }
        }
        throw new RuntimeException("couldnt find entity entityField");

    }

    private RichObjectType createDomainObjectFromEntity(final EntityType entity) {
        return applicationContext.getBean(domainClass, entity);
    }

    private <S extends RichObjectType> EntityType persistenceObjectFromDomainObject(final S domainObject) {
        try {
            return (EntityType) entityField.get(domainObject);
        } catch (IllegalAccessException e) {
            throw propagate(e);
        }
    }

    @Override
    public <S extends RichObjectType> S save(S domainObject) {
        EntityType persistenceEntity = persistenceObjectFromDomainObject(domainObject);

        final EntityType possiblyMutatedPersistenceEntity = entityRepository.save(persistenceEntity);
        return (S)createDomainObjectFromEntity(possiblyMutatedPersistenceEntity);
    }

    @Override
    public <S extends RichObjectType> Iterable<S> save(Iterable<S> entities) {
        return FluentIterable.from(entities).transform(this::save).toList();
    }

    @Override
    public RichObjectType findOne(ID id) {
        final EntityType entity = entityRepository.findOne(id);
        return createDomainObjectFromEntity(entity);
    }

    @Override
    public boolean exists(ID id) {
        return entityRepository.exists(id);
    }

    @Override
    public Iterable<RichObjectType> findAll() {
        return FluentIterable.from(entityRepository.findAll()).transform(this::createDomainObjectFromEntity);
    }

    @Override
    public Iterable<RichObjectType> findAll(Iterable<ID> ids) {
        return FluentIterable.from(entityRepository.findAll(ids)).transform(this::createDomainObjectFromEntity);
    }

    @Override
    public long count() {
        return entityRepository.count();
    }

    @Override
    public void delete(ID id) {
        entityRepository.delete(id);
    }

    @Override
    public void delete(RichObjectType entity) {
        entityRepository.delete(persistenceObjectFromDomainObject(entity));
    }

    @Override
    public void delete(Iterable<? extends RichObjectType> entities) {
        entities.forEach(this::delete);
    }

    @Override
    public void deleteAll() {
        entityRepository.deleteAll();
    }
}

