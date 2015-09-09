package be.cm.apps.playground.testjpa2.model2;

import java.io.Serializable;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Entity;
import javax.persistence.Id;

/**
 * Entity that demonstrates mix access mode (field or property) using
 * the @Access annotation
 *
 */
@Entity
@Access(AccessType.FIELD)
public class Property2 implements Serializable {
	@Id
	private long id;
	private String name;
	private String value;
	
	private char activeFlag;   // T if property is active , F otherwise
	
	private static final long serialVersionUID = 1L;

	public Property2() {
		super();
	}   
	public long getId() {
		return this.id;
	}

	public String getName() {
		return this.name;
	}   
	public String getValue() {
		return this.value;
	}

	public void setId(long id) {
		this.id = id;
	}   
	public void setName(String name) {
		this.name = name;
	}

	public void setValue(String value) {
		this.value = value;
	}
	
	public char getActiveFlag() {
		return activeFlag;
	}
	public void setActiveFlag(char activeFlag) {
		this.activeFlag = activeFlag;
	}
	



   
}
