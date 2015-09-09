package be.cm.apps.playground.testjaxb.model;

import java.io.Serializable;
import java.util.Collection;

/**
 * Example entity to demonstrate the ElementCollection JPA2 mapping.
 *
 */
public class ParameterList implements Serializable {
	private static final long serialVersionUID = 1L;

	private long id;
	private String name;

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
