package be.cm.apps.playground.jaxb;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.io.InputStream;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.stream.EventFilter;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;

import org.junit.Test;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

import playground.cm.be.model.log.MESSAGE;
import be.cm.apps.playground.util.NanoMeter;

/**
 * JAXB example that reads a large xml file using jaxb.
 * 
 * It uses sax to read each element and then uses the standard unmarshal to transform the element to java classes.
 * 
 * Inspiration from : http://www.javarants.com/2006/04/30/simple-and-efficient-xml-parsing-using-jaxb-2-0/
 * 
 * 
 * @author Ivan
 * 
 */
public class LargeXmlFileToJavaTest {

	private static final String XSDFILE_LOCATION = "log.xsd";
	private static final String XMLFILE_LOCATION = "log_large.xml";

	@Test
	public void testParseLargeDocumentEclipseLinkValidating() throws JAXBException, SAXException, XMLStreamException,
			IOException {
		System.setProperty("javax.xml.bind.JAXBContext", "org.eclipse.persistence.jaxb.JAXBContextFactory");
		parseLargeFile(true);
	}

	@Test
	public void testParseLargeDocumentSunValidating() throws JAXBException, SAXException, XMLStreamException,
			IOException {
		System.setProperty("javax.xml.bind.JAXBContext", "com.sun.xml.internal.bind.v2.ContextFactory");
		parseLargeFile(true);
	}

	@Test
	public void testParseLargeDocumentEclipseLink() throws JAXBException, SAXException, XMLStreamException, IOException {
		System.setProperty("javax.xml.bind.JAXBContext", "org.eclipse.persistence.jaxb.JAXBContextFactory");
		parseLargeFile(false);
	}

	@Test
	public void testParseLargeDocumentSun() throws JAXBException, SAXException, XMLStreamException, IOException {
		System.setProperty("javax.xml.bind.JAXBContext", "com.sun.xml.internal.bind.v2.ContextFactory");
		parseLargeFile(false);
	}

	/**
	 * Parses a large xml file using Stax but each element is unmarshalled using JAXB.
	 * 
	 * @param validating
	 * @throws XMLStreamException
	 * @throws JAXBException
	 * @throws IOException
	 * @throws SAXException
	 */
	private void parseLargeFile(boolean validating) throws XMLStreamException, JAXBException, IOException, SAXException {
		NanoMeter logger = new NanoMeter();
		logger.log("===");

		InputStream xmlFile = this.getClass().getResourceAsStream(XMLFILE_LOCATION);
		assertNotNull("should find input xml file", xmlFile);

		try {
			XMLInputFactory xmlif = XMLInputFactory.newInstance();
			logger.log("created XMLInputFactory:" + xmlif.getClass().getName());

			// Parse the data, filtering out the start elements
			XMLEventReader xmler = xmlif.createXMLEventReader(xmlFile);
			EventFilter filter = new EventFilter() {
				public boolean accept(XMLEvent event) {
					return event.isStartElement();
				}
			};
			XMLEventReader xmlfer = xmlif.createFilteredReader(xmler, filter);

			// Jump to the first element in the document, the enclosing BugCollection
			logger.log("reading first element");
			StartElement e = (StartElement) xmlfer.nextEvent();
			assertEquals("LogHistory", e.getName().getLocalPart());

			// Parse into typed objects
			logger.log("creating JAXBContext");
			JAXBContext ctx = JAXBContext.newInstance(MESSAGE.class);
			Unmarshaller um = ctx.createUnmarshaller();

			if (validating) {
				logger.log("setting schema");
				// let's set the schema so that the marshaller validates the xml
				SchemaFactory sf = SchemaFactory.newInstance("http://www.w3.org/2001/XMLSchema");
				Schema schema = sf.newSchema(this.getClass().getResource(XSDFILE_LOCATION));
				um.setSchema(schema);
			}

			int entries = 0;
			logger.log("reading messages from xmlfile " + XMLFILE_LOCATION);
			while (xmlfer.peek() != null) {
				Object o = um.unmarshal(xmler);
				if (o instanceof MESSAGE) {
					MESSAGE msg = (MESSAGE) o;
					entries++;

					// process the entry
					short msgLevel = msg.getHEADER().getMSGLEVEL();
					assertTrue("all MSG_LEVEL's should be positive", msgLevel > 0);
					String msgText = msg.getPAYLOAD().getMSGTEXT();
					assertTrue("MSG_TEXT is required",msgText.length() > 0);
					
					
				} else {
					throw new IllegalArgumentException("Unexpected element found in xmlinput file:"
							+ o.getClass().getName());
				}
			}
			logger.log("number of entries:" + entries);
			assertEquals("number of entries in xml file", 85600, entries);
			xmlFile.close();
		} catch (JAXBException jaxbe) {
			// Extra exception handling to print the location information when an error occurs
			if ((jaxbe.getLinkedException() != null) && (jaxbe.getLinkedException() instanceof SAXParseException)) {
				SAXParseException saxe = (SAXParseException) jaxbe.getLinkedException();
				System.err.println("SAXParseException: line:" + saxe.getLineNumber() + " column:"
						+ saxe.getColumnNumber() + " " + saxe.getMessage());
			}
			throw jaxbe;
		} catch (SAXParseException saxe) {
			System.err.println("SAXParseException: line:" + saxe.getLineNumber() + " column:" + saxe.getColumnNumber()
					+ " " + saxe.getMessage());
			throw saxe;
		} finally {
			xmlFile.close();
		}
	}

}
