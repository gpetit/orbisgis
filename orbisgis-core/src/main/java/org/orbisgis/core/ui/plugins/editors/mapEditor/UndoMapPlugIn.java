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
package org.orbisgis.core.ui.plugins.editors.mapEditor;

import javax.swing.JButton;

import org.gdms.driver.DriverException;
import org.orbisgis.core.Services;
import org.orbisgis.core.layerModel.IDisplayable;
import org.orbisgis.core.layerModel.ILayer;
import org.orbisgis.core.layerModel.MapContext;
import org.orbisgis.core.ui.pluginSystem.AbstractPlugIn;
import org.orbisgis.core.ui.pluginSystem.PlugInContext;
import org.orbisgis.core.ui.pluginSystem.workbench.WorkbenchContext;
import org.orbisgis.core.ui.plugins.views.MapEditorPlugIn;
import org.orbisgis.core.ui.preferences.lookandfeel.OrbisGISIcon;

public class UndoMapPlugIn extends AbstractPlugIn {

	private JButton btn;

	public UndoMapPlugIn() {
		btn = new JButton(OrbisGISIcon.UNDO_ICON);
	}

	public boolean execute(PlugInContext context) throws Exception {
		MapEditorPlugIn mapEditor = (MapEditorPlugIn) getPlugInContext().getActiveEditor();
		MapContext mc = (MapContext) mapEditor.getElement().getObject();
		IDisplayable activeLayer = mc.getActiveLayer();
		try {
			activeLayer.getDataSource().undo();
		} catch (DriverException e) {
			Services.getErrorManager().error("Cannot undo", e);
		}
		return true;
	}

	public void initialize(PlugInContext context) throws Exception {
		WorkbenchContext wbcontext = context.getWorkbenchContext();
		wbcontext.getWorkbench().getFrame().getEditionMapToolBar().addPlugIn(
				this, btn, context);
	}

	public boolean isEnabled() {
		boolean isEnabled = false;
		MapEditorPlugIn mapEditor = null;
		if((mapEditor=getPlugInContext().getMapEditor()) != null){
			MapContext mc = (MapContext) mapEditor.getElement().getObject();
			IDisplayable activeLayer = mc.getActiveLayer();
			isEnabled =  (activeLayer != null) && activeLayer.getDataSource().canUndo();
		}
		btn.setEnabled(isEnabled);
		return isEnabled;
	}
}
