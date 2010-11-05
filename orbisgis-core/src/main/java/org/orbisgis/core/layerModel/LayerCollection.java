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
import java.util.List;
import java.util.Set;

import org.gdms.data.SpatialDataSourceDecorator;
import org.gdms.driver.DriverException;
import org.orbisgis.core.layerModel.persistence.LayerCollectionType;
import org.orbisgis.core.layerModel.persistence.LayerType;

import com.vividsolutions.jts.geom.Envelope;
import org.apache.log4j.Logger;
import org.orbisgis.core.layerModel.persistence.AbstractLayerType;
import org.orbisgis.core.renderer.legend.Legend;

public class LayerCollection extends AbstractDisplayable implements IDisplayable {

	private static Logger logger = Logger.getLogger(AbstractDisplayable.class.getName());
	private String name;
	private List<ILayer> layerCollection;

	/**
	 * Create a new LayerCollection with the name name.
	 * @param name
	 */
	public LayerCollection(String name, MapContext con) {
		super(name, con);
		this.name = name;
		layerCollection = new ArrayList<ILayer>();
		if(con !=null){
			try {
				addToContext();
			} catch( LayerException e) {
				logger.warn("can't add the layer to the MapContext",e);
			}
		}
	}

	/**
	 * Retrieve the layer collection as a list of layers.
	 * @return
	 */
	public List<ILayer> getLayerCollection() {
		return layerCollection;
	}

	/**
	 * Get the layer stored at the given index in the collection.
	 * @param index
	 * @return
	 */
	public ILayer getLayer(final int index) {
		//TODO : get will throw a IndexOutOfBoundsException which is nor catch neither managed here...
		return layerCollection.get(index);
	}

	/**
	 * Get the name of the layer collection
	 * @return
	 */
	@Override
	public String getName() {
		return this.name;
	}

	/**
	 * Add a ILayer to this collection.
	 * @param layer
	 * @throws LayerException
	 */
	public void addLayer(final ILayer layer) throws LayerException {
		if(context.getLayerModel().contains(layer) || layer.getParent()!=null){
			addLayer(layer, true);
		}else {
			addLayer(layer, false);
		}
	}

	/**
	 * Insert layer at the given index.
	 * @param layer
	 * @param index
	 * @throws LayerException
	 */
	public void insertLayer(final ILayer layer, int index)
		throws LayerException {
		insertLayer(layer, index, false);
	}

	/**
	 * Removes the layer from the collection
	 * 
	 * @param layerName
	 * @return the layer removed or null if the layer does not exists
	 * @throws LayerException
	 * 
	 */
	public ILayer remove(final String layerName) throws LayerException {
		for (int i = 0; i < size(); i++) {
			if (layerName.equals(layerCollection.get(i).getName())) {
				return remove(layerCollection.get(i));
			}
		}
		return null;
	}

	/**
	 * Retrieve the children of this node as an array.
	 * @return
	 */
	public ILayer[] getChildren() {
		if (null != layerCollection) {
			ILayer[] result = new ILayer[size()];
			return layerCollection.toArray(result);
		} else {
			return null;
		}
	}

	/**
	 * Return the number of children in this collection.
	 * @return
	 */
	private int size() {
		return layerCollection.size();
	}

	public ILayer remove(ILayer layer) throws LayerException {
		return remove(layer, false);
	}

	/**
	 * Add a new layer to this collection.
	 * @param layer
	 * @param isMoving
	 * @throws LayerException
	 */
	public void addLayer(ILayer layer, boolean isMoving) throws LayerException {
		if (null != layer) {
			if (isMoving) {
				layerCollection.add(layer);
				layer.setParent(this);
			} else {
				setNamesRecursively(layer, context.getAllLayersNames());
				layerCollection.add(layer);
				layer.setParent(this);
				fireLayerAddedEvent(new ILayer[]{layer});
			}
		}
	}

	/**
	 * Remove the ILayer layer from this collection.
	 * @param layer
	 * @param isMoving
	 * @return
	 * @throws LayerException
	 */
	public ILayer remove(ILayer layer, boolean isMoving) throws LayerException {
		if (layerCollection.contains(layer)) {
			if (isMoving) {
				if (layerCollection.remove(layer)) {
					return layer;
				} else {
					return null;
				}
			} else {
				ILayer[] toRemove = new ILayer[]{layer};
				if (fireLayerRemovingEvent(toRemove)) {
					if (layerCollection.remove(layer)) {
						fireLayerRemovedEvent(toRemove);
						return layer;
					} else {
						return null;
					}
				} else {
					return null;
				}
			}
		} else {
			return null;
		}
	}

	/**
	 * Try to insert the ILayer layer at the index index.
	 * @param layer
	 * @param index
	 * @param isMoving
	 * @throws LayerException
	 */
	public void insertLayer(ILayer layer, int index, boolean isMoving)
		throws LayerException {
		if (null != layer) {
			if (isMoving) {
				layerCollection.add(index, layer);
				layer.setParent(this);
			} else {
				setNamesRecursively(layer, context.getAllLayersNames());
				layerCollection.add(index, layer);
				layer.setParent(this);
				fireLayerAddedEvent(new ILayer[]{layer});
			}
		}

	}

