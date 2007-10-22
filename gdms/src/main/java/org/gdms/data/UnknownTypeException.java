/*
 * The GDMS library (Generic Datasources Management System)
 * is a middleware dedicated to the management of various kinds of
 * data-sources such as spatial vectorial data or alphanumeric. Based
 * on the JTS library and conform to the OGC simple feature access
 * specifications, it provides a complete and robust API to manipulate
 * in a SQL way remote DBMS (PostgreSQL, H2...) or flat files (.shp,
 * .csv...). GDMS is produced  by the geomatic team of the IRSTV
 * Institute <http://www.irstv.cnrs.fr/>, CNRS FR 2488:
 *    Erwan BOCHER, scientific researcher,
 *    Thomas LEDUC, scientific researcher,
 *    Fernando GONZALES CORTES, computer engineer.
 *
 * Copyright (C) 2007 Erwan BOCHER, Fernando GONZALES CORTES, Thomas LEDUC
 *
 * This file is part of GDMS.
 *
 * GDMS is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * GDMS is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with GDMS. If not, see <http://www.gnu.org/licenses/>.
 *
 * For more information, please consult:
 *    <http://orbisgis.cerma.archi.fr/>
 *    <http://sourcesup.cru.fr/projects/orbisgis/>
 *    <http://listes.cru.fr/sympa/info/orbisgis-developers/>
 *    <http://listes.cru.fr/sympa/info/orbisgis-users/>
 *
 * or contact directly:
 *    erwan.bocher _at_ ec-nantes.fr
 *    fergonco _at_ gmail.com
 *    thomas.leduc _at_ cerma.archi.fr
 */
package org.gdms.data;

/**
 * Excepci�n que se d� cuando un driver devuelve un tipo de datos no reconocido
 * 
 * @author Fernando Gonz�lez Cort�s
 */
public class UnknownTypeException extends Exception {
	/**
	 * Creates a new UnknownTypeException object.
	 */
	public UnknownTypeException() {
		super();
	}

	/**
	 * Creates a new UnknownTypeException object.
	 * 
	 * @param arg0
	 */
	public UnknownTypeException(String arg0) {
		super(arg0);
	}

	/**
	 * Creates a new UnknownTypeException object.
	 * 
	 * @param arg0
	 */
	public UnknownTypeException(Throwable arg0) {
		super(arg0);
	}

	/**
	 * Creates a new UnknownTypeException object.
	 * 
	 * @param arg0
	 * @param arg1
	 */
	public UnknownTypeException(String arg0, Throwable arg1) {
		super(arg0, arg1);
	}
}
