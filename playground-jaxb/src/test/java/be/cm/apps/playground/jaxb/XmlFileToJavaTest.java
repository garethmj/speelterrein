package be.cm.apps.playground.jaxb;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.InputStream;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.ValidationEvent;
import javax.xml.bind.ValidationEventHandler;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;

import org.junit.Test;
import org.xml.sax.SAXException;

import playground.cm.be.model.log.LogHistory;
import playground.cm.be.model.log.MESSAGE;

import be.cm.apps.playground.util.NanoMeter;

/**
 * JAXB example that reads an xml from a file and unmarshal it to a value object.
 * 
 * It demonstrates the error-handling of the SUN and EclipseLink implementation.
 * 
 * @author Ivan
 * 
 */
public class XmlFileToJavaTest {

	private final class MyEventHandler implements ValidationEventHandler {
		public int errorCounter = 0;

		public boolean handleEvent(ValidationEvent event) {
			System.err.println(event);
			errorCounter++;
			return true;
		}
	}

	private static final String XSD_LOCATION = "log.xsd";
	private static final String SMALL_XML_FILE = "log_small.xml";
	private static final String ERROR_XML_FILE = "log_error.xml";

	@Test
	public void testParseEntireDocumentEclipseLinkValidating() throws JAXBException, SAXException {
		System.setProperty("javax.xml.bind.JAXBContext", "org.eclipse.persistence.jaxb.JAXBContextFactory");
		parseEntireDocument(SMALL_XML_FILE, true);
	}

	@Test
	public void testParseEntireDocumentSunValidating() throws JAXBException, SAXException {
		System.setProperty("javax.xml.bind.JAXBContext", "com.sun.xml.internal.bind.v2.ContextFactory");
		parseEntireDocument(SMALL_XML_FILE, true);
	}

	@Test
	public void testParseEntireDocumentEclipseLink() throws JAXBException, SAXException {
		System.setProperty("javax.xml.bind.JAXBContext", "org.eclipse.persistence.jaxb.JAXBContextFactory");
		parseEntireDocument(SMALL_XML_FILE, false);
	}

	@Test
	public void testParseEntireDocumentSun() throws JAXBException, SAXException {
		System.setProperty("javax.xml.bind.JAXBContext", "com.sun.xml.internal.bind.v2.ContextFactory");
		parseEntireDocument(SMALL_XML_FILE, false);
	}

	@Test
	public void testParseEntireDocumentErrorSunValidating() throws JAXBException, SAXException {
		System.setProperty("javax.xml.bind.JAXBContext", "com.sun.xml.internal.bind.v2.ContextFactory");
		parseEntireDocument(ERROR_XML_FILE, true);
	}

	@Test
	public void testParseEntireDocumentErrorSun() throws JAXBException, SAXException {
		System.setProperty("javax.xml.bind.JAXBContext", "com.sun.xml.internal.bind.v2.ContextFactory");
		parseEntireDocument(ERROR_XML_FILE, false);
	}

	@Test
	public void testParseEntireDocumentErrorEclipselinkValidating() throws JAXBException, SAXException {
		System.setProperty("javax.xml.bind.JAXBContext", "org.eclipse.persistence.jaxb.JAXBContextFactory");
		parseEntireDocument(ERROR_XML_FILE, true);
	}

	@Test
	public void testParseEntireDocumentErrorEclipselink() throws JAXBException, SAXException {
		System.setProperty("javax.xml.bind.JAXBContext", "org.eclipse.persistence.jaxb.JAXBContextFactory");
		parseEntireDocument(ERROR_XML_FILE, false);
	}

	private void parseEntireDocument(String filename, boolean validating) throws JAXBException, SAXException {
		NanoMeter logger = new NanoMeter();
		logger.log("===");

		InputStream xmlFile = this.getClass().getResourceAsStream(filename);
		assertNotNull("should find input xml file", xmlFile);

		JAXBContext ctx = JAXBContext.newInstance("playground.cm.be.model.log");
		Unmarshaller um = ctx.createUnmarshaller();

		// we will continue processing when we encounter errors
		MyEventHandler handler = new MyEventHandler();
		um.setEventHandler(handler);

		logger.log("created JAXBContext " + ctx.getClass().getName());
		if (validating) {
			logger.log("setting schema");
			// let's set the schema so that the marshaller validates the xml
			SchemaFactory sf = SchemaFactory.newInstance("http://www.w3.org/2001/XMLSchema");
			Schema schema = sf.newSchema(this.getClass().getResource(XSD_LOCATION));
			um.setSchema(schema);
		}

		logger.log("starting unmarshal");

		LogHistory history = (LogHistory) um.unmarshal(xmlFile);
		logger.log("unmarshal done");
		assertNotNull("unmarshal", history);

		logger.log("number of entries:" + history.getMESSAGE().size());

		if (SMALL_XML_FILE.equals(filename)) {
			// we should have found no errors
			assertEquals(0, handler.errorCounter);

			assertEquals("number of entries in small xml file", 20, history.getMESSAGE().size());

			MESSAGE firstEntry = history.getMESSAGE().get(0);
			assertEquals("First entry", 1, firstEntry.getHEADER().getMSGLEVEL());

			MESSAGE lastEntry = history.getMESSAGE().get(19);
			assertEquals("Last entry", 20, lastEntry.getHEADER().getMSGLEVEL());
		} else if (ERROR_XML_FILE.equals(filename)) {
			// Some errors are only reported when validating
			if (validating) {
				assertTrue("eclipselink reports 5 errors, sun reports 7 errors", handler.errorCounter >= 5);
			} else {
				assertTrue("eclipselink reports 0 errors, sun reports 2 errors", handler.errorCounter >= 0);
			}

			assertEquals("number of entries in error xml file", 4, history.getMESSAGE().size());

			MESSAGE firstEntry = history.getMESSAGE().get(0);
			assertEquals("First entry error file", 1, firstEntry.getHEADER().getMSGLEVEL());

			MESSAGE lastEntry = history.getMESSAGE().get(3);
			assertEquals("Last entry error file", 4, lastEntry.getHEADER().getMSGLEVEL());

		}
	}

}
