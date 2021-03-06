/**
 * OrbisGIS is a GIS application dedicated to scientific spatial simulation.
 * This cross-platform GIS is developed at French IRSTV institute and is able to
 * manipulate and create vector and raster spatial information.
 *
 * OrbisGIS is distributed under GPL 3 license. It is produced by the "Atelier SIG"
 * team of the IRSTV Institute <http://www.irstv.fr/> CNRS FR 2488.
 *
 * Copyright (C) 2007-1012 IRSTV (FR CNRS 2488)
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
 * or contact directly:
 * info_at_ orbisgis.org
 */
package org.orbisgis.core.layerModel;

import org.junit.Before;
import org.junit.Test;
import java.io.File;

import org.gdms.data.DataSource;
import org.gdms.data.DataSourceFactory;
import org.gdms.data.types.Type;
import org.gdms.data.types.TypeFactory;
import org.gdms.driver.driverManager.DriverManager;
import org.gdms.driver.memory.MemoryDataSetDriver;
import org.gdms.source.SourceManager;
import org.grap.model.GeoRaster;
import org.orbisgis.core.AbstractTest;
import org.orbisgis.core.DataManager;
import org.orbisgis.core.Services;
import org.orbisgis.core.renderer.legend.carto.LegendFactory;

import static org.junit.Assert.*;

public class LayerModelTest extends AbstractTest {

        private DataSourceFactory dsf = new DataSourceFactory();
        private DataSource dummy;
        private DataSource dummy2;
        private DataSource dummy3;

        @Override
        @Before
        public void setUp() throws Exception {
                MemoryDataSetDriver omd = new MemoryDataSetDriver(
                        new String[]{"the_geom"}, new Type[]{TypeFactory.createType(Type.GEOMETRY)});
                dsf.getSourceManager().register("vector1", omd);
                dummy = dsf.getDataSource(omd, DriverManager.DEFAULT_SINGLE_TABLE_NAME);
                omd = new MemoryDataSetDriver(
                        new String[]{"the_geom"}, new Type[]{TypeFactory.createType(Type.GEOMETRY)});
                dsf.getSourceManager().register("vector2", omd);
                dummy2 = dsf.getDataSource("vector2");
                omd = new MemoryDataSetDriver(
                        new String[]{"the_geom"}, new Type[]{TypeFactory.createType(Type.GEOMETRY)});
                dsf.getSourceManager().register("vector3", omd);
                dummy3 = dsf.getDataSource("vector3");
                super.setUp();
                super.registerDataManager();
        }

        @Test
        public void testTreeExploring() throws Exception {
                ILayer vl = getDataManager().createLayer((DataSource) dummy);
                ILayer rl = getDataManager().createLayer("my tiff",
                        new File("src/test/resources/data/ace.tiff"));
                ILayer lc = getDataManager().createLayerCollection("my data");
                lc.addLayer(vl);
                lc.addLayer(rl);

                ILayer layer = lc;
                if (layer instanceof LayerCollection) {
                        lc = (ILayer) layer;
                        lc.getChildren();
                } else {
                        if (layer.getDataSource().isRaster()) {
                                GeoRaster fc = layer.getDataSource().getRaster(0);
                                assertNotNull(fc);
                        } else if (layer.getDataSource().isVectorial()) {
                                DataSource fc = layer.getDataSource();
                                assertNotNull(fc);
                        }
                }
        }

        @Test
        public void testLayerEvents() throws Exception {
                TestLayerListener listener = new TestLayerListener();
                ILayer vl = getDataManager().createLayer((DataSource) dummy);
                ILayer lc = getDataManager().createLayerCollection("root");
                vl.addLayerListener(listener);
                lc.addLayerListener(listener);
                ILayer vl1 = getDataManager().createLayer((DataSource) dummy);
                lc.addLayer(vl1);
                assertEquals(listener.la, 1);
                lc.setName("new name");
                assertEquals(listener.nc, 1);
                lc.setVisible(false);
                assertEquals(listener.vc, 1);
                vl.open();
                int refsc = listener.sc;
                vl.setLegend(LegendFactory.createUniqueSymbolLegend());
                assertEquals(listener.sc, refsc + 1);
                lc.remove(vl1.getName());
                assertEquals(listener.lr, 1);
                assertEquals(listener.lring, 1);
                assertEquals(lc.getLayerCount(), 0);
                vl.close();
        }