	public int getLayerCount() {
		return layerCollection.size();
	}

	public AbstractLayerType saveLayer() {
		LayerCollectionType xmlLayer = new LayerCollectionType();
		xmlLayer.setName(getName());
		for (ILayer child : layerCollection) {
			LayerType xmlChild = child.saveLayer();
			xmlLayer.getLayer().add(xmlChild);
		}

		return xmlLayer;
	}

	public void restoreLayer(LayerType layer) throws LayerException {
	}

	public ILayer getLayerByName(String layerName) {
		for (ILayer layer : layerCollection) {
			if (layer.getName().equals(layerName)) {
				return layer;
			}
		}
		return null;
	}

	/**
	 * Retrieve the children of this collection that are raster layers
	 * @return
	 * @throws DriverException
	 */
	@Override
	public ILayer[] getRasterLayers() throws DriverException {
		ArrayList<ILayer> filterLayer = new ArrayList<ILayer>();

		for (ILayer l : this.layerCollection) {
			if (l.isRaster()) {
				filterLayer.add(l);
			}
		}
		return filterLayer.toArray(new ILayer[filterLayer.size()]);
	}

	/**
	 * Retrieve the children of this collection that are vector layers
	 * @return
	 * @throws DriverException
	 */
	@Override
	public ILayer[] getVectorLayers() throws DriverException {
		ArrayList<ILayer> filterLayer = new ArrayList<ILayer>();
		for (ILayer l : this.layerCollection) {
			if (l.isVectorial()) {
				filterLayer.add(l);
			}
		}
		return filterLayer.toArray(new ILayer[filterLayer.size()]);
	}

	/**
	 * Retrieve the children of this collection that are vector layers
	 * @return
	 * @throws DriverException
	 */
	@Override
	public ILayer[] getWMSLayers() throws DriverException {
		ArrayList<ILayer> filterLayer = new ArrayList<ILayer>();
		for (ILayer l : this.layerCollection) {
			if (l.isWMS()) {
				filterLayer.add(l);
			}
		}
		return filterLayer.toArray(new ILayer[filterLayer.size()]);
	}

	/**
	 * Used to determine if this layer is a raster layer. It is not, it is a layer collection.
	 * @return false
	 */
	public boolean isRaster() {
		return false;
	}

	/**
	 * Used to determine if this layer is a vector layer. It is not, it is a layer collection.
	 * @return false
	 */
	public boolean isVectorial() {
		return false;
	}

	///////******************************************///////
	///////******IDisplayable interface/*************///////
	///////******************************************///////

	/**
	 * This IDisplayable can't have a Parent. Returns null.
	 * @return
	 */
	@Override
	public LayerCollection getParent(){
		return null;
	}
	/**
	 * Close this layer and all its children.
	 * @throws LayerException
	 */
	@Override
	public void close() throws LayerException {
		for (ILayer layer : layerCollection) {
			layer.close();
		}
	}

	/**
	 * Open the layer and all its children.
	 * @throws LayerException
	 */
	@Override
	public void open() throws LayerException {
		for (ILayer layer : layerCollection) {
			layer.open();
		}
	}
	
	/**
	 * Return the layers that are contained in that collection in an array of ILayer.
	 * @return
	 */
	@Override
	public ILayer[] getLayers() {
		if (layerCollection != null) {
			return this.layerCollection.toArray(new ILayer[layerCollection.size()]);
		} else {
			return null;
		}
	}

	@Override
	public List<ILayer> getLayerList() {
		return this.getLayerCollection();
	}

	@Override
	public List<IDisplayable> getDisplayableList() {
		ArrayList<IDisplayable> list = new ArrayList<IDisplayable>(layerCollection);
		list.add(this);
		return list;
	}

