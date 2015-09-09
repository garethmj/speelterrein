package be.cm.apps.playground.testjpa2.model1;

import java.io.Serializable;
import java.util.Collection;

import javax.persistence.*;

/**
 * Example entity to demonstrate the ElementCollection JPA2 mapping.
 *
 */
@Entity
public class ParameterList implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	private long id;
	private String name;

	@ElementCollection
	@CollectionTable(name="PARAMLIST_VALUES")
	@Column(name="pvalue")
	Collection<String> values;

	public ParameterList() {
		super();
	}

	public ParameterList(long id, String name, Collection<String> values) {
		super();
		this.id = id;
		this.name = name;
		this.values = values;
	}

	public long getId() {
		return id;
	}
	
	public String getName() {
		return name;
	}
	
	public Collection<String> getValues() {
		return values;
	}
}
