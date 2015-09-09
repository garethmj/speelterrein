package be.cm.apps.playground.testjpa2;

import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import be.cm.apps.playground.testjpa2.model1.ParameterList;

/**
 * An example unit test that loads data using JPA persists in the setup.
 * The test uses the same transaction for the setup and the test
 * and does a rollback after each test.
 * 
 * Remark: this test will work with multiple threads.
 * 
 * @author Ivan Belis
 * 
 */
public class SimpleJPaTest2 {
	private static final long TESTDATA_ID1 = 1L;
	
	private EntityManager em;

	@Before
	public void setUp() throws Exception {
		EntityManagerFactory emf = Persistence.createEntityManagerFactory("testjpa2-test");
		em = emf.createEntityManager();

		// Begin a new local transaction so that we can persist a new entity
		em.getTransaction().begin();

		// Read the existing entries
		Query q = em.createQuery("delete from ParameterList m where m.id = :id");
		q.setParameter("id", TESTDATA_ID1);
		int number = q.executeUpdate();
		assertTrue((number == 0) || (number == 1));

		// Lets create new entries
		List<String> paramValues = Arrays.asList("Buenos Aires", "Córdoba", "La Plata");
		ParameterList paramList = new ParameterList(TESTDATA_ID1, "aname", paramValues);

		em.persist(paramList);
		
		//Synchronize the persistence context to the underlying database. 
		em.flush();

		// And remove the entity from the persistence context because we want
		// to reuse the same EntityManager in the test and do not want it containing entities
		// from the setup.
		em.detach(paramList);
	}
	
	@After
	public void tearDown() {
		//Synchronize the persistence context to the underlying database.  This will cause
		// all pending db statements executed.
		em.flush();
		
		// Rollback the transaction, which will leave the database in it's original state.
		em.getTransaction().rollback();

		// It is always good practice to close the EntityManager so that
		// resources are conserved.
		em.close();
	}

	@Test
	public void checkParameterList() {
		// Now lets check the database and see if the created entries are there

		// Perform a simple query for the specified entity.
		Query q = em.createQuery("select m from ParameterList m where m.id = :id");
		q.setParameter("id", TESTDATA_ID1);

		// We should have 1 ParameterList in the database
		@SuppressWarnings("rawtypes")
		List resultList = q.getResultList();
		assertTrue(resultList.size() == 1);
		ParameterList paramList = (ParameterList) resultList.get(0);

		// We should have 3 values in ParameterList.values
		assertTrue(paramList.getValues().size() == 3);
	}

}
