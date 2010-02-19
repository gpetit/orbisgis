/*
 * OrbisGIS is a GIS application dedicated to scientific spatial simulation.
 * This cross-platform GIS is developed at French IRSTV institute and is able
 * to manipulate and create vector and raster spatial information. OrbisGIS
 * is distributed under GPL 3 license. It is produced  by the geo-informatic team of
 * the IRSTV Institute <http://www.irstv.cnrs.fr/>, CNRS FR 2488:
 *    Erwan BOCHER, scientific researcher,
 *    Thomas LEDUC, scientific researcher,
 *    Fernando GONZALEZ CORTES, computer engineer.
 *
 * Copyright (C) 2009 Erwan BOCHER, Pierre-yves FADET
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
 *
 * or contact directly:
 *    erwan.bocher _at_ ec-nantes.fr
 *    Pierre-Yves.Fadet_at_ec-nantes.fr
 *    thomas.leduc _at_ cerma.archi.fr
 */

package org.orbisgis.core.ui.plugins.toc;

import java.util.Observable;

import javax.swing.JOptionPane;

import org.gdms.data.NonEditableDataSourceException;
import org.gdms.driver.DriverException;
import org.orbisgis.core.Services;
import org.orbisgis.core.images.IconNames;
import org.orbisgis.core.layerModel.ILayer;
import org.orbisgis.core.layerModel.MapContext;
import org.orbisgis.core.ui.pluginSystem.AbstractPlugIn;
import org.orbisgis.core.ui.pluginSystem.PlugInContext;
import org.orbisgis.core.ui.pluginSystem.workbench.Names;
import org.orbisgis.core.ui.pluginSystem.workbench.WorkbenchContext;
import org.orbisgis.core.ui.pluginSystem.workbench.WorkbenchFrame;

public class SetInactivePlugIn extends AbstractPlugIn {

	public boolean execute(PlugInContext context) throws Exception {
		getPlugInContext().executeLayers();
		return true;
	}

	public void initialize(PlugInContext context) throws Exception {
		WorkbenchContext wbContext = context.getWorkbenchContext();
		WorkbenchFrame frame = wbContext.getWorkbench().getFrame().getToc();
		context.getFeatureInstaller().addPopupMenuItem(frame, this,
				new String[] { Names.POPUP_TOC_INACTIVE_PATH1 },
				Names.POPUP_TOC_INACTIVE_GROUP, false,
				getIcon(IconNames.POPUP_TOC_INACTIVE_ICON), wbContext);
	}

	public void update(Observable o, Object arg) {
	}

	public void execute(MapContext mapContext, ILayer layer) {
		int option = JOptionPane.showConfirmDialog(null,
				"Do you want to save your changes", "Stop edition",
				JOptionPane.YES_NO_CANCEL_OPTION);
		if (option == JOptionPane.YES_OPTION) {
			try {
				mapContext.getActiveLayer().getDataSource().commit();
			} catch (DriverException e) {
				Services.getErrorManager().error("Cannot save layer", e);
			} catch (NonEditableDataSourceException e) {
				Services.getErrorManager().error("This layer cannot be saved",
						e);
			}
		}

		if (option != JOptionPane.CANCEL_OPTION) {
			mapContext.setActiveLayer(null);
		}
	}

	public boolean isEnabled() {
		return true;
	}

	public boolean isVisible() {
		return getPlugInContext().checkLayerAvailability();
	}

	public boolean accepts(MapContext mc, ILayer layer) {
		return mc.getActiveLayer() == layer;
	}

	public boolean acceptsSelectionCount(int selectionCount) {
		return selectionCount == 1;
	}
}
