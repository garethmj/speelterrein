package be.cm.apps.playground.jaxb;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import generated.Parameter;

import java.io.Reader;
import java.io.StringReader;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.ValidationEvent;
import javax.xml.bind.ValidationEventHandler;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;

import org.junit.experimental.theories.DataPoint;
import org.junit.experimental.theories.Theory;
import org.junit.runner.RunWith;
import org.xml.sax.SAXException;

import be.cm.apps.playground.util.Concurrent;
import be.cm.apps.playground.util.ConcurrentJunitRunner;
import be.cm.apps.playground.util.NanoMeter;

/**
 * JAXB example that parses an xml from memory using jaxb.
 * 
 * It uses the EclipseLink JAXB and the Sun JAXB implementations.
 * 
 * It demonstrates validating the xml using the setSchema() method.
 * 
 * It demonstrates using an EvenHandler for error handling.
 * 
 * It uses the ConcurrentJunitRunner to run the test with multiple threads.
 * 
 * It uses the DataPoint and Theory annotation to run different combinations of tests.
 * 
 * TODO: investigate the error i get when running with multiple threads sometimes i get an error -> maybe create a jira issue in eclipselink
 * (if it's eclipselink related...) org.junit.experimental.theories.internal.ParameterizedAssertionError: parseXmlString02(eclipseLinkJaxb,
 * runValidationOn) at org.junit.experimental.theories.Theories$TheoryAnchor.reportParameterizedError(Theories.java:183) at
 * org.junit.experimental.theories.Theories$TheoryAnchor$1$1.evaluate(Theories.java:138) at
 * org.junit.experimental.theories.Theories$TheoryAnchor.runWithCompleteAssignment(Theories.java:119) at
 * org.junit.experimental.theories.Theories$TheoryAnchor.runWithAssignment(Theories.java:103) at
 * org.junit.experimental.theories.Theories$TheoryAnchor.runWithIncompleteAssignment(Theories.java:112) at
 * org.junit.experimental.theories.Theories$TheoryAnchor.runWithAssignment(Theories.java:101) at
 * org.junit.experimental.theories.Theories$TheoryAnchor.runWithIncompleteAssignment(Theories.java:112) at
 * org.junit.experimental.theories.Theories$TheoryAnchor.runWithAssignment(Theories.java:101) at
 * org.junit.experimental.theories.Theories$TheoryAnchor.evaluate(Theories.java:89) at
 * org.junit.runners.BlockJUnit4ClassRunner.runNotIgnored(BlockJUnit4ClassRunner.java:79) at
 * org.junit.runners.BlockJUnit4ClassRunner.runChild(BlockJUnit4ClassRunner.java:71) at
 * org.junit.runners.BlockJUnit4ClassRunner.runChild(BlockJUnit4ClassRunner.java:49) at
 * org.junit.runners.ParentRunner$3.run(ParentRunner.java:193) at java.util.concurrent.Executors$RunnableAdapter.call(Executors.java:441) at
 * java.util.concurrent.FutureTask$Sync.innerRun(FutureTask.java:303) at java.util.concurrent.FutureTask.run(FutureTask.java:138) at
 * java.util.concurrent.Executors$RunnableAdapter.call(Executors.java:441) at
 * java.util.concurrent.FutureTask$Sync.innerRun(FutureTask.java:303) at java.util.concurrent.FutureTask.run(FutureTask.java:138) at
 * java.util.concurrent.ThreadPoolExecutor$Worker.runTask(ThreadPoolExecutor.java:886) at
 * java.util.concurrent.ThreadPoolExecutor$Worker.run(ThreadPoolExecutor.java:908) at java.lang.Thread.run(Thread.java:619) Caused by:
 * java.lang.LinkageError: loader (instance of org/eclipse/persistence/internal/jaxb/JaxbClassLoader): attempted duplicate class definition
 * for name: "org/eclipse/persistence/jaxb/generated0" at java.lang.ClassLoader.defineClass1(Native Method) at
 * java.lang.ClassLoader.defineClass(ClassLoader.java:621) at java.lang.ClassLoader.defineClass(ClassLoader.java:466) at
 * org.eclipse.persistence.internal.jaxb.JaxbClassLoader.generateClass(JaxbClassLoader.java:108) at
 * org.eclipse.persistence.jaxb.compiler.MappingsGenerator.generateWrapperClass(MappingsGenerator.java:2427) at
 * org.eclipse.persistence.jaxb.compiler.MappingsGenerator.generateWrapperClassAndDescriptor(MappingsGenerator.java:2217) at
 * org.eclipse.persistence.jaxb.compiler.MappingsGenerator.processGlobalElements(MappingsGenerator.java:2158) at
 * org.eclipse.persistence.jaxb.compiler.MappingsGenerator.generateProject(MappingsGenerator.java:189) at
 * org.eclipse.persistence.jaxb.compiler.Generator.generateProject(Generator.java:172) at
 * org.eclipse.persistence.jaxb.JAXBContextFactory.createContext(JAXBContextFactory.java:313) at
 * org.eclipse.persistence.jaxb.JAXBContextFactory.createContext(JAXBContextFactory.java:245) at
 * org.eclipse.persistence.jaxb.JAXBContextFactory.createContext(JAXBContextFactory.java:207) at
 * org.eclipse.persistence.jaxb.JAXBContextFactory.createContext(JAXBContextFactory.java:104) at
 * org.eclipse.persistence.jaxb.JAXBContextFactory.createContext(JAXBContextFactory.java:94) at
 * sun.reflect.NativeMethodAccessorImpl.invoke0(Native Method) at
 * sun.reflect.NativeMethodAccessorImpl.invoke(NativeMethodAccessorImpl.java:39) at
 * sun.reflect.DelegatingMethodAccessorImpl.invoke(DelegatingMethodAccessorImpl.java:25) at java.lang.reflect.Method.invoke(Method.java:597)
 * at javax.xml.bind.ContextFinder.newInstance(ContextFinder.java:202) at javax.xml.bind.ContextFinder.find(ContextFinder.java:343) at
 * javax.xml.bind.JAXBContext.newInstance(JAXBContext.java:574) at javax.xml.bind.JAXBContext.newInstance(JAXBContext.java:522) at
 * be.cm.apps.playground.jaxb.MemoryJaxbTest.parseXmlString(MemoryJaxbTest.java:129) at
 * be.cm.apps.playground.jaxb.MemoryJaxbTest.parseXmlString02(MemoryJaxbTest.java:84) at sun.reflect.NativeMethodAccessorImpl.invoke0(Native
 * Method) at sun.reflect.NativeMethodAccessorImpl.invoke(NativeMethodAccessorImpl.java:39) at
 * sun.reflect.DelegatingMethodAccessorImpl.invoke(DelegatingMethodAccessorImpl.java:25) at java.lang.reflect.Method.invoke(Method.java:597)
 * at org.junit.runners.model.FrameworkMethod$1.runReflectiveCall(FrameworkMethod.java:44) at
 * org.junit.internal.runners.model.ReflectiveCallable.run(ReflectiveCallable.java:15) at
 * org.junit.runners.model.FrameworkMethod.invokeExplosively(FrameworkMethod.java:41) at
 * org.junit.experimental.theories.Theories$TheoryAnchor$2.evaluate(Theories.java:167) at
 * org.junit.experimental.theories.Theories$TheoryAnchor$1$1.evaluate(Theories.java:133) ... 20 more
 * 
 * It could be a class loading issue that is not thread safe: http://jira.codehaus.org/browse/JETTY-418
 * 
 * 
 * 
 * @author Ivan
 * 
 */
