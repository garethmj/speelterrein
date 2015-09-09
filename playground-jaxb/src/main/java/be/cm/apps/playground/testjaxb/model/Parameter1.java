package be.cm.apps.playground.testjaxb.model;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * Just a value object used for marshaling, with the XmlRootElement annotation.
 * 
 */
@XmlRootElement(name = "parameter")
public class Parameter1 implements Serializable {
	private String name;
	private String value;
	private static final long serialVersionUID = 1L;

	public Parameter1() {
		super();
	}

	public Parameter1(String name, String value) {
		super();
		this.name = name;
		this.value = value;
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
