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
package org.orbisgis.core.ui.editors.map.tools;

import java.awt.Color;
import java.awt.Graphics;
import java.text.DecimalFormat;
import java.util.Observable;

import javax.swing.AbstractButton;

import org.orbisgis.core.Services;
import org.orbisgis.core.layerModel.MapContext;
import org.orbisgis.core.ui.editors.map.tool.DrawingException;
import org.orbisgis.core.ui.editors.map.tool.ToolManager;
import org.orbisgis.core.ui.editors.map.tool.TransitionException;
import org.orbisgis.core.ui.pluginSystem.PlugInContext;
import org.orbisgis.core.ui.plugins.views.OutputManager;
import org.orbisgis.utils.I18N;

import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.LineString;

public class MesureLineTool extends AbstractLineTool {

	AbstractButton button;

	public AbstractButton getButton() {
		return button;
	}

	public void setButton(AbstractButton button) {
		this.button = button;
	}

	public void update(Observable o, Object arg) {
		PlugInContext.checkTool(this);
	}

	public boolean isEnabled(MapContext vc, ToolManager tm) {
		return vc.getLayerCount() > 0;
	}

	public boolean isVisible(MapContext vc, ToolManager tm) {
		return true;
	}

	protected void lineDone(LineString ls, MapContext vc, ToolManager tm)
			throws TransitionException {
		OutputManager om = (OutputManager) Services
				.getService(OutputManager.class);
		Color color = Color.blue;
		om.print("Distance : " + getLength(ls) + "\n", color);
	}

	private String getLength(Geometry ls) {
		return new DecimalFormat("0.000").format(ls.getLength());
	}

	public void drawIn_Point(Graphics g, MapContext vc, ToolManager tm)
			throws DrawingException {
		super.drawIn_Point(g, vc, tm);
		Geometry geom = getCurrentLineString(vc, tm);
		tm.addTextToDraw(getLength(geom) + "");
	}

	public String getName() {
		return I18N.getText("orbisgis.core.ui.editors.map.tool.mesure.length");
	}
}