@RunWith(ConcurrentJunitRunner.class)
@Concurrent(threads = 4)
public class MemoryXmlToJavaTest {

	private static Object lock = new Object(); // dummy object to synchronize on

	private static final String XML_DECLARATION = "<?xml version='1.0' encoding='UTF-8'?>\n";
	private static final String XML_INPUT_STRING = XML_DECLARATION
			+ "<parameter><name>PROPERTY1</name><value>VALUE1</value></parameter>";

	private static final String XSD_PARAMETER = "parameter.xsd";

	@DataPoint
	public static boolean runValidationOn = true;
	@DataPoint
	public static boolean runValidationOff = false;

	@DataPoint
	public static final String ECLIPSELINK_FACTORY = "org.eclipse.persistence.jaxb.JAXBContextFactory";

	@DataPoint
	public static final String SUN_FACTORY = "com.sun.xml.internal.bind.v2.ContextFactory";

	/*
	 * To be able to test the thread-safety i have created n number of indentical test cases.
	 */
	@Theory
	public void parseXmlString00(String jaxbImplementation, boolean validating) throws JAXBException, SAXException {
		parseXmlString(jaxbImplementation, validating);
	}

	@Theory
	public void parseXmlString01(String jaxbImplementation, boolean validating) throws JAXBException, SAXException {
		parseXmlString(jaxbImplementation, validating);
	}

