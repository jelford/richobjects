package com.jameselford.richobject.testsupport.domainobjects;

import com.jameselford.richobject.testsupport.entities.TestPersistentEntity;
import com.jameselford.richobject.PersistenceEntity;

public class TestDomainObjectImplementation implements TestDomainObject {
    @PersistenceEntity
    TestPersistentEntity persistentEntity;

        public TestDomainObjectImplementation(final TestPersistentEntity persistentEntity) {
        this.persistentEntity = persistentEntity;
    }

    @Override
    public String getId() {
        return persistentEntity.id;
    }
}
