package be.cm.apps.playground.testjpa2.model1;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class UserInfo {

	@Id
	private long id;
	

	private String name;

	public long getId() {
		return id;
	}


	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}


	public void setId(long id) {
		this.id = id;
	}

}
