package be.cm.apps.playground.testjaxb.model;

import java.io.Serializable;

/**
 * Just a value object used for marshaling, it does NOT have an XmlRootElement annotation.
 *
 */
public class Parameter2 implements Serializable {
	
	private String name;
	private String value;
	private static final long serialVersionUID = 1L;

	public Parameter2() {
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
