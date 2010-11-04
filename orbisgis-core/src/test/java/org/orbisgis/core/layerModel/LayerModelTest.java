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
package org.orbisgis.core.layerModel;

import java.io.File;

import org.gdms.data.DataSource;
import org.gdms.data.DataSourceFactory;
import org.gdms.data.types.Type;
import org.gdms.data.types.TypeFactory;
import org.gdms.driver.generic.GenericObjectDriver;
import org.gdms.source.SourceManager;
import org.grap.model.GeoRaster;
import org.orbisgis.core.AbstractTest;
import org.orbisgis.core.DataManager;
import org.orbisgis.core.Services;
import org.orbisgis.core.renderer.legend.carto.LegendFactory;

public class LayerModelTest extends AbstractTest {

	private DataSourceFactory dsf = new DataSourceFactory();
	private DataSource dummy;
	private DataSource dummy2;
	private DataSource dummy3;

	@Override
	protected void setUp() throws Exception {
		GenericObjectDriver omd = new GenericObjectDriver(
				new String[] { "the_geom" }, new Type[] { TypeFactory
						.createType(Type.GEOMETRY) });
		dsf.getSourceManager().register("vector1", omd);
		dummy = dsf.getDataSource(omd);
		omd = new GenericObjectDriver();
		dsf.getSourceManager().register("vector2", omd);
		dummy2 = dsf.getDataSource("vector2");
		omd = new GenericObjectDriver();
		dsf.getSourceManager().register("vector3", omd);
		dummy3 = dsf.getDataSource("vector3");
		super.setUp();
		super.registerDataManager();
	}

	public void testTreeExploring() throws Exception {
		MapContext mc=new DefaultMapContext();
		mc.open(null);
		ILayer vl = getDataManager().createLayer((DataSource) dummy,mc);
		ILayer rl = getDataManager().createLayer("my tiff",
				new File("src/test/resources/data/ace.tiff"),mc);
		LayerCollection lc = getDataManager().createLayerCollection("my data",mc);
		lc.addLayer(vl);
		lc.addLayer(rl);

		IDisplayable layer = lc;
		if (layer instanceof LayerCollection) {
			lc.getChildren();
		} else {
			if (layer.getDataSource().isDefaultRaster()) {
				GeoRaster fc = layer.getDataSource().getRaster(0);
				assertTrue(fc != null);
			} else if (layer.getDataSource().isDefaultVectorial()) {
				DataSource fc = layer.getDataSource();
				assertTrue(fc != null);
			}
		}
	}

	public void testLayerEvents() throws Exception {
		MapContext mc=new DefaultMapContext();
		mc.open(null);
		TestLayerListener listener = new TestLayerListener();
		ILayer vl = getDataManager().createLayer((DataSource) dummy,mc);
		LayerCollection lc = getDataManager().createLayerCollection("root",mc);
		vl.addLayerListener(listener);
		lc.addLayerListener(listener);
		ILayer vl1 = getDataManager().createLayer((DataSource) dummy,mc);
		lc.addLayer(vl1);
		assertTrue(listener.la == 1);
		lc.setName("new name");
		assertTrue(listener.nc == 1);
		lc.setVisible(false);
		assertTrue(listener.vc == 1);
		vl.open();
		int refsc = listener.sc;
		vl.setLegend(LegendFactory.createUniqueSymbolLegend());
		assertTrue(listener.sc == refsc + 1);
		lc.remove(vl1.getName());
		assertTrue(listener.lr == 1);
		assertTrue(listener.lring == 1);
		assertTrue(lc.getLayerCount() == 0);
		vl.close();
	}

	public void testLayerRemovalCancellation() throws Exception {
		MapContext mc=new DefaultMapContext();
		mc.open(null);
		TestLayerListener listener = new TestLayerListener() {
			@Override
			public boolean layerRemoving(LayerCollectionEvent arg0) {
				return false;
			}
		};
		ILayer vl = getDataManager().createLayer((DataSource) dummy, mc);
		LayerCollection lc = getDataManager().createLayerCollection("root", mc);
		lc.addLayer(vl);
		lc.addLayerListener(listener);
		assertTrue(lc.remove(vl) == null);
		assertTrue(lc.remove(vl.getName()) == null);
		assertTrue(lc.remove(vl, false) == null);
		assertTrue(lc.remove(vl, true) != null);
	}