	@Theory
	public void parseXmlString02(String jaxbImplementation, boolean validating) throws JAXBException, SAXException {
		parseXmlString(jaxbImplementation, validating);
	}

	@Theory
	public void parseXmlString03(String jaxbImplementation, boolean validating) throws JAXBException, SAXException {
		parseXmlString(jaxbImplementation, validating);
	}

	@Theory
	public void parseXmlString04(String jaxbImplementation, boolean validating) throws JAXBException, SAXException {
		parseXmlString(jaxbImplementation, validating);
	}

	@Theory
	public void parseXmlString05(String jaxbImplementation, boolean validating) throws JAXBException, SAXException {
		parseXmlString(jaxbImplementation, validating);
	}

	@Theory
	public void parseXmlString06(String jaxbImplementation, boolean validating) throws JAXBException, SAXException {
		parseXmlString(jaxbImplementation, validating);
	}

	@Theory
	public void parseXmlString07(String jaxbImplementation, boolean validating) throws JAXBException, SAXException {
		parseXmlString(jaxbImplementation, validating);
	}

	@Theory
	public void parseXmlString08(String jaxbImplementation, boolean validating) throws JAXBException, SAXException {
		parseXmlString(jaxbImplementation, validating);
	}

	@Theory
	public void parseXmlString09(String jaxbImplementation, boolean validating) throws JAXBException, SAXException {
		parseXmlString(jaxbImplementation, validating);
	}

	@Theory
	public void parseXmlString(String jaxbImplementation, boolean validating) throws JAXBException, SAXException {
		System.setProperty("javax.xml.bind.JAXBContext", jaxbImplementation);

		NanoMeter logger = new NanoMeter();
		logger.log("=== " + Thread.currentThread().getName());

		// newInstance is not thread safe in eclipselink
		JAXBContext ctx;
		synchronized (lock) {
			 ctx = JAXBContext.newInstance(Parameter.class);
		}
		Unmarshaller um = ctx.createUnmarshaller();

		// we will continue processing when we encounter errors
		MyEventHandler handler = new MyEventHandler();
		um.setEventHandler(handler);

		logger.log("created JAXBContext " + ctx.getClass().getName());
		if (validating) {
			logger.log("setting schema");
			// let's set the schema so that the marshaller validates the xml
			SchemaFactory sf = SchemaFactory.newInstance("http://www.w3.org/2001/XMLSchema");
			Schema schema = sf.newSchema(this.getClass().getResource(XSD_PARAMETER));
			um.setSchema(schema);
		}

		logger.log("starting unmarshal");
		Reader stringReader = new StringReader(XML_INPUT_STRING);
		Parameter param = (Parameter) um.unmarshal(stringReader);

		logger.log("unmarshal done");
		assertNotNull("unmarshal", param);

		// we should have found no errors
		assertEquals(0, handler.errorCounter);

		assertEquals("First property", "PROPERTY1", param.getName());
		assertEquals("First value", "VALUE1", param.getValue());

	}

	/*
	 * Simple EvenHandler that count's the events.
	 */
	private final class MyEventHandler implements ValidationEventHandler {
		public int errorCounter = 0;

		public boolean handleEvent(ValidationEvent event) {
			System.err.println(event);
			errorCounter++;
			return false;
		}
	}

}
