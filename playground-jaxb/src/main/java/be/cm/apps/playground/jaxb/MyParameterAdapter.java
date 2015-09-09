package be.cm.apps.playground.jaxb;

import javax.xml.bind.annotation.adapters.XmlAdapter;

import be.cm.apps.playground.testjaxb.model.MyParameterType;
import be.cm.apps.playground.testjaxb.model.Parameter2;

/**
 * Example adapter that transforms Parameter2 value object to another type before
 * it get's written to xml.
 * 
 * @see JavaToXmlAdapterTest for an example usage.
 * 
 * @author Ivan
 *
 */
public class MyParameterAdapter extends XmlAdapter<MyParameterType, Parameter2> {
	@Override
	public Parameter2 unmarshal(MyParameterType v) throws Exception {
		Parameter2 result = null;
		if (v != null) {
			result = new Parameter2();
			result.setName(v.name);
			result.setValue(v.value);
		}
		return result;
	}

	@Override
	public MyParameterType marshal(Parameter2 v) throws Exception {
		MyParameterType result = null;
		if (v != null) {
			result = new MyParameterType();
			result.value = v.getValue();
			result.name = v.getName();
		}
		return result;
	}

}