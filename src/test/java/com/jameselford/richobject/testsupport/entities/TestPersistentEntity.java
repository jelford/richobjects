package com.jameselford.richobject.testsupport.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity(name = "TEST_PERSISTENT_ENTITY")
public class TestPersistentEntity {
    @Id
    @Column(name = "id")
    public String id;

    @Column(name = "status")
    String status;

    protected TestPersistentEntity() { }

    public TestPersistentEntity(final String id) {
        this.id = id;
    }
}
