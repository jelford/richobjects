package com.jameselford.richobject.repositorytests;


import com.jameselford.richobject.testsupport.JpaRepositoryConfiguration;
import com.jameselford.richobject.testsupport.RichRepositoryConfiguration;
import com.jameselford.richobject.testsupport.domainobjects.TestDomainObject;
import com.jameselford.richobject.testsupport.domainobjects.TestDomainObjectImplementation;
import com.jameselford.richobject.testsupport.entities.TestPersistentEntity;
import com.jameselford.richobject.testsupport.domainobjects.TestDomainObjectRepository;
import com.jameselford.richobject.testsupport.entities.TestPersistentEntityRepository;
import org.hamcrest.CoreMatchers;
import org.hamcrest.Matcher;
import org.hamcrest.Matchers;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Scope;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;

import javax.transaction.Transactional;
import java.util.Arrays;
import java.util.UUID;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {RichDirectoryRepositoryIntegrationTest.class, JpaRepositoryConfiguration.class, RichRepositoryConfiguration.class})
@Transactional
@TransactionConfiguration(defaultRollback = true)
public class RichDirectoryRepositoryIntegrationTest {

    @Autowired
    TestDomainObjectRepository richDirectoryRepository;

    @Autowired
    TestPersistentEntityRepository entityRepository;

    @Bean
    @Scope("prototype")
    public TestDomainObject tdo(TestPersistentEntity tpe) {
        return new TestDomainObjectImplementation(tpe);
    }

    @Test
    public void richDirectoryRepositoryCanBeWiredBySpring() {
        assertNotNull(richDirectoryRepository);
    }

    @Test
    public void canInsertAndRetrieveDomainObject() {
        String id = UUID.randomUUID().toString();
        final TestDomainObjectImplementation directory = new TestDomainObjectImplementation(new TestPersistentEntity(id));

        richDirectoryRepository.save(directory);

        final TestDomainObject retrieved = richDirectoryRepository.findOne(id);

        assertEquals(directory.getId(), retrieved.getId());
    }

    @Test
    public void findAllReturnsPreviouslyEnteredValues() throws Exception {

        String id1 = UUID.randomUUID().toString();
        final TestDomainObjectImplementation directory1 = new TestDomainObjectImplementation(new TestPersistentEntity(id1));
        String id2 = UUID.randomUUID().toString();
        final TestDomainObjectImplementation directory2 = new TestDomainObjectImplementation(new TestPersistentEntity(id2));

        richDirectoryRepository.save(Arrays.asList(directory1, directory2));

        assertThat(richDirectoryRepository.findAll(), Matchers.iterableWithSize(2));
    }

    @Test
    public void deletionRemovesAnObjectFromTheUnderlyingRepository() throws Exception {

        String id1 = UUID.randomUUID().toString();
        final TestDomainObjectImplementation directory1 = new TestDomainObjectImplementation(new TestPersistentEntity(id1));
        String id2 = UUID.randomUUID().toString();
        final TestDomainObjectImplementation directory2 = new TestDomainObjectImplementation(new TestPersistentEntity(id2));

        richDirectoryRepository.save(Arrays.asList(directory1, directory2));

        assertEquals(entityRepository.count(), 2);
        assertEquals(richDirectoryRepository.count(), 2);

        richDirectoryRepository.delete(directory1);
        assertEquals(entityRepository.count(), 1);
        assertThat(entityRepository.findOne(id1), Matchers.nullValue());

    }
}
