package be.cm.apps.playground.testjpa2;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.unitils.UnitilsJUnit4;
import org.unitils.orm.jpa.JpaUnitils;
import org.unitils.orm.jpa.annotation.JpaEntityManagerFactory;
import org.unitils.dbunit.annotation.DataSet;

import be.cm.apps.playground.testjpa2.model1.UserInfo;
import be.cm.apps.playground.testjpa2.repositories.UserRepository;

@DataSet
@JpaEntityManagerFactory(persistenceUnit = "testjpa2-test")
public class UnitilsJpaTest1 extends UnitilsJUnit4 {

	private UserRepository repository;

	@PersistenceContext
	EntityManager entityManager;

	@Before
	public void setUp() {
		repository = new UserRepository();
		JpaUnitils.injectEntityManagerInto(repository);
	}

	@Test
	public void testFindUserById() {
		UserInfo user = repository.find(2L);
		Assert.assertEquals("John Doe2", user.getName());
		// assertPropertyLenientEquals("name", Arrays.asList("John Doe"), user);
	}

}