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
public class MesgVerboseTextWriter implements MesgListener, MesgDefinitionListener {
	private PrintStream outFile;

	public MesgVerboseTextWriter(PrintStream stream) {
		this.outFile = stream;
	}

	public void close() {
		outFile.close();
	}

	public void onMesgDefinition(MesgDefinition mesgDef) {
		Collection<FieldDefinition> fields = mesgDef.getFields();
		Iterator<FieldDefinition> fieldsIterator;
		int fieldNum;
		Mesg mesg = Factory.createMesg(mesgDef.getNum());

		// outFile.println("Type", "Definition");
		outFile.print("Local Number." + mesgDef.getLocalNum());

		if (mesg == null) {
			outFile.print(" Message:unknown");
		} else {
			outFile.print(" Message:" + mesg.getName());
		}

		fieldNum = 0;
		fieldsIterator = fields.iterator();

		while (fieldsIterator.hasNext()) {
			FieldDefinition fieldDef = fieldsIterator.next();
			Field field = Factory.createField(mesgDef.getNum(),
					fieldDef.getNum());
			fieldNum++;

			if (field == null) {
				outFile.print(" Field:" + fieldNum + ":unknown");
			} else {
				outFile.print(" Field:" + fieldNum + ":" + field.getName());
			}

			outFile.print(" Value:"
					+ fieldNum
					+ ":"
					+ (fieldDef.getSize() / Fit.baseTypeSizes[fieldDef
							.getType() & Fit.BASE_TYPE_NUM_MASK]));
			outFile.print(" Units:" + fieldNum);
		}
		outFile.println();
	}

	public void onMesg(Mesg mesg) {
		Collection<Field> fields = mesg.getFields();
		Iterator<Field> fieldsIterator;
		int fieldNum;

		// outFile.println("Type", "Data");
		outFile.print("Local Number:" + mesg.getLocalNum());
		outFile.print(" Message:" + mesg.getName());

		fieldNum = 0;
		fieldsIterator = fields.iterator();

		while (fieldsIterator.hasNext()) {
			Field field = fieldsIterator.next();

			fieldNum++;

			outFile.print(" Field " + fieldNum + ":" + field.getName());

			String value = field.getStringValue(0);
			for (int fieldElement = 1; fieldElement < field.getNumValues(); fieldElement++) {
				value += "|" + field.getStringValue(fieldElement);
			}
			outFile.print(" Value:" + fieldNum + ":" + value);

			outFile.print(" Units:" + fieldNum + ":" + field.getUnits());
		}

		outFile.println();
	}
}
