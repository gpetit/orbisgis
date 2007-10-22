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
package org.gdms.data.db;

import org.gdms.SourceTest;
import org.gdms.data.DataSource;
import org.gdms.data.metadata.MetadataUtilities;

/**
 * DOCUMENT ME!
 * 
 * @author Fernando Gonzalez Cortes
 */
public class DataBaseTests extends SourceTest {
	/**
	 * Access to the PK field
	 * 
	 * @throws Exception
	 *             DOCUMENT ME!
	 */
	private void testPKAccess(String dsName) throws Exception {
		DataSource d = dsf.getDataSource(dsName);
		d.open();

		// String[] pks = d.getDataSourceMetadata().getPrimaryKey();
		String[] pks = MetadataUtilities.getPKNames(d.getMetadata());
		assertTrue(pks.length > 0);
		d.cancel();
	}

	public void testPKAccess() throws Exception {
		String[] resources = super.getResourcesWithPK();
		for (String ds : resources) {
			testPKAccess(ds);
		}
	}

	@Override
	protected void setUp() throws Exception {
		setWritingTests(false);
		super.setUp();
	}
}
