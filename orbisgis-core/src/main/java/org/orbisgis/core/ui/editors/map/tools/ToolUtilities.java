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

import java.text.ParseException;
import java.util.ArrayList;

import org.gdms.data.SpatialDataSourceDecorator;
import org.gdms.data.types.Constraint;
import org.gdms.data.types.Type;
import org.gdms.data.values.Value;
import org.gdms.driver.DriverException;
import org.orbisgis.core.layerModel.ILayer;
import org.orbisgis.core.layerModel.MapContext;
import org.orbisgis.core.sif.UIFactory;
import org.orbisgis.core.ui.components.sif.AskValidValue;
import org.orbisgis.core.ui.editors.map.tool.Automaton;
import org.orbisgis.core.ui.editors.map.tool.TransitionException;

import com.vividsolutions.jts.geom.Coordinate;
import org.orbisgis.core.layerModel.IDisplayable;

public class ToolUtilities {

	public static double getActiveLayerInitialZ(MapContext mapContext) {
		SpatialDataSourceDecorator sds = mapContext.getActiveLayer()
				.getDataSource();
		try {
			Type type = sds.getFieldType(sds.getSpatialFieldIndex());
			if (type.getIntConstraint(Constraint.GEOMETRY_DIMENSION) == 3) {
				return 0;
			}
		} catch (DriverException e) {
		}
		return Double.NaN;
	}

	/**
	 * Ask the user to input initial values for the non null fields
	 * 
	 * @param sds
	 * @param row
	 * @return
	 * @throws DriverException
	 * @throws TransitionException
	 */
	public static Value[] populateNotNullFields(SpatialDataSourceDecorator sds,
			Value[] row) throws DriverException, TransitionException {
		Value[] ret = new Value[row.length];
		for (int i = 0; i < sds.getFieldCount(); i++) {
			Type type = sds.getFieldType(i);
			if (type.getBooleanConstraint(Constraint.NOT_NULL)
					&& !type.getBooleanConstraint(Constraint.AUTO_INCREMENT)) {
				AskValidValue av = new AskValidValue(sds, i);
				if (UIFactory.showDialog(av)) {
					try {
						ret[i] = av.getUserValue();
					} catch (ParseException e) {
						throw new TransitionException("bug!");
					}
				} else {
					throw new TransitionException("Insertion cancelled");
				}
			} else {
				ret[i] = row[i];
			}
		}

		return ret;
	}

	public static ArrayList<Coordinate> removeDuplicated(
			ArrayList<Coordinate> points) {
		if (points.size() == 0) {
			return points;
		} else {
			ArrayList<Coordinate> ret = new ArrayList<Coordinate>();
			for (int i = 0; i < points.size() - 1; i++) {
				if (!points.get(i).equals(points.get(i + 1))) {
					ret.add(points.get(i));
				}
			}
			ret.add(points.get(points.size() - 1));
			return ret;
		}
	}

	public static boolean isActiveLayerEditable(MapContext vc) {
		IDisplayable activeLayer = vc.getActiveLayer();
		if (activeLayer == null) {
			return false;
		} else {
			return activeLayer.getDataSource().isEditable();
		}
	}

	public static boolean isActiveLayerVisible(MapContext vc) {
		IDisplayable activeLayer = vc.getActiveLayer();
		if (activeLayer == null) {
			return false;
		} else {
			return activeLayer.isVisible();
		}
	}

	public static boolean activeSelectionGreaterThan(MapContext vc, int i) {
		IDisplayable activeLayer = vc.getActiveLayer();
		if (activeLayer == null) {
			return false;
		} else {
			return activeLayer.getSelection().length >= i;
		}
	}

	public static boolean geometryTypeIs(MapContext vc, int... geometryTypes) {
		IDisplayable activeLayer = vc.getActiveLayer();
		if (activeLayer == null) {
			return false;
		} else {
			try {
				SpatialDataSourceDecorator sds = activeLayer.getDataSource();
				Type type = sds.getFieldType(sds.getSpatialFieldIndex());
				int geometryType = type
						.getIntConstraint(Constraint.GEOMETRY_TYPE);
				if (geometryType == -1) {
					return true;
				} else {
					for (int geomType : geometryTypes) {
						if (geomType == geometryType) {
							return true;
						}
					}
				}
			} catch (DriverException e) {
			}
			return false;
		}
	}

	public static boolean layerCountGreaterThan(MapContext vc, int i) {
		return vc.getAllLayersCount() > i;
	}

	public static boolean isResctritedPopup(Automaton currentTool) {

		if (currentTool instanceof ZoomInTool) {
			return true;

		} else if (currentTool instanceof ZoomOutTool) {
			return true;
		} else if (currentTool instanceof PanTool) {
			return true;
		}

		return false;

	}
}
