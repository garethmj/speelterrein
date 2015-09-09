package be.cm.apps.playground.testjaxb.model;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Custom class with correct xml annotations to mimic Parameter2 value object.
 * This is used in the adapter.
 * 
 * @author Ivan
 *
 */
@XmlRootElement(name = "parameterAdapted")
public class MyParameterType {
	@XmlElement(name="adaptedName")
	public String name;
	public String value;
}