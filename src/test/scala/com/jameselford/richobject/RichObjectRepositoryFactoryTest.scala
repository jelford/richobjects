package com.jameselford.richobject

import javax.persistence.EntityManager

import com.jameselford.richobject.testsupport.domainobjects.{TestDomainObject, TestDomainObjectRepository}
import com.jameselford.richobject.testsupport.entities.TestPersistentEntityRepository
import com.jameselford.richobject.testsupport.nonpersistence.NonRepository
import org.junit.runner.RunWith
import org.mockito.stubbing.OngoingStubbing
import org.mockito.{Matchers, Mockito}
import org.scalatest.FlatSpec
import org.scalatest.junit.JUnitRunner
import org.springframework.beans.factory.NoSuchBeanDefinitionException
import org.springframework.context.ApplicationContext
import org.springframework.data.repository.core.RepositoryMetadata

@RunWith(classOf[JUnitRunner])
class RichObjectRepositoryFactoryTest extends FlatSpec {

  val nonRepositoryClass : Class[_] = classOf[NonRepository]
  val jpaRepositoryClass : Class[_] = classOf[TestPersistentEntityRepository]
  val domainObjectRepositoryClass : Class[_] = classOf[TestDomainObjectRepository]

  val entityManager = Mockito.mock(classOf[EntityManager])
  val applicationContext = Mockito.mock(classOf[ApplicationContext])
  val repositoryMetadata = Mockito.mock(classOf[RepositoryMetadata])

  val domainObjectClass : Class[_] = classOf[TestDomainObject]
  val _genericCasting_When: OngoingStubbing[Class[_]] = Mockito.when(repositoryMetadata.getDomainType)
  _genericCasting_When.thenReturn(domainObjectClass)



  val factory = new RichObjectRepositoryFactory(entityManager, applicationContext)

  "Factory" should "throw when being asked to construct a non-repository type" in {

    tryingToInstantiateRepository(nonRepositoryClass)

    val error = intercept[RuntimeException] {
      factory.getTargetRepository(repositoryMetadata)
    }

    assert(error.getMessage contains "Repository")
  }

  def tryingToInstantiateRepository(clazz: Class[_]) {
    val when: OngoingStubbing[Class[_]] = Mockito.when(repositoryMetadata.getRepositoryInterface)
    when.thenReturn(clazz)
  }

  "Factory" should "throw when being asked to construct a repository type that's not rich-object" in {

    tryingToInstantiateRepository(jpaRepositoryClass)

    val error = intercept[RuntimeException] {
      factory.getTargetRepository(repositoryMetadata)
    }

    assert(error.getMessage contains "RichObjectRepository")
  }

  "Factory" should "check for the existence of a prototype method before building the repository" in {
    tryingToInstantiateRepository(domainObjectRepositoryClass)

    whenNoPrototypeIsAvailable

    val error = intercept[NoSuchBeanDefinitionException] {
      factory.getTargetRepository(repositoryMetadata)
    }

    assert(error.getMessage contains "prototype")
  }

  "Factory" should "return a repository implementation if able to instantiate the domain object from prototype" in {
    tryingToInstantiateRepository(domainObjectRepositoryClass)

    whenAPrototypeIsAvailable

    assert(factory.getTargetRepository(repositoryMetadata) !== null)
  }

  def whenAPrototypeIsAvailable {
    Mockito.when(applicationContext.getBeanNamesForType(Matchers.any[Class[_]]())).thenReturn(Array[String]("a prototype"))
    Mockito.when(applicationContext.isPrototype("a prototype")).thenReturn(true)
  }

  def whenNoPrototypeIsAvailable {
    Mockito.when(applicationContext.getBeanNamesForType(Matchers.any[Class[_]]())).thenReturn(Array[String]())
  }
}
