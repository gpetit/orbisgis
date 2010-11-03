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
 * Previous computer developer : Pierre-Yves FADET, computer engineer, Thomas LEDUC, scientific researcher, Fernando GONZALEZ
 * CORTES, computer engineer.
 *
 * Copyright (C) 2007 Erwan BOCHER, Fernando GONZALEZ CORTES, Thomas LEDUC
 *
 * Copyright (C) 2010 Erwan BOCHER, Alexis GUEGANNO, Maxence LAURENT
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
 * info@orbisgis.org
 */
package org.orbisgis.core.ui.plugins.toc;

import org.orbisgis.core.DataManager;
import org.orbisgis.core.Services;
import org.orbisgis.core.layerModel.IDisplayable;
import org.orbisgis.core.layerModel.ILayer;
import org.orbisgis.core.layerModel.LayerCollection;
import org.orbisgis.core.layerModel.LayerException;
import org.orbisgis.core.layerModel.MapContext;
import org.orbisgis.core.ui.pluginSystem.AbstractPlugIn;
import org.orbisgis.core.ui.pluginSystem.PlugInContext;
import org.orbisgis.core.ui.pluginSystem.PlugInContext.LayerAvailability;
import org.orbisgis.core.ui.pluginSystem.PlugInContext.SelectionAvailability;
import org.orbisgis.core.ui.pluginSystem.workbench.Names;
import org.orbisgis.core.ui.pluginSystem.workbench.WorkbenchContext;
import org.orbisgis.core.ui.pluginSystem.workbench.WorkbenchFrame;
import org.orbisgis.core.ui.preferences.lookandfeel.OrbisGISIcon;

/**
 * This plug-in is supposed to group all the selected layers in one group. It will
 * retrieve all the layers found in the selectedLayers attribute from the mapContext,
 * and group them in a new LayerCollection.
 * @author alexis
 */

public class GroupLayersPlugIn extends AbstractPlugIn {

	public boolean execute(PlugInContext context) throws Exception {

		MapContext mapContext = getPlugInContext().getMapContext();
		ILayer[] layers = mapContext.getSelectedLayers();

		DataManager dataManager = (DataManager) Services
				.getService(DataManager.class);
		IDisplayable col = dataManager.createLayerCollection("group"
				+ System.currentTimeMillis(), mapContext);
		try {
                    //col is an instance of LayerCollection.
			mapContext.add(col);
			for (ILayer layer : layers) {
				layer.moveTo((LayerCollection) col);
			}
		} catch (LayerException e) {
			throw new RuntimeException("A bug occured during the execution of GroupLayersPlugIn !");
		}

		return true;
	}

	public void initialize(PlugInContext context) throws Exception {
		WorkbenchContext wbContext = context.getWorkbenchContext();
		WorkbenchFrame frame = wbContext.getWorkbench().getFrame().getToc();
		context.getFeatureInstaller().addPopupMenuItem(frame, this,
				new String[] { Names.POPUP_TOC_LAYERS_GROUP_PATH1 },
				Names.POPUP_TOC_LAYERS_GROUP_GROUP, false,
				OrbisGISIcon.GROUP_LAYERS, wbContext);
	}

	public boolean isEnabled() {
		MapContext mapContext = getPlugInContext().getMapContext();
		ILayer[] layers = null;
		if (mapContext != null)
			layers = mapContext.getSelectedLayers();
		if(layers==null) return false;
		for (int i = 0; i < layers.length - 1; i++) {
			if (!layers[i].getParent().equals(layers[i + 1].getParent())) {
				return false;
			}
		}
		return getPlugInContext().checkLayerAvailability(
				new SelectionAvailability[]{ SelectionAvailability.SUPERIOR , SelectionAvailability.ACTIVE_MAPCONTEXT},
				1,
				new LayerAvailability[]{});
	}
}
