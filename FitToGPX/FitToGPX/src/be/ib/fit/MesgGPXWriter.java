////////////////////////////////////////////////////////////////////////////////
// The following FIT Protocol software provided may be used with FIT protocol

package be.ib.fit;

import java.io.PrintStream;
import java.math.BigDecimal;
import java.util.Collection;
import java.util.Iterator;

import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;

import com.garmin.fit.Field;
import com.garmin.fit.Mesg;
import com.garmin.fit.MesgDefinition;
import com.garmin.fit.MesgDefinitionListener;
import com.garmin.fit.MesgListener;
import com.garmin.fit.csv.MesgCSVWriter;

/**
 * 
 * Convert FIT format to GPX format.
 * 
 * @author Ivan
 * @see MesgCSVWriter
 */
public class MesgGPXWriter implements MesgListener, MesgDefinitionListener {

	private XMLStreamWriter out;
	private PrintStream stream;

	// Used to make sure that we do not write empty track segments
	private boolean inTrackSegment = false; // true if currently writing a track
											// segment
	private int numTrackPointInSegment = 0; // number of trackpoints in the
											// current track segment

	private static final String lineSeparator = System.getProperty("line.separator");

	public MesgGPXWriter(PrintStream pstream) {
		XMLOutputFactory outputFactory = XMLOutputFactory.newInstance();
		try {
			this.stream = pstream;
			this.out = outputFactory.createXMLStreamWriter(stream, "utf-8");
			writeHeader();
		} catch (XMLStreamException e) {
			throw new RuntimeException("Problem creating xml output file", e);
		}

	}

	public void close() {
		try {
			writeFooter();
			out.close();
		} catch (XMLStreamException e) {
			throw new RuntimeException("Problem closing xml output file", e);
		}
	}

	/*
	 * Search the field with name fieldName in the list of fields.
	 */
	private Field getField(String fieldName, Collection<Field> fields) {
		for (Iterator<Field> iterator = fields.iterator(); iterator.hasNext();) {
			Field field = (Field) iterator.next();
			if (fieldName.equals(field.getName()))
				return field;
		}
		return null; // not found
	}

	private String getStringValues(Field field) {
		String value = null;
		if (field != null) {
			value = field.getStringValue(0);
			for (int fieldElement = 1; fieldElement < field.getNumValues(); fieldElement++) {
				value += "|" + field.getStringValue(fieldElement);
			}
		}
		return value;
	}

	public void onMesg(Mesg mesg) {
		if ((mesg != null) && ("lap".equals(mesg.getName()))) {
			try {
				writeTrackSegment();
			} catch (XMLStreamException e) {
				throw new RuntimeException("Problem during writing trkseg", e);
			}
		} else if ((mesg != null) && ("record".equals(mesg.getName()))) {

			// Search relevant info
			Collection<Field> fields = mesg.getFields();
			Field posLat = getField("position_lat", fields);
			Field posLong = getField("position_long", fields);
			Field hr = getField("heart_rate", fields);
			Field time = getField("timestamp", fields);
			Field elevation = getField("altitude", fields);

			// Convert semicircles latitude to decimals
			BigDecimal posLatDec = Convertor.semicircleToDms(posLat);
			BigDecimal posLongDec = Convertor.semicircleToDms(posLong);

			try {
				writeTrackPoint(posLatDec, posLongDec, getStringValues(elevation), time, getStringValues(hr));
			} catch (XMLStreamException e) {
				throw new RuntimeException("Problem during writing trackpoint", e);
			}

			// Iterator<Field> fieldsIterator = fields.iterator();
			//
			// while (fieldsIterator.hasNext()) {
			// Field field = fieldsIterator.next();
			//
			// if ("speed".equals(field.getName()) ||
			// "distance".equals(field.getName())
			// || "heart_rate".equals(field.getName()) ||
			// "timestamp".equals(field.getName())
			// || "position_lat".equals(field.getName()) ||
			// "position_long".equals(field.getName())) {
			// continue;
			// }
			//
			// }

		}
	}

