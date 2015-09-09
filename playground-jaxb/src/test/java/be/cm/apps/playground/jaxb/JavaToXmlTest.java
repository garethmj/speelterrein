package be.cm.apps.playground.jaxb;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.StringWriter;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.namespace.QName;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.MethodRule;
import org.junit.rules.TestName;

import be.cm.apps.playground.testjaxb.model.Parameter1;
import be.cm.apps.playground.testjaxb.model.Parameter2;
import be.cm.apps.playground.util.NanoMeter;
import be.cm.apps.playground.util.TestRepeater;

/**
 * JAXB example that marshals a value object (Parameter1) to xml.
 * 
 * The example measures performance difference between
 * the Sun JAXB implementation and the Eclipselink implementation.
 * 
 * It also demonstrates the marshaling of a POJO that does not have
 * any xml annotations (XmlRootElement is missing).
 * 
 * @author Ivan
 *
 */
public class JavaToXmlTest {
	
    @Rule
    public TestName name= new TestName();
    
    @Rule
    public MethodRule repeater = new TestRepeater();

	@Test
	public void testJavaToXMLDemo() throws Exception {
		NanoMeter logger = new NanoMeter();

		logger.log("starting ----- " + name.getMethodName());
		JAXBContext context = JAXBContext.newInstance(Parameter1.class);
		Marshaller m = context.createMarshaller();
		m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

		StringWriter sw = new StringWriter();
		m.marshal(new Parameter1("aName","aValue"), sw);
		String xmlString = sw.toString();
		logger.log("ended first javaToXMLDemo");

		assertTrue("should contain <parameter>", xmlString.contains("<parameter>"));
		assertTrue("should contain aName", xmlString.contains("aName"));
	}
	


	@Test
	public void testJaxbContextCreation() throws Exception {
		NanoMeter logger = new NanoMeter();
		logger.log("Start testing jaxb Context creation");
		// default context
		JAXBContext context1 = JAXBContext.newInstance(Parameter1.class);
		logger.log("using context1:" + context1.getClass());

		// sun RI context
		System.setProperty("javax.xml.bind.JAXBContext", "com.sun.xml.internal.bind.v2.ContextFactory");
		JAXBContext context2 = JAXBContext.newInstance(Parameter1.class);
		logger.log("using context2:" + context2.getClass());

		// eclipselink context
		System.setProperty("javax.xml.bind.JAXBContext", "org.eclipse.persistence.jaxb.JAXBContextFactory");
		JAXBContext context3 = JAXBContext.newInstance(Parameter1.class);
		logger.log("using context3:" + context3.getClass());

		// eclipselink context second time
		System.setProperty("javax.xml.bind.JAXBContext", "org.eclipse.persistence.jaxb.JAXBContextFactory");
		JAXBContext context4 = JAXBContext.newInstance(Parameter1.class);
		logger.log("using context4:" + context4.getClass());
	}
	
	@Test
	public void testCompareJAXBImplementations() throws Exception {
		NanoMeter logger = new NanoMeter();
		logger.log("Start testing marshal");

		// sun RI context
		System.setProperty("javax.xml.bind.JAXBContext", "com.sun.xml.internal.bind.v2.ContextFactory");
		JAXBContext contextSun = JAXBContext.newInstance(Parameter1.class);
		logger.log("created sun context:" + contextSun.getClass());

		// eclipselink context
		System.setProperty("javax.xml.bind.JAXBContext", "org.eclipse.persistence.jaxb.JAXBContextFactory");
		JAXBContext contextEclipselink = JAXBContext.newInstance(Parameter1.class);
		logger.log("created eclipse context:" + contextEclipselink.getClass());

		String xmlStringEclipselink = javaToXMLDemo(contextEclipselink);
		logger.log("after eclipselink marshal" + xmlStringEclipselink);
		
		String xmlStringSun = javaToXMLDemo(contextSun);
		logger.log("after sun marshal:" + xmlStringSun);


		// Make sure the marshaling is correct
		// Ignore the first line because they are different (standalone option)
		xmlStringEclipselink = xmlStringEclipselink.substring(xmlStringEclipselink.indexOf("<parameter>"));
		xmlStringSun = xmlStringSun.substring(xmlStringSun.indexOf("<parameter>"));
		assertEquals("<parameter><name>CA</name><value>Cath</value></parameter>", xmlStringSun);
		assertEquals("<parameter><name>CA</name><value>Cath</value></parameter>", xmlStringEclipselink);
	}

	@Test
	public void testMarshalPerformance() throws Exception {
		NanoMeter logger = new NanoMeter();
		logger.log("Start testing marshal");

		// sun RI context
		System.setProperty("javax.xml.bind.JAXBContext", "com.sun.xml.internal.bind.v2.ContextFactory");
		JAXBContext contextSun = JAXBContext.newInstance(Parameter1.class);
		logger.log("created sun context:" + contextSun.getClass());

		// eclipselink context
		System.setProperty("javax.xml.bind.JAXBContext", "org.eclipse.persistence.jaxb.JAXBContextFactory");
		JAXBContext contextEclipselink = JAXBContext.newInstance(Parameter1.class);
		logger.log("created eclipse context:" + contextEclipselink.getClass());

		String xmlStringSun = javaToXMLDemo(contextSun);
		logger.log("after sun marshal:" + xmlStringSun);

		String xmlStringEclipselink = javaToXMLDemo(contextEclipselink);
		logger.log("after eclipselink marshal" + xmlStringEclipselink);

		// Make sure the marshaling is correct
		// Ignore the first line because they are different (standalone option)
		assertEquals("<parameter><name>CA</name><value>Cath</value></parameter>"
					, xmlStringSun.substring(xmlStringSun.indexOf("<parameter>")));
		assertEquals("<parameter><name>CA</name><value>Cath</value></parameter>"
					, xmlStringEclipselink.substring(xmlStringEclipselink.indexOf("<parameter>")));

		// Now for the performance test
		logger.log("starting sun marshaling");
		for (int i = 0; i < 10; i++) {
			String xmlString = javaToXMLDemo(contextSun);
			assertEquals(xmlStringSun,xmlString);
		}
		logger.log("ended 10 sun marshalings");

		for (int i = 0; i < 10; i++) {
			String xmlString = javaToXMLDemo(contextEclipselink);
			assertEquals(xmlStringEclipselink,xmlString);
		}
		logger.log("ended 10 eclipselink marshalings");
	}

	private String javaToXMLDemo(JAXBContext context) throws JAXBException {
		Marshaller m = context.createMarshaller();
		m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, false);

		Parameter1 object = new Parameter1();
		object.setName("CA");
		object.setValue("Cath");

		StringWriter sw = new StringWriter();
		m.marshal(object, sw);
		return sw.toString();
	}
	
	
	/**
	 * Demonstrate the marshaling of a POJO that does NOT have the XmlRootElement
	 * annotation.
	 * @throws Exception 
	 *  
	 */
	@Test
	public void testMarshalNoRootElement() throws Exception {
		JAXBContext context = JAXBContext.newInstance(Parameter2.class);
		Marshaller m = context.createMarshaller();
		m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

		Parameter2 object = new Parameter2();
		object.setName("someName");
		object.setValue("someValue");

		StringWriter sw = new StringWriter();
		m.marshal(new JAXBElement<Parameter2>(
				  new QName("parameter2"), Parameter2.class, object ),sw);
		String xmlString = sw.toString();
		assertTrue("should contain <parameter2>", xmlString.contains("<parameter2>"));
		assertTrue("should contain <name>someName</name>", xmlString.contains("<name>someName</name>"));


	}

}
