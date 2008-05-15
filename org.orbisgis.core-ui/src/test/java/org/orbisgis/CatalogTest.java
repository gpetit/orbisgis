/*
 * OrbisGIS is a GIS application dedicated to scientific spatial simulation.
 * This cross-platform GIS is developed at french IRSTV institute and is able
 * to manipulate and create vectorial and raster spatial information. OrbisGIS
 * is distributed under GPL 3 license. It is produced  by the geomatic team of
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
 *    <http://listes.cru.fr/sympa/info/orbisgis-developers/>
 *    <http://listes.cru.fr/sympa/info/orbisgis-users/>
 *
 * or contact directly:
 *    erwan.bocher _at_ ec-nantes.fr
 *    fergonco _at_ gmail.com
 *    thomas.leduc _at_ cerma.archi.fr
 */
package org.orbisgis;

import org.gdms.data.DataSourceFactory;
import org.gdms.data.file.FileSourceDefinition;
import org.orbisgis.resource.IResource;
import org.orbisgis.views.geocatalog.Catalog;
import org.orbisgis.views.geocatalog.ResourceTreeModel;

public class CatalogTest extends AbstractTest {

	public void testResourcesModifedInDSF() throws Exception {
		Catalog cat = new Catalog();
		DataSourceFactory dsf = ((DataManager) Services
				.getService("org.orbisgis.DataManager")).getDSF();
		dsf.registerDataSource("source", new FileSourceDefinition("a.csv"));
		assertTrue(cat.getTreeModel().getRoot().getResourceAt(0).getName()
				.equals("source"));
		dsf.getSourceManager().rename("source", "source2");
		assertTrue(cat.getTreeModel().getRoot().getResourceAt(0).getName()
				.equals("source2"));
		dsf.remove("source2");
		assertTrue(cat.getTreeModel().getRoot().getChildCount() == 0);
	}

	public void testResourcesModifiedInCatalog() throws Exception {
		Catalog cat = new Catalog();
		ResourceTreeModel model = cat.getTreeModel();
		DataSourceFactory dsf = ((DataManager) Services
				.getService("org.orbisgis.DataManager")).getDSF();
		dsf.registerDataSource("source", new FileSourceDefinition("a.csv"));
		IResource res = model.getRoot().getResourceAt(0);
		res.setResourceName("source2");
		assertTrue(dsf.exists("source2"));
		assertTrue(!dsf.exists("source"));
		res.getParentResource().removeResource(res);
		assertTrue(!dsf.exists("source2"));
	}
}
