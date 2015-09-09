package be.cm.apps.playground.testjpa2.repositories;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import be.cm.apps.playground.testjpa2.model1.UserInfo;

public class UserRepository {
	@PersistenceContext(unitName = "testjpa2")
	private static EntityManager entityManager;

	public UserInfo find(long id) {
		return entityManager.find(UserInfo.class, new Long(id));
	}

	public static EntityManager getEntityManager() {
		return entityManager;
	}

	public static void setEntityManager(EntityManager entityManager) {
		UserRepository.entityManager = entityManager;
	}
}