        @Test
        public void testLayerRemovalCancellation() throws Exception {
                TestLayerListener listener = new TestLayerListener() {

                        @Override
                        public boolean layerRemoving(LayerCollectionEvent arg0) {
                                return false;
                        }
                };
                ILayer vl = getDataManager().createLayer((DataSource) dummy);
                ILayer lc = getDataManager().createLayerCollection("root");
                lc.addLayer(vl);
                lc.addLayerListener(listener);
                assertNull(lc.remove(vl));
                assertNull(lc.remove(vl.getName()));
                assertNull(lc.remove(vl, false));
                assertNotNull(lc.remove(vl, true));
        }

        @Test
        public void testRepeatedName() throws Exception {
                DataSourceFactory dsf = ((DataManager) Services.getService(DataManager.class)).getDataSourceFactory();
                SourceManager sourceManager = dsf.getSourceManager();
                sourceManager.register("vector1", new File("/tmp/1.shp"));
                sourceManager.register("vector2", new File("/tmp/2.shp"));
                sourceManager.register("vector3", new File("/tmp/3.shp"));
                ILayer lc1 = getDataManager().createLayerCollection("firstLevel");
                ILayer lc2 = getDataManager().createLayerCollection("secondLevel");
                ILayer lc3 = getDataManager().createLayerCollection("thirdLevel");
                ILayer vl1 = getDataManager().createLayer(dummy);
                ILayer vl2 = getDataManager().createLayer(dummy2);
                ILayer vl3 = getDataManager().createLayer(dummy3);
                lc1.addLayer(vl1);
                lc2.addLayer(vl2);
                lc1.addLayer(lc2);
                lc3.addLayer(vl3);
                lc2.addLayer(lc3);
                try {
                        vl3.setName(dummy2.getName());
                        fail();
                } catch (LayerException e) {
                }
                assertFalse(vl3.getName().equals("vector2"));
                vl3.setName("firstLevel");
                assertFalse(vl3.getName().equals("firstLevel"));
                lc1.setName("vector2");
                assertFalse(lc1.getName().equals("vector2"));
        }

        @Test
        public void testAddWithSameName() throws Exception {
                DataSourceFactory dsf = ((DataManager) Services.getService(DataManager.class)).getDataSourceFactory();
                SourceManager sourceManager = dsf.getSourceManager();
                sourceManager.register("mySource", new File(
                        "src/test/resources/data/bv_sap.shp"));
                ILayer lc = getDataManager().createLayerCollection("firstLevel");
                ILayer vl1 = getDataManager().createLayer("mySource");
                ILayer vl2 = getDataManager().createLayer("mySource");
                lc.addLayer(vl1);
                lc.addLayer(vl2);
                assertFalse(vl1.getName().equals(vl2.getName()));

        }

        @Test
        public void testAddToChild() throws Exception {
                ILayer lc1 = getDataManager().createLayerCollection("firstLevel");
                ILayer lc2 = getDataManager().createLayerCollection("secondLevel");
                ILayer lc3 = getDataManager().createLayerCollection("thirdLevel");
                ILayer lc4 = getDataManager().createLayerCollection("fourthLevel");
                lc1.addLayer(lc2);
                lc2.addLayer(lc3);
                lc3.addLayer(lc4);
                try {
                        lc2.moveTo(lc4);
                        fail();
                } catch (LayerException e) {
                }

                TestLayerListener listener = new TestLayerListener();
                lc1.addLayerListenerRecursively(listener);
                lc3.moveTo(lc1);
                assertEquals(lc3.getParent(), lc1);
                assertEquals(lc2.getChildren().length, 0);
                assertEquals(listener.la, 0);
                assertEquals(listener.lr, 0);
                assertEquals(listener.lring, 0);
                assertEquals(listener.lm, 1);
        }

        @Test
        public void testContainsLayer() throws Exception {
                ILayer lc = getDataManager().createLayerCollection("root");
                ILayer l2 = getDataManager().createLayerCollection("secondlevel");
                ILayer vl1 = getDataManager().createLayer(dummy);
                lc.addLayer(l2);
                l2.addLayer(vl1);
                assertTrue(lc.getAllLayersNames().contains(vl1.getName()));
        }

        @Test
        public void testGetLayerByName() throws Exception {
                ILayer lc = getDataManager().createLayerCollection("root");
                ILayer l2 = getDataManager().createLayerCollection("secondlevel");
                ILayer l3 = getDataManager().createLayerCollection("secondlevelbis");
                ILayer vl1 = getDataManager().createLayer(dummy);
                l2.addLayer(vl1);
                lc.addLayer(l2);
                lc.addLayer(l3);

                assertEquals(lc.getLayerByName("secondlevel"), l2);
                assertEquals(lc.getLayerByName("secondlevelbis"), l3);
                assertEquals(lc.getLayerByName(dummy.getName()), vl1);
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