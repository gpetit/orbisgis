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
package org.orbisgis.core.layerModel;

import java.util.ArrayList;

/**
 * This abstract class contains methods that can be used by both <code>ILayer</code>s
 * and <code>LayerCollection</code>s.
 * @author alexis
 */
public abstract class AbstractDisplayable implements IDisplayable {

	protected ArrayList<LayerListener> listeners;
	protected MapContext context;

	public AbstractDisplayable(MapContext mc) {
		context = mc;
		listeners = new ArrayList<LayerListener>();
	}

////////////////protected methods.//////////////////////
	protected void fireNameChanged() {
		if (null != listeners) {
			for (LayerListener listener : listeners) {
				listener.nameChanged(new LayerListenerEvent(this));
			}
		}
	}

	@SuppressWarnings("unchecked")
	protected void fireVisibilityChanged() {
		if (null != listeners) {
			ArrayList<LayerListener> l = (ArrayList<LayerListener>) listeners.clone();
			for (LayerListener listener : l) {
				listener.visibilityChanged(new LayerListenerEvent(this));
			}
		}
	}

	protected void fireLayerMovedEvent(LayerCollection parent, ILayer layer) {
		for (LayerListener listener : listeners) {
			listener.layerMoved(new LayerCollectionEvent(parent,
				new ILayer[]{layer}));
		}
	}

	@SuppressWarnings("unchecked")
	protected void fireStyleChanged() {
		ArrayList<LayerListener> l = (ArrayList<LayerListener>) listeners.clone();
		for (LayerListener listener : l) {
			listener.styleChanged(new LayerListenerEvent(this));
		}
	}
}