	@Override
	public IDisplayable[] getLayerPath(){
		return null;
	}
	/**
	 * Check if this layer is visible or not. It is visible if at least one of its children is visible,
	 * false otherwise.
	 * @see org.orbisgis.core.layerModel.ILayer#isVisible()
	 */
	@Override
	public boolean isVisible() {
		for (ILayer layer : getChildren()) {
			if (layer.isVisible()) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Set the visible attribute. We don't see this object (which is a collection,
	 * not a layer) but its leaves. Consequently, whe using this method, we set
	 * the visible attribute to isVisible for all the leaves of this collection.
	 * @throws LayerException
	 * @see org.orbisgis.core.layerModel.ILayer#setVisible(boolean)
	 */
	@Override
	public void setVisible(boolean isVisible) throws LayerException {
		for (ILayer layer : getChildren()) {
			layer.setVisible(isVisible);
		}
		fireVisibilityChanged();
	}

	/**
	 * Returns the index of the first occurrence of the specified element in this list,
	 * or -1 if this list does not contain the element.
	 * @param layer
	 * @return
	 */
	@Override
	public int getIndex(ILayer layer) {
		return layerCollection.indexOf(layer);
	}

	/**
	 * Add a listener to the list of listeners associated to this LayerCollection.
	 * @param listener
	 */
	@Override
	public void addLayerListener(LayerListener listener) {
		if(!listeners.contains(listener)){
			this.listeners.add(listener);
		}
	}

	@Override
	public void removeLayerListener(LayerListener listener) {
		listeners.remove(listener);
	}

	/**
	 * Add the LayerListener listener to this, and to each child of this.
	 *
	 * @param listener
	 */
	@Override
	public void addLayerListenerRecursively(LayerListener listener) {
		this.addLayerListener(listener);
		for (ILayer layer : layerCollection) {
			layer.addLayerListener(listener);
		}
	}

	/**
	 * Remove the LayerListener listener of this' listeners, and of its children's listeners
	 * @param listener
	 */
	@Override
	public void removeLayerListenerRecursively(LayerListener listener) {
		this.removeLayerListener(listener);
		for (ILayer layer : layerCollection) {
			layer.removeLayerListener(listener);
		}
	}

	/**
	 * Get the context in which this collection is.
	 * @return
	 */
	@Override
	public MapContext getMapContext() {
		return this.context;
	}

	/**
	 * Set the context in which this collection lives.
	 * @param mc
	 */
	@Override
	public void setMapContext(MapContext mc) {
		this.context = mc;
	}

	@Override
	public Envelope getEnvelope() {
		Envelope tmp = new Envelope();
		for (ILayer layer : layerCollection) {
			tmp.expandToInclude(layer.getEnvelope());
		}
		return tmp;
	}

	/**
	 * Supposed to return the datasource associated to this layer. But it's a collection,
	 * so it is null.
	 * @return
	 */
	@Override
	public SpatialDataSourceDecorator getDataSource() {
		return null;
	}

	@Override
	public boolean isCollection() {
		return true;
	}

	@Override
	public int[] getSelection() {
		return new int[0];
	}

	@Override
	public void setSelection(int[] newSelection) {
	}

	/**
	 * Supposed to return a legend... but we are working on a LayerCollection, so it doesn't
	 * have one.
	 * @return
	 * @throws DriverException
	 */
	public Legend[] getRenderingLegend() throws DriverException {
		throw new UnsupportedOperationException("Cannot draw a layer collection");
	}

	///////////Static methods///////////////////////////////
	/**
	 * Count the number of LayerCollection.
	 * @param root
	 * @return
	 */
	public static int getNumberOfLeaves(final LayerCollection root) {
		return root.layerCollection.size();
	}
	///////////Private methods//////////////////////////

	/*
	 * This method will guarantee that layer, and all its potential inner
	 * layers, will have names that are not already owned by another, declared, layer.
	 */
	private void setNamesRecursively(final ILayer layer,
		final Set<String> allLayersNames) throws LayerException {
		layer.setName(provideNewLayerName(layer.getName(), allLayersNames));
		if (layer instanceof LayerCollection) {
			LayerCollection lc = (LayerCollection) layer;
			if (null != lc.getLayerCollection()) {
				for (ILayer layerItem : lc.getChildren()) {
					setNamesRecursively(layerItem, allLayersNames);
				}
			}
		}
	}

	/*
	 * Check that name is not already contained in allLayersNames.
	 * If it is in, a new String is created and returned, with the form name_i
	 * where i is as small as possible.
	 */
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

	@SuppressWarnings("unchecked")
	protected void fireLayerAddedEvent(ILayer[] added) {
		ArrayList<LayerListener> l = (ArrayList<LayerListener>) listeners.clone();
		for (LayerListener listener : l) {
			listener.layerAdded(new LayerCollectionEvent(this, added));
		}
	}

	@SuppressWarnings("unchecked")
	protected void fireLayerRemovedEvent(ILayer[] removed) {
		ArrayList<LayerListener> l = (ArrayList<LayerListener>) listeners.clone();
		for (LayerListener listener : l) {
			listener.layerRemoved(new LayerCollectionEvent(this, removed));
		}
	}

	@SuppressWarnings("unchecked")
	protected boolean fireLayerRemovingEvent(ILayer[] toRemove) {
		ArrayList<LayerListener> l = (ArrayList<LayerListener>) listeners.clone();
		for (LayerListener listener : l) {
			if (!listener.layerRemoving(new LayerCollectionEvent(this, toRemove))) {
				return false;
			}
		}
		return true;
	}
	//////////Private classes//////////////////////////

	private class GetEnvelopeLayerAction implements ILayerAction {

		private Envelope globalEnvelope;

		@Override
		public void action(IDisplayable layer) {
			if (null == globalEnvelope) {
				globalEnvelope = new Envelope(layer.getEnvelope());
			} else {
				globalEnvelope.expandToInclude(layer.getEnvelope());
			}
		}

		public Envelope getGlobalEnvelope() {
			return globalEnvelope;
		}
	}

	private static class CountLeavesAction implements ILayerAction {

		private int numberOfLeaves = 0;

		@Override
		public void action(IDisplayable layer) {
			numberOfLeaves++;
		}

		public int getNumberOfLeaves() {
			return numberOfLeaves;
		}
	}
}
