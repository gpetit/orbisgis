/**
 * The GDMS library (Generic Datasource Management System)
 * is a middleware dedicated to the management of various kinds of
 * data-sources such as spatial vectorial data or alphanumeric. Based
 * on the JTS library and conform to the OGC simple feature access
 * specifications, it provides a complete and robust API to manipulate
 * in a SQL way remote DBMS (PostgreSQL, H2...) or flat files (.shp,
 * .csv...). It is produced by the "Atelier SIG" team of
 * the IRSTV Institute <http://www.irstv.fr/> CNRS FR 2488.
 *
 *
 * Team leader : Erwan BOCHER, scientific researcher,
 *
 * User support leader : Gwendall Petit, geomatic engineer.
 *
 * Previous computer developer : Pierre-Yves FADET, computer engineer, Thomas LEDUC,
 * scientific researcher, Fernando GONZALEZ CORTES, computer engineer, Maxence LAURENT,
 * computer engineer.
 *
 * Copyright (C) 2007 Erwan BOCHER, Fernando GONZALEZ CORTES, Thomas LEDUC
 *
 * Copyright (C) 2010 Erwan BOCHER, Alexis GUEGANNO, Maxence LAURENT, Antoine GOURLAY
 *
 * Copyright (C) 2012 Erwan BOCHER, Antoine GOURLAY
 *
 * This file is part of Gdms.
 *
 * Gdms is free software: you can redistribute it and/or modify it under the
 * terms of the GNU General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any later
 * version.
 *
 * Gdms is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR
 * A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with
 * Gdms. If not, see <http://www.gnu.org/licenses/>.
 *
 * For more information, please consult: <http://www.orbisgis.org/>
 *
 * or contact directly:
 * info@orbisgis.org
 */
package org.gdms.data.edition;

import org.junit.Before;
import org.junit.Test;
import org.gdms.TestBase;
import org.gdms.data.DataSource;
import org.gdms.data.DataSourceFactory;
import org.gdms.data.memory.MemorySourceDefinition;
import org.gdms.driver.driverManager.DriverManager;

import static org.junit.Assert.*;

public class IsEditableTests extends TestBase {

	@Test
        public void testObject() throws Exception {
		DataSource ds = dsf.getDataSource("readObject");
		assertFalse(ds.isEditable());
		ds = dsf.getDataSource("readWriteObject");
		assertFalse(ds.isEditable());
		ReadDriver.isEditable = true;
		assertTrue(ds.isEditable());
	}

	@Test
        public void testFile() throws Exception {
		DataSource ds = dsf.getDataSource("readFile");
		assertFalse(ds.isEditable());
		ds = dsf.getDataSource("readWriteFile");
		assertFalse(ds.isEditable());
		ReadDriver.isEditable = true;
		assertTrue(ds.isEditable());
	}

	@Test
        public void testDB() throws Exception {
		DataSource ds = dsf.getDataSource("readDB");
		assertFalse(ds.isEditable());
		ds = dsf.getDataSource("readWriteDB");
		assertFalse(ds.isEditable());
		ReadDriver.isEditable = true;
		assertTrue(ds.isEditable());
	}

	@Before
	public void setUp() throws Exception {
		ReadDriver.initialize();

		super.setUpTestsWithoutEdition();
                
                DriverManager dm = new DriverManager();
		dm.registerDriver(ReadDriver.class);
		
                sm.setDriverManager(dm);

		sm.register("readObject", new MemorySourceDefinition(
				new ReadDriver(),"main"));
		sm.register("readWriteObject", new MemorySourceDefinition(
				new ReadAndWriteDriver(),"main"));
		sm.register("readFile", new FakeFileSourceDefinition(
				new ReadDriver()));
		sm.register("readWriteFile", new FakeFileSourceDefinition(
				new ReadAndWriteDriver()));
		sm.register("readDB", new FakeDBTableSourceDefinition(
				new ReadDriver(), "jdbc:executefailing"));
		sm.register("readWriteDB", new FakeDBTableSourceDefinition(
				new ReadAndWriteDriver(), "jdbc:closefailing"));
	}
}
