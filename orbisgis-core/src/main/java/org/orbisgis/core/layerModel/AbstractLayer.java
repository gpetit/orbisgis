/**
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
 **/
package org.orbisgis.core.layerModel;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.gdms.driver.DriverException;

public abstract class AbstractLayer extends AbstractDisplayable implements ILayer {

	private String name;
	private LayerCollection parent;

	public AbstractLayer(final String name, MapContext mc) {
		super(mc);
		this.name = name;
		listeners = new ArrayList<LayerListener>();
		context = mc;
	}

	/* getters and setters */
	/**
	 *
	 * @see org.orbisgis.core.layerModel.ILayer#getParent()
	 */
	@Override
	public LayerCollection getParent() {
		return parent;
	}

	/**
	 *
	 * @see org.orbisgis.core.layerModel.ILayer#setParent()
	 */
	@Override
	public void setParent(final LayerCollection parent) {
		this.parent = parent;
	}

	/**
	 *
	 * @see org.orbisgis.core.layerModel.ILayer#getName()
	 */
	@Override
	public String getName() {
		return name;
	}

	/**
	 * Set the name of this layer. If the name is already used in the
	 * parent MapContext, a new one will be computed.
	 * @throws LayerException
	 * @see org.orbisgis.core.layerModel.ILayer#setName(java.lang.String)
	 */
	@Override
	public void setName(final String name) throws LayerException {
		Set<String> allLayersNames = context.getAllLayersNames();
		allLayersNames.remove(getName());
		this.name = provideNewLayerName(name, allLayersNames);
		fireNameChanged();
	}

	private String provideNewLayerName(final String name,
		final Set<String> allLayersNames) {
		String tmpName = name;
		if (allLayersNames.contains(tmpName)) {
			int i = 1;
			while (allLayersNames.contains(tmpName + "_" + i)) {
				i++;
			}
			tmpName += "_" + i;
		}
		allLayersNames.add(tmpName);
		return tmpName;
	}

	@Override
	public void moveTo(LayerCollection layer, int index) throws LayerException {
		LayerCollection oldParent = getParent();
		if(oldParent != null){
			oldParent.remove(this, true);
		}
		layer.insertLayer(this, index, true);
		fireLayerMovedEvent(oldParent, this);
	}

	@Override
	public void moveTo(LayerCollection layer) throws LayerException {
		LayerCollection oldParent = getParent();
		if(oldParent != null){
			oldParent.remove(this, true);
		}
		layer.addLayer(this, true);
		fireLayerMovedEvent(oldParent, this);
	}

	/////////Interface IDisplayable//////////////:
	@Override
	public ILayer[] getLayers() {
		return new ILayer[]{};
	}

	/**
	 * Get this layer in an array, if it is a raster one.
	 * @return
	 */
	@Override
	public ILayer[] getRasterLayers() throws DriverException {
		if (isRaster()) {
			return new ILayer[]{this};
		} else {
			return null;
		}
	}

	/**
	 * Get this layer in an array, if it is a vectorial one.
	 * @return
	 */
	@Override
	public ILayer[] getVectorLayers() throws DriverException {
		if (isVectorial()) {
			return new ILayer[]{this};
		} else {
			return null;
		}
	}

	/**
	 * Get this layer in an array, if it is a WMS one.
	 * @return
	 */
	@Override
	public ILayer[] getWMSLayers() throws DriverException {
		if (isWMS()) {
			return new ILayer[]{this};
		} else {
			return null;
		}
	}

	@Override
	public List<ILayer> getLayerList() {
		ArrayList<ILayer> list = new ArrayList<ILayer>();
		return list;
	}

	@Override
	public IDisplayable[] getLayerPath() {
		ArrayList<IDisplayable> path = new ArrayList<IDisplayable>();
		IDisplayable current = this;
		while (current != null) {
			path.add(current);
			current = current.getParent();
		}

		// Now we must reverse the order
		ArrayList<IDisplayable> path2 = new ArrayList<IDisplayable>();
		int l = path.size();
		for (int i = 0; i < l; i++) {
			path2.add(i, path.get(l - i - 1));
		}

		return path2.toArray(new ILayer[path2.size()]);
	}

	@Override
	public List<IDisplayable> getDisplayableList() {
		ArrayList<IDisplayable> list = new ArrayList<IDisplayable>();
		list.add(this);
		return list;
	}

	@Override
	public void addLayerListener(LayerListener listener) {
		listeners.add(listener);
	}

	@Override
	public void removeLayerListener(LayerListener listener) {
		listeners.remove(listener);
	}

	@Override
	public void addLayerListenerRecursively(LayerListener listener) {
		listeners.add(listener);
	}

	@Override
	public void removeLayerListenerRecursively(LayerListener listener) {
		listeners.remove(listener);
	}

	@Override
	public MapContext getMapContext() {
		return this.context;
	}

	@Override
	public void setMapContext(MapContext mc) {
		this.context = mc;
	}

	@Override
	public boolean isCollection() {
		return false;
	}
}
