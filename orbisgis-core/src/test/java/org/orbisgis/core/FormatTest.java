/*
 * OrbisGIS is a GIS application dedicated to scientific spatial simulation.
 * This cross-platform GIS is developed at French IRSTV institute and is able to
 * manipulate and create vector and raster spatial information. OrbisGIS is
 * distributed under GPL 3 license. It is produced by the "Atelier SIG" team of
 * the IRSTV Institute <http://www.irstv.cnrs.fr/> CNRS FR 2488.
 *
 * 
 *  Team leader Erwan BOCHER, scientific researcher,
 * 
 *  User support leader : Gwendall Petit, geomatic engineer.
 *
 *
 * Copyright (C) 2007 Erwan BOCHER, Fernando GONZALEZ CORTES, Thomas LEDUC
 *
 * Copyright (C) 2010 Erwan BOCHER, Pierre-Yves FADET, Alexis GUEGANNO, Maxence LAURENT
 *
 * This file is part of OrbisGIS.
 *
 * OrbisGIS is free software: you can redistribute it and/or modify it under the
 * terms of the GNU General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any later
 * version.
 *
 * OrbisGIS is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR
 * A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with
 * OrbisGIS. If not, see <http://www.gnu.org/licenses/>.
 *
 * For more information, please consult: <http://www.orbisgis.org/>
 *
 * or contact directly:
 * erwan.bocher _at_ ec-nantes.fr
 * gwendall.petit _at_ ec-nantes.fr
 */
package org.orbisgis.core;

import java.io.File;

import org.gdms.source.SourceManager;
import org.orbisgis.core.layerModel.DefaultMapContext;
import org.orbisgis.core.layerModel.MapContext;

public class FormatTest extends AbstractTest {

	private SourceManager sourceManager;

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		super.registerDataManager();
		sourceManager = ((DataManager) Services.getService(DataManager.class))
				.getDataSourceFactory().getSourceManager();
		sourceManager.removeAll();
		
	}

	public void testTiff() throws Exception {
		MapContext mc = new DefaultMapContext();
		File file = new File("src/test/resources/data/ace.tiff");
		sourceManager.register("tiff", file);
		getDataManager().createLayer("tiff", mc);
	}

	public void testAsc() throws Exception {
		MapContext mc = new DefaultMapContext();
		File file = new File("src/test/resources/data/3x3.asc");
		sourceManager.register("asc", file);
		getDataManager().createLayer("asc", mc);
	}

	public void testShapefile() throws Exception {
		MapContext mc = new DefaultMapContext();
		File file = new File("src/test/resources/data/bv_sap.shp");
		sourceManager.register("shp", file);
		getDataManager().createLayer("shp", mc);
	}

}
