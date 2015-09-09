package be.cm.apps.playground.testjpa2.model3;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

/**
 * Entity implementation class for Entity: Parameter
 *
 */
@Entity
@Table(name="PARAMETER")
@SequenceGenerator(name = "ParameterIdSequence")
public class Parameter implements Serializable {
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="ParameterIdSequence")
	@Id
	private long id;
	
	private String name;
	private String value;
	private static final long serialVersionUID = 1L;

	public Parameter() {
		super();
	}   
	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}   
	public String getValue() {
		return this.value;
	}

	public void setValue(String value) {
		this.value = value;
	}
   
}
