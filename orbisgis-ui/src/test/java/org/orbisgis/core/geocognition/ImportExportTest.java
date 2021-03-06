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
package org.orbisgis.core.geocognition;

import org.junit.Test;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

import org.orbisgis.core.layerModel.DefaultMapContext;

import static org.junit.Assert.*;

public class ImportExportTest extends AbstractGeocognitionTest {

        @Test
        public void testExportRootDoesNotCreateContainer() throws Exception {
                gc.addElement("A", new DefaultMapContext());
                ByteArrayOutputStream bos = new ByteArrayOutputStream();
                gc.write(bos, "/");
                GeocognitionElement imported = gc.createTree(new ByteArrayInputStream(
                        bos.toByteArray()));
                imported.setId(gc.getRoot().getId());
                equals(gc.getRoot(), imported);
        }

        private void equals(GeocognitionElement root, GeocognitionElement imported) {
                assertTrue(root.getId().equals(imported.getId()));
                if (root.isFolder()) {
                        assertEquals(root.getElementCount(), imported.getElementCount());
                        for (int i = 0; i < root.getElementCount(); i++) {
                                equals(root.getElement(i), imported.getElement(i));
                        }
                } else {
                        assertFalse(imported.isFolder());
                }
        }

        @Test
        public void testExportFolderDoesNotCreateContainer() throws Exception {
                gc.addFolder("A");
                gc.addElement("/A/Map", new DefaultMapContext());
                ByteArrayOutputStream bos = new ByteArrayOutputStream();
                gc.write(bos, "/A");
                GeocognitionElement imported = gc.createTree(new ByteArrayInputStream(
                        bos.toByteArray()));
                GeocognitionElement compareNode = gc.getGeocognitionElement("/A");
                imported.setId(compareNode.getId());
                equals(compareNode, imported);
        }

        @Test
        public void testExportLeaveCreateContainer() throws Exception {
                gc.addFolder("A");
                gc.addElement("/A/Map", new DefaultMapContext());
                ByteArrayOutputStream bos = new ByteArrayOutputStream();
                gc.write(bos, "/A/Map");
                GeocognitionElement imported = gc.createTree(new ByteArrayInputStream(
                        bos.toByteArray()));
                GeocognitionElement compareNode = gc.getGeocognitionElement("/A");
                imported.setId(compareNode.getId());
                equals(compareNode, imported);
        }
}
