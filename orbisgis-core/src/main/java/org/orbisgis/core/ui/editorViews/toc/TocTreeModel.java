/*
 * OrbisGIS is a GIS application dedicated to scientific spatial simulation.
 * This cross-platform GIS is developed at French IRSTV institute and is able to
 * manipulate and create vector and raster spatial information. OrbisGIS is
 * distributed under GPL 3 license. It is produced by the "Atelier SIG" team of
 * the IRSTV Institute <http://www.irstv.cnrs.fr/> CNRS FR 2488.
 *
 *  Team leader Erwan BOCHER, scientific researcher,
 *
 *  User support leader : Gwendall Petit, geomatic engineer.
 *
 * Previous computer developer : Pierre-Yves FADET, computer engineer,
 * Thomas LEDUC, scientific researcher, Fernando GONZALEZ
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

package org.orbisgis.core.ui.editorViews.toc;

import javax.swing.JTree;
import javax.swing.tree.TreePath;

import org.gdms.driver.DriverException;
import org.orbisgis.core.layerModel.IDisplayable;
import org.orbisgis.core.layerModel.ILayer;
import org.orbisgis.core.layerModel.LayerCollection;
import org.orbisgis.core.layerModel.MapContext;
import org.orbisgis.core.ui.components.resourceTree.AbstractTreeModel;

public class TocTreeModel extends AbstractTreeModel {

	private final MapContext context;

	public TocTreeModel(MapContext con, JTree tree) {
		super(tree);
		this.context = con;
	}

	public void refresh() {
		fireEvent(new TreePath(context));
	}

	@Override
	public Object getChild(Object parent, int index) {
		if (parent instanceof LayerCollection) {
			return ((IDisplayable) parent).getLayerList().get(index);
		//If it is a Layer and not a collection, we are dealing with a Legend collection.
		} else if (parent instanceof ILayer){
			return new LegendNode((ILayer) parent, index);
		} else if (parent instanceof MapContext){
			return ((MapContext) parent).getLayerModel().get(index);
		} else {
			return null;
		}
	}

	@Override
	public int getChildCount(Object parent) {
		if (parent instanceof IDisplayable) {
			IDisplayable layer = (IDisplayable) parent;
			if (layer instanceof LayerCollection) {
				return layer.getLayerList().size();
			} else {
				try {
					return layer.getRenderingLegend().length;
				} catch (DriverException e) {
					return 0;
				}
			}
		} else if (parent instanceof MapContext){
			return ((MapContext) parent).getLayerCount();
		} else {
			return 0;
		}
	}

	@Override
	public int getIndexOfChild(Object parent, Object child) {
		if (parent instanceof ILayer) {
			if (child instanceof LegendNode) {
				return ((LegendNode) child).getLegendIndex();
			} else {
				return ((IDisplayable) parent).getIndex((ILayer) child);
			}
		} else {
			return 0;
		}
	}

	@Override
	public Object getRoot() {
		return context;
	}

	@Override
	public boolean isLeaf(Object node) {
		if (node instanceof IDisplayable) {
			IDisplayable layer = (IDisplayable) node;
			return layer.isCollection() && (layer.getLayers().length == 0);
		} else {
			return true;
		}
	}

	@Override
	public void valueForPathChanged(TreePath path, Object newValue) {

	}

	class LegendNode {
		private IDisplayable layer;
		private int legendIndex;

		public LegendNode(IDisplayable layer, int legendIndex) {
			this.layer = layer;
			this.legendIndex = legendIndex;
		}

		public IDisplayable getLayer() {
			return layer;
		}

		public int getLegendIndex() {
			return legendIndex;
		}

	}

}
