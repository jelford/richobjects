package com.jameselford.richobject.testsupport.domainobjects;

import com.jameselford.richobject.testsupport.entities.TestPersistentEntity;
import com.jameselford.richobject.RichObjectRepository;

public interface TestDomainObjectRepository extends RichObjectRepository<TestDomainObject, TestDomainObjectImplementation, TestPersistentEntity, String> {

}
