package be.ib.fit;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;

import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;

import org.junit.Test;


public class TestXmlWriter {
	



	
	private XMLStreamWriter out;

	@Test
	public void testWriteGPX() throws Exception {
		XMLOutputFactory outputFactory = XMLOutputFactory.newInstance();
		out = outputFactory.createXMLStreamWriter(new FileOutputStream("gpx.xml"), "utf-8");
	
		out.writeStartDocument();

		out.writeStartElement("gpx");
		out.writeAttribute("xmlns:tc2", "http://www.garmin.com/xmlschemas/TrainingCenterDatabase/v2");
		out.writeAttribute("xmlns:xsi", "http://www.w3.org/2001/XMLSchema-instance");
		out.writeAttribute("xmlns:gpxtpx", "http://www.garmin.com/xmlschemas/TrackPointExtension/v1");
		out.writeAttribute("xmlns", "http://www.topografix.com/GPX/1/1");
		out.writeAttribute("version", "1.1");
		out.writeAttribute("creator", "FitToGPXConverter");
		out.writeAttribute("xsi:schemaLocation", "http://www.topografix.com/GPX/1/1 http://www.topografix.com/GPX/1/1/gpx.xsd http://www.garmin.com/xmlschemas/GpxExtensions/v3 http://www.garmin.com/xmlschemas/GpxExtensionsv3.xsd http://www.garmin.com/xmlschemas/TrackPointExtension/v1 http://www.garmin.com/xmlschemas/TrackPointExtensionv1.xsd");
		
		writeMetaData();
		out.writeStartElement("trk");
		out.writeStartElement("name");
		out.writeCharacters("demo track");
		out.writeEndElement();
		out.writeStartElement("trkseg");
		writeTrackPoint();
		out.writeEndElement(); // end trkseg
		
		out.writeEndElement(); // end trk
		
		out.writeEndElement(); // end gpx
		out.writeEndDocument();
		out.close();
	}
	
	/*
	 *   <metadata>
    <name>Running 5/06/2011/13:56:00</name>
    <desc>Opbouw aantal km, hs rond 130</desc>
    <author>
      <name>Ivan Belis</name>
    </author>
    <link href="www.sports-tracker.com">
      <text>Sports Tracker</text>
    </link>
  </metadata>
	 */
	private void writeMetaData() throws XMLStreamException {
		out.writeStartElement("metadata");
		
		out.writeStartElement("name");
		out.writeCharacters("naam van de track");
		out.writeEndElement(); // end name
		
		out.writeStartElement("desc");
		out.writeCharacters("omschrijving van de track");
		out.writeEndElement(); // end desc
		
		out.writeStartElement("author");
		out.writeStartElement("name");
		out.writeCharacters("Jane Doe");
		out.writeEndElement(); // end name		
		out.writeEndElement(); // end author		
		
		out.writeEndElement(); // end metadata
	}

	/*
      <trkpt lat="51.09196333333333" lon="4.375595">
        <ele>13.9</ele>
        <time>2011-06-05T13:56:01.0</time>
        <extensions>
          <gpxtpx:TrackPointExtension>
            <gpxtpx:hr>106</gpxtpx:hr>
          </gpxtpx:TrackPointExtension>
        </extensions>
      </trkpt>	 */
	private void writeTrackPoint() throws XMLStreamException {
		out.writeStartElement("trkpt");
		out.writeAttribute("lat", "51.0919834");
		out.writeAttribute("lon", "51.0919834");
		out.writeStartElement("ele");
		out.writeCharacters( "16.2000000");
		out.writeEndElement(); // end ele
		out.writeStartElement("time");
		out.writeCharacters("2011-06-05T13:56:09Z");
		out.writeEndElement(); // end time
		
		out.writeStartElement("extensions");
		out.writeStartElement("gpxtpx:TrackPointExtension");
		out.writeStartElement("gpxtpx:hr");
		out.writeCharacters("106");
		out.writeEndElement(); // end hr
		out.writeEndElement(); // end gpxtpx:TrackPointExtension
		out.writeEndElement(); // end extensions
		
		out.writeEndElement(); // end trkpt
	}


}
