package com.jameselford.richobject

import javax.persistence.EntityManager

import org.junit.runner.RunWith
import org.mockito.Mockito
import org.scalatest.FlatSpec
import org.scalatest.junit.JUnitRunner
import org.springframework.data.repository.core.support.RepositoryFactorySupport

/**
 * Created by jelford on 11/10/14.
 */
@RunWith(classOf[JUnitRunner])
class RichObjectRepositoryFactoryBeanTest extends FlatSpec {

  "A RichObjectRepositoryFactoryBean" should "provide a RichObjectRepositoryFactory for spring to create repositories with" in {
    val factory: RepositoryFactorySupport = new RichObjectRepositoryFactoryBean(null).createRepositoryFactory(entityManager)
    assert(factory.isInstanceOf[RichObjectRepositoryFactory])
  }


  val entityManager = Mockito.mock(classOf[EntityManager])
}