	public void testRepeatedName() throws Exception {
		MapContext mc=new DefaultMapContext();
		mc.open(null);
		DataSourceFactory dsf = ((DataManager) Services
				.getService(DataManager.class)).getDataSourceFactory();
		SourceManager sourceManager = dsf.getSourceManager();
		sourceManager.register("vector1", new File("/tmp/1.shp"));
		sourceManager.register("vector2", new File("/tmp/2.shp"));
		sourceManager.register("vector3", new File("/tmp/3.shp"));
		LayerCollection lc1 = getDataManager().createLayerCollection("firstLevel", mc);
		LayerCollection lc2 = getDataManager().createLayerCollection("secondLevel", mc);
		LayerCollection lc3 = getDataManager().createLayerCollection("thirdLevel", mc);
		ILayer vl1 = getDataManager().createLayer(dummy, mc);
		ILayer vl2 = getDataManager().createLayer(dummy2, mc);
		ILayer vl3 = getDataManager().createLayer(dummy3, mc);
		lc1.addLayer(vl1);
		lc2.addLayer(vl2);
//		lc1.addLayer(lc2);
		lc3.addLayer(vl3);
//		lc2.addLayer(lc3);
		try {
			vl3.setName(dummy2.getName());
			assertTrue(false);
		} catch (LayerException e) {
		}
		assertTrue(!vl3.getName().equals("vector2"));
		vl3.setName("firstLevel");
		assertTrue(!vl3.getName().equals("firstLevel"));
		lc1.setName("vector2");
		assertTrue(!lc1.getName().equals("vector2"));
	}

	public void testAddWithSameName() throws Exception {
		MapContext mc=new DefaultMapContext();
		mc.open(null);
		DataSourceFactory dsf = ((DataManager) Services
				.getService(DataManager.class)).getDataSourceFactory();
		SourceManager sourceManager = dsf.getSourceManager();
		sourceManager.register("mySource", new File(
				"src/test/resources/data/bv_sap.shp"));
		LayerCollection lc = getDataManager().createLayerCollection("firstLevel", mc);
		ILayer vl1 = getDataManager().createLayer("mySource", mc);
		ILayer vl2 = getDataManager().createLayer("mySource", mc);
		lc.addLayer(vl1);
		lc.addLayer(vl2);
		assertTrue(!vl1.getName().equals(vl2.getName()));

	}

	//This test is not useful anymore, as we are not managing an actual tree anymore...
//	public void testAddToChild() throws Exception {
//		LayerCollection lc1 = getDataManager().createLayerCollection("firstLevel", mc);
//		LayerCollection lc2 = getDataManager().createLayerCollection("secondLevel", mc);
//		LayerCollection lc3 = getDataManager().createLayerCollection("thirdLevel", mc);
//		LayerCollection lc4 = getDataManager().createLayerCollection("fourthLevel", mc);
//		lc1.addLayer(lc2);
//		lc2.addLayer(lc3);
//		lc3.addLayer(lc4);
//		try {
//			lc2.moveTo(lc4);
//			assertTrue(false);
//		} catch (LayerException e) {
//		}
//
//		TestLayerListener listener = new TestLayerListener();
//		lc1.addLayerListenerRecursively(listener);
//		lc3.moveTo(lc1);
//		assertTrue(lc3.getParent() == lc1);
//		assertTrue(lc2.getChildren().length == 0);
//		assertTrue(listener.la == 0);
//		assertTrue(listener.lr == 0);
//		assertTrue(listener.lring == 0);
//		assertTrue(listener.lm == 1);
//	}

	public void testContainsLayer() throws Exception {
		MapContext mc=new DefaultMapContext();
		mc.open(null);
		LayerCollection l2 = getDataManager().createLayerCollection("secondlevel", mc);
		ILayer vl1 = getDataManager().createLayer(dummy, mc);
		l2.addLayer(vl1);
		assertTrue(mc.getAllLayersNames().contains(vl1.getName()));
	}

	public void testGetLayerByName() throws Exception {
		MapContext mc=new DefaultMapContext();
		mc.open(null);
		LayerCollection l2 = getDataManager().createLayerCollection("secondlevel",mc);
		LayerCollection l3 = getDataManager().createLayerCollection("secondlevelbis",mc);
		ILayer vl1 = getDataManager().createLayer(dummy,mc);
		l2.addLayer(vl1);

		assertTrue(mc.getLayerByName("secondlevel") == l2);
		assertTrue(mc.getLayerByName("secondlevelbis") == l3);
		assertTrue(mc.getLayerByName(dummy.getName()) == vl1);
	}

	private class TestLayerListener implements LayerListener {

		private int nc = 0;

		private int vc = 0;

		private int la = 0;

		private int lm = 0;

		private int lr = 0;

		private int lring = 0;

		private int sc = 0;

		public void nameChanged(LayerListenerEvent e) {
			nc++;
		}

		public void visibilityChanged(LayerListenerEvent e) {
			vc++;
		}

		public void layerAdded(LayerCollectionEvent listener) {
			la++;
		}

		public void layerMoved(LayerCollectionEvent listener) {
			lm++;
		}

		public void layerRemoved(LayerCollectionEvent listener) {
			lr++;
		}

		public void styleChanged(LayerListenerEvent e) {
			sc++;
		}

		public void selectionChanged(SelectionEvent e) {
		}

		@Override
		public boolean layerRemoving(LayerCollectionEvent layerCollectionEvent) {
			lring++;
			return true;
		}
	}
}