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
package org.gdms.data.edition;

import java.io.Serializable;

import org.gdms.data.DataSource;
import org.gdms.data.metadata.Metadata;
import org.gdms.data.values.Value;
import org.gdms.data.values.ValueCollection;
import org.gdms.driver.DriverException;

public class OriginalDirection implements PhysicalDirection, Serializable {

	private transient DataSource source;
	private int row;

	public OriginalDirection(DataSource source, int row) {
		this.source = source;
		this.row = row;
	}

	public Value getFieldValue(int fieldId) throws DriverException {
		return source.getFieldValue(row, fieldId);
	}

	public ValueCollection getPK() throws DriverException {
		return source.getPK(row);
	}

	public int getRowIndex() {
		return row;
	}

	public void setSource(DataSource source) {
		this.source = source;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof OriginalDirection) {
			OriginalDirection od = (OriginalDirection) obj;
			return (od.source.getName().equals(source.getName()))
					&& (od.row == row);
		}

		return false;
	}

	@Override
	public int hashCode() {
		return source.hashCode() + row;
	}

	public Metadata getMetadata() throws DriverException {
		return source.getMetadata();
	}

}
