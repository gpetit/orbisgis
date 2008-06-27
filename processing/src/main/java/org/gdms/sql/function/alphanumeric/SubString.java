/*
 * OrbisGIS is a GIS application dedicated to scientific spatial simulation.
 * This cross-platform GIS is developed at French IRSTV institute and is able
 * to manipulate and create vector and raster spatial information. OrbisGIS
 * is distributed under GPL 3 license. It is produced  by the geo-informatic team of
 * the IRSTV Institute <http://www.irstv.cnrs.fr/>, CNRS FR 2488:
 *    Erwan BOCHER, scientific researcher,
 *    Thomas LEDUC, scientific researcher,
 *    Fernando GONZALEZ CORTES, computer engineer.
 *
 * Copyright (C) 2007 Erwan BOCHER, Fernando GONZALEZ CORTES, Thomas LEDUC
 *
 * This file is part of OrbisGIS.
 *
 * OrbisGIS is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * OrbisGIS is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with OrbisGIS. If not, see <http://www.gnu.org/licenses/>.
 *
 * For more information, please consult:
 *    <http://orbisgis.cerma.archi.fr/>
 *    <http://sourcesup.cru.fr/projects/orbisgis/>
 *
 * or contact directly:
 *    erwan.bocher _at_ ec-nantes.fr
 *    fergonco _at_ gmail.com
 *    thomas.leduc _at_ cerma.archi.fr
 */
package org.gdms.sql.function.alphanumeric;

import org.gdms.data.types.InvalidTypeException;
import org.gdms.data.types.Type;
import org.gdms.data.types.TypeFactory;
import org.gdms.data.values.Value;
import org.gdms.data.values.ValueFactory;
import org.gdms.sql.function.Function;
import org.gdms.sql.function.FunctionException;
import org.gdms.sql.function.FunctionValidator;
import org.gdms.sql.strategies.IncompatibleTypesException;

public class SubString implements Function {

	public Value evaluate(Value[] arg0) throws FunctionException {
		// Return a null value is the two arguments are null
		if ((arg0[0].isNull()) || (arg0[1].isNull())) {
			return ValueFactory.createNullValue();
		} else {
			// Get the argument
			final String text = arg0[0].getAsString();
			final int firstRightString = arg0[1].getAsInt();
			String newText = null;
			if (arg0.length == 3) {
				final int secondRightString = arg0[2].getAsInt();
				// The substring with two arguments
				newText = text.substring(firstRightString, secondRightString);

			} else {
				// The substring with one argument
				if (text.length()< firstRightString){
					newText = text;
				}
				else {
				newText = text.substring(firstRightString);
				}
			}
			
			if (newText!=null){
				return ValueFactory.createValue(newText);
			}
			else  {
				return ValueFactory.createNullValue();
			}
			
		}

	}

	public String getDescription() {

		return "Extract a substring. Arguments = right digits ";
	}

	public String getName() {

		return "SubString";
	}

	public String getSqlOrder() {

		return "select substring(text, integer[, integer]) from myTable";
	}

	public Type getType(Type[] arg0) throws InvalidTypeException {
		
		return TypeFactory.createType(Type.STRING);
	}

	public boolean isAggregate() {
		return false;
	}

	public void validateTypes(Type[] argumentsTypes)
			throws IncompatibleTypesException {
		// At leat two arguments must be used and the third is an option.
		FunctionValidator
				.failIfBadNumberOfArguments(this, argumentsTypes, 2, 3);
		// The first argument must be a text
		FunctionValidator.failIfNotOfType(this, argumentsTypes[0], Type.STRING);
		// The second argument must be a numeric
		FunctionValidator.failIfNotNumeric(this, argumentsTypes[1]);
		if (argumentsTypes.length == 3) {
			// The third argument must be a numeric
			FunctionValidator.failIfNotNumeric(this, argumentsTypes[2]);
		}
	}

}