	public void onMesgDefinition(MesgDefinition mesgDef) {
	}

	private void writeFooter() throws XMLStreamException {
		out.writeEndElement(); // end trkseg
		out.writeEndElement(); // end trk
		out.writeEndElement(); // end gpx
		out.writeEndDocument();
		inTrackSegment = true;
		numTrackPointInSegment = 0;
	}

	private void writeHeader() throws XMLStreamException {
		out.writeStartDocument();
		out.writeStartElement("gpx");
		out.writeAttribute("xmlns:tc2", "http://www.garmin.com/xmlschemas/TrainingCenterDatabase/v2");
		out.writeAttribute("xmlns:xsi", "http://www.w3.org/2001/XMLSchema-instance");
		out.writeAttribute("xmlns:gpxtpx", "http://www.garmin.com/xmlschemas/TrackPointExtension/v1");
		out.writeAttribute("xmlns", "http://www.topografix.com/GPX/1/1");
		out.writeAttribute("version", "1.1");
		out.writeAttribute("creator", "FitToGPXConverter");
		out.writeAttribute(
				"xsi:schemaLocation",
				"http://www.topografix.com/GPX/1/1 http://www.topografix.com/GPX/1/1/gpx.xsd http://www.garmin.com/xmlschemas/GpxExtensions/v3 http://www.garmin.com/xmlschemas/GpxExtensionsv3.xsd http://www.garmin.com/xmlschemas/TrackPointExtension/v1 http://www.garmin.com/xmlschemas/TrackPointExtensionv1.xsd");

		out.writeStartElement("trk");
		out.writeStartElement("name");
		out.writeCharacters("converted from fit file");
		out.writeEndElement();
		writeTrackSegment();
	}

	private void writeTrackSegment() throws XMLStreamException {

		if (inTrackSegment && numTrackPointInSegment > 0) {
			stream.append(lineSeparator); // add a new line for readability of the xml
			out.writeEndElement(); // close previous trkseg
			inTrackSegment = false;
		}
		if (!inTrackSegment) {
			stream.append(lineSeparator); // add a new line for readability of the xml
			out.writeStartElement("trkseg");
			out.writeCharacters(" ");
		}
		inTrackSegment = true;
		numTrackPointInSegment = 0;
	}

	/**
	 * Write a GPX trackpoint xml element.
	 * 
	 * @see http://www.topografix.com/gpx_manual.asp#trk
	 * 
	 *      All coordinates are relative to the WGS84 datum
	 * 
	 * @param lat
	 *            Latitude of the trackpoint in Decimal degrees, WGS84, ...
	 *            lat="42.323" ...
	 * @param elevation
	 *            Elevation of the trackpoint.
	 * @param time
	 * @param hr
	 * @throws XMLStreamException
	 */
	private void writeTrackPoint(BigDecimal lat, BigDecimal lon, String elevation, Field time, String hr)
			throws XMLStreamException {

		// only write a trackpoint if it contains coordinates
		if ((lat != null) && (lon != null)) {
			numTrackPointInSegment++;
			stream.append(lineSeparator); // add a new line for readability of the xml

			out.writeStartElement("trkpt");
			out.writeAttribute("lat", lat.toPlainString());// 51.0919834
			out.writeAttribute("lon", lon.toPlainString());
			if (elevation != null) {
				out.writeStartElement("ele");
				out.writeCharacters(elevation);// 16.2000000
				out.writeEndElement(); // end ele
			}
			if (time != null) {
				out.writeStartElement("time");
				out.writeCharacters(Convertor.convertTime(time));
				out.writeEndElement(); // end time
			}

			if (hr != null) {
				out.writeStartElement("extensions");
				out.writeStartElement("gpxtpx:TrackPointExtension");
				out.writeStartElement("gpxtpx:hr");
				out.writeCharacters(hr);
				out.writeEndElement(); // end hr
				out.writeEndElement(); // end gpxtpx:TrackPointExtension
				out.writeEndElement(); // end extensions
			}

			out.writeEndElement(); // end trkpt
		}
	}

}
