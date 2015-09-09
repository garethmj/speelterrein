package be.cm.apps.playground.testjpa2;

import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;

import org.junit.Before;
import org.junit.Test;

import be.cm.apps.playground.testjpa2.model1.ParameterList;
import be.cm.apps.playground.util.NanoMeter;

/**
 * An example unit test that loads data using JPA persists in the setup.
 * 
 * It uses different EntityManager's for the setup and the test, and thus also different transactions.
 * 
 * Remark: this test can fail if run with multiple threads.
 * 
 * @author Ivan
 * 
 */
public class SimpleJPaTest1 {
	private static final long TESTDATA_ID1 = 1L;
	private EntityManagerFactory emf = Persistence.createEntityManagerFactory("testjpa2-test");

	@Before
	public void setUp() throws Exception {
		EntityManager em = emf.createEntityManager();

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

		// Commit the transaction, which will cause the entity to
		// be stored in the database
		em.getTransaction().commit();

		// It is always good practice to close the EntityManager so that
		// resources are conserved.
		em.close();
	}

	@Test
	public void checkParameterSingleResult() {
		// Now lets check the database and see if the created entries are there
		// Create a fresh, new EntityManager
		EntityManager em = emf.createEntityManager();

		NanoMeter log = new NanoMeter();
		log.log("start checkParameterList");

		// Perform a simple query for all the entities
		Query q = em.createQuery("select m from ParameterList m where m.id = :id");
		q.setParameter("id", TESTDATA_ID1);
		log.log("after creating a query");

		// We should have exactly 1 entity
		ParameterList paramList = (ParameterList) q.getSingleResult();
		log.log("after retrieving the single result");

		// We should have 3 values in ParameterList.values
		assertTrue(paramList.getValues().size() == 3);
		log.log("after examining the values of the parameter");
		em.close();
	}

	@Test
	public void checkParameterList() {
		// Now lets check the database and see if the created entries are there
		// Create a fresh, new EntityManager
		EntityManager em = emf.createEntityManager();

		NanoMeter log = new NanoMeter();
		log.log("start checkParameterList");

		// Perform a simple query for all the entities
		Query q = em.createQuery("select m from ParameterList m where m.id = :id");
		q.setParameter("id", TESTDATA_ID1);
		log.log("after creating a query");

		// We should have found 1 entity
		@SuppressWarnings("rawtypes")
		List resultList = q.getResultList();
		log.log("after retrieving the resultlist");

		assertTrue(resultList.size() == 1);
		log.log("after asking the resultlist.size() method");

		ParameterList paramList = (ParameterList) resultList.get(0);
		log.log("after reading the first result");

		// We should have 3 values in ParameterList.values
		assertTrue(paramList.getValues().size() == 3);
		log.log("after examining the values of the parameter");
		em.close();
	}

}
