package org.gdms.sql.function;

import org.gdms.data.types.Type;
import org.gdms.data.values.Value;
import org.gdms.spatial.GeometryValue;

import com.vividsolutions.jts.geom.Geometry;

public class FunctionValidator {

	public static void failIfNotNull(Value... values) throws FunctionException {
		for (Value value : values) {
			if (value.getType() == Type.NULL) {
				throw new FunctionException("Cannot operate in null values");
			}
		}
	}

	public static void warnIfNotNull(Value... values) throws WarningException {
		for (Value value : values) {
			if (value.getType() == Type.NULL) {
				throw new WarningException("Cannot operate in null values");
			}
		}
	}

	public static void warnIfGeometryNotValid(Value... values)
			throws WarningException {
		for (Value value : values) {
			Geometry geom = ((GeometryValue) value).getGeom();
			if (!geom.isValid()) {
				throw new WarningException(geom.toText()
						+ " is not a valid geometry");
			}
		}
	}

	public static void failIfBadNumerOfArguments(Function function,
			Value[] args, int i) throws FunctionException {
		if (args.length != i) {
			throw new FunctionException("The function " + function.getName()
					+ " has a wrong number of arguments. " + i + " expected");
		}
	}

}
