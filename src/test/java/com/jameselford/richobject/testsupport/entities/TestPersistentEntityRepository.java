package com.jameselford.richobject.testsupport.entities;

import com.jameselford.richobject.testsupport.entities.TestPersistentEntity;
import org.springframework.data.repository.CrudRepository;

public interface TestPersistentEntityRepository extends CrudRepository<TestPersistentEntity, String> {

    Iterable<TestPersistentEntity> findByStatus(String status);
}
