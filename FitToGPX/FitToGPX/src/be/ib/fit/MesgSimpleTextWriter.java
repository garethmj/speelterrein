////////////////////////////////////////////////////////////////////////////////
// The following FIT Protocol software provided may be used with FIT protocol

package be.ib.fit;

import java.io.PrintStream;
import java.util.Collection;
import java.util.Iterator;

import com.garmin.fit.Factory;
import com.garmin.fit.Field;
import com.garmin.fit.FieldDefinition;
import com.garmin.fit.Fit;
import com.garmin.fit.Mesg;
import com.garmin.fit.MesgDefinition;
import com.garmin.fit.MesgDefinitionListener;
import com.garmin.fit.MesgListener;
import com.garmin.fit.csv.MesgCSVWriter;

/**
 * 
 * @author Ivan
 * @see MesgCSVWriter
 */
public class MesgSimpleTextWriter implements MesgListener, MesgDefinitionListener {
	private PrintStream outFile;

	public MesgSimpleTextWriter(PrintStream stream) {
		this.outFile = stream;
	}

	public void close() {
		outFile.close();
	}

	public void onMesgDefinition(MesgDefinition mesgDef) {
		// Mesg mesg = Factory.createMesg(mesgDef.getNum());
		// outFile.print(mesg == null ? "unknown" : mesg.getName());
		//
		// Collection<FieldDefinition> fields = mesgDef.getFields();
		// Iterator<FieldDefinition> fieldsIterator = fields.iterator();
		//
		// while (fieldsIterator.hasNext()) {
		// FieldDefinition fieldDef = fieldsIterator.next();
		// Field field = Factory.createField(mesgDef.getNum(),
		// fieldDef.getNum());
		//
		// outFile.print(";");
		// outFile.print(field.getName());
		// outFile.print( " " + getFieldStringValues(field) + " " +
		// field.getUnits());
		//
		// }
		// outFile.println();
	}

	private void printSpecialField(Field field) {
		if (field != null) {
			outFile.printf(";%s %s", getFieldStringValues(field), field.getUnits());
		}
	}

	public void onMesg(Mesg mesg) {
		outFile.print(mesg == null ? "unknown" : mesg.getName());
		if (mesg == null) {
			outFile.println();
			return;
		}

		Collection<Field> fields = mesg.getFields();

		// Handle some special fields ===========================
		Field posLat = getField("position_lat", fields);
		Field posLong = getField("position_long", fields);
		if (posLat != null) {
			outFile.printf(";lat %s", getFieldStringValues(posLat), "semicircles".equals(posLat.getUnits()) ? ""
					: posLat.getUnits());
		}
		if (posLong != null) {
			outFile.printf(";lon %s", getFieldStringValues(posLong), "semicircles".equals(posLong.getUnits()) ? ""
					: posLong.getUnits());
		}

		printSpecialField(getField("speed", fields));
		printSpecialField(getField("distance", fields));
		printSpecialField(getField("heart_rate", fields));
		printSpecialField(getField("timestamp", fields));

		Iterator<Field> fieldsIterator = fields.iterator();

		while (fieldsIterator.hasNext()) {
			Field field = fieldsIterator.next();

			if ("speed".equals(field.getName()) || "distance".equals(field.getName())
					|| "heart_rate".equals(field.getName()) || "timestamp".equals(field.getName())
					|| "position_lat".equals(field.getName()) || "position_long".equals(field.getName())) {
				continue;
			}

			outFile.print(";" + field.getName());

			outFile.print(" " + getFieldStringValues(field) + " " + field.getUnits());
		}

		outFile.println();
	}

	private String getFieldStringValues(Field field) {
		String value = field.getStringValue(0);
		for (int fieldElement = 1; fieldElement < field.getNumValues(); fieldElement++) {
			value += "|" + field.getStringValue(fieldElement);
		}
		return value;
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

}
