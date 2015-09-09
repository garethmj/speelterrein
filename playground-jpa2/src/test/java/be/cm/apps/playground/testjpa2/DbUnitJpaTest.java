package be.cm.apps.playground.testjpa2;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Properties;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import org.dbunit.database.DatabaseConnection;
import org.dbunit.database.IDatabaseConnection;
import org.dbunit.dataset.xml.FlatXmlDataSet;
import org.dbunit.operation.DatabaseOperation;

import junit.framework.TestCase;
import be.cm.apps.playground.testjpa2.model1.UserInfo;
import be.cm.apps.playground.testjpa2.repositories.UserRepository;

/**
 * Example of a dbunit test that loads data using dbunit and a connection that
 * was manually created.
 * 
 * @author Ivan Belis
 * 
 */
public class DbUnitJpaTest extends TestCase {
	private EntityManagerFactory emf = Persistence.createEntityManagerFactory("testjpa2-test");

	public void setUp() throws Exception {
		// create an SQL connection
		Properties props = null;
		Connection connection = DriverManager.getConnection("jdbc:derby:C:/Users/Ivan/MyDB;create=true", props);
        
		// create a dbunit connection
		IDatabaseConnection con = new DatabaseConnection(connection);
		
		// create a dbunit dataset
		InputStream testData = UserInfo.class.getResourceAsStream("/userinfo.db.xml");		
        FlatXmlDataSet dataSet = new FlatXmlDataSet(testData);

        // load the dbunit dataset in the dbunit connection
        DatabaseOperation.CLEAN_INSERT.execute(con, dataSet);

        con.close();
	}

	public void testFindAll() {
		UserRepository.setEntityManager(emf.createEntityManager());

		UserInfo user = new UserRepository().find(1);

		assertNotNull(user);
		assertEquals(1, user.getId());
		assertEquals("John Doe", user.getName());
	}

	public void testInsertOne() {
		EntityManager em = emf.createEntityManager();
		UserRepository userRepository = new UserRepository();
		em.getTransaction().begin();
		UserRepository.setEntityManager(em);

		UserInfo user = new UserInfo();
		user.setId(10);
		user.setName("number10");

		em.persist(user);

		UserInfo user10 = userRepository.find(10);

		assertNotNull(user10);
		assertEquals(10, user10.getId());
		assertEquals("number10", user10.getName());

		em.remove(user10);

		em.flush();

		em.getTransaction().rollback();
	}

}
