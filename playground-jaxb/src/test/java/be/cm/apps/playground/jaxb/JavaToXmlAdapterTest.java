package be.cm.apps.playground.jaxb;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.StringWriter;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import org.junit.Test;

import be.cm.apps.playground.testjaxb.model.MyParameterType;
import be.cm.apps.playground.testjaxb.model.Parameter2;
import be.cm.apps.playground.util.NanoMeter;

/**
 * Example of using an adapter to change the marshaling of a value object
 * to xml.
 * 
 * @author Ivan
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement
public class JavaToXmlAdapterTest {

	@XmlJavaTypeAdapter(MyParameterAdapter.class)
	private Parameter2 parameter = new Parameter2();


	/*
	 * Sanity test, verify that the MyParameterType can be marshalled.
	 */
	@Test
	public void testMyParameterType() throws Exception {
		System.setProperty("javax.xml.bind.JAXBContext", "com.sun.xml.internal.bind.v2.ContextFactory");

		JAXBContext context = JAXBContext.newInstance(MyParameterType.class);
		Marshaller m = context.createMarshaller();

		MyParameterType myParam = new MyParameterType();
		myParam.name = "property1";
		myParam.value = "value1";

		StringWriter sw = new StringWriter();
		m.marshal(myParam, sw);
		assertEquals(
				"<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?><parameterAdapted><adaptedName>property1</adaptedName><value>value1</value></parameterAdapted>",
				sw.toString());
	}

	/*
	 * Test marshaling a value object using jaxb and an adapter.
	 * This test uses the SUN JAXB implementation.
	 */
	@Test
	public void testMarshalWithAdapterSun() throws Exception {
		NanoMeter logger = new NanoMeter();
		logger.log("Start testing marshal");

		// sun RI context
		System.setProperty("javax.xml.bind.JAXBContext", "com.sun.xml.internal.bind.v2.ContextFactory");
		JAXBContext context = JAXBContext
				.newInstance(JavaToXmlAdapterTest.class, MyParameterType.class, Parameter2.class);
		logger.log("created sun context:" + context.getClass());

		String xmlString = javaToXMLDemo(context);
		logger.log("after sun marshal:" + xmlString);
		System.out.println(xmlString);

		// Make sure the marshaling is correct
		assertTrue(xmlString, xmlString.indexOf("<parameter>") > 0);
		xmlString = xmlString.substring(xmlString.indexOf("<parameter>"));
		assertTrue(xmlString,
				xmlString
						.startsWith("<parameter><adaptedName>property1</adaptedName><value>value1</value></parameter>"));
	}

	/*
	 * Test marshaling a value object using jaxb and an adapter.
	 * This test uses the EclipseLink JAXB implementation.
	 */
	@Test
	public void testMarshalWithAdapterEclipselink() throws Exception {
		NanoMeter logger = new NanoMeter();
		logger.log("Start testing marshal");

		// eclipselink context
		System.setProperty("javax.xml.bind.JAXBContext", "org.eclipse.persistence.jaxb.JAXBContextFactory");
		JAXBContext context = JAXBContext.newInstance(JavaToXmlAdapterTest.class, MyParameterType.class,
				Parameter2.class);
		logger.log("created eclipse context:" + context.getClass());

		String xmlString = javaToXMLDemo(context);
		logger.log("after eclipselink marshal" + xmlString);

		// Make sure the marshaling is correct
		assertTrue(xmlString.indexOf("<parameter>") > 0);
		xmlString = xmlString.substring(xmlString.indexOf("<parameter>"));
		assertTrue(xmlString
				.startsWith("<parameter><adaptedName>property1</adaptedName><value>value1</value></parameter>"));

	}

	private String javaToXMLDemo(JAXBContext context) throws JAXBException {
		Marshaller m = context.createMarshaller();
		m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, false);

		parameter.setName("property1");
		parameter.setValue("value1");

		StringWriter sw = new StringWriter();

		m.marshal(this, sw);

		return sw.toString();
	}

}
