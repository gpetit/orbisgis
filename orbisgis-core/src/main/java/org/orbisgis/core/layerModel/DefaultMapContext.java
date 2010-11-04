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

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.gdms.driver.DriverException;

import org.gdms.source.SourceEvent;
import org.gdms.source.SourceListener;
import org.gdms.source.SourceRemovalEvent;
import org.orbisgis.core.DataManager;
import org.orbisgis.core.Services;
import org.orbisgis.core.errorManager.ErrorManager;
import org.orbisgis.core.layerModel.persistence.BoundingBox;
import org.orbisgis.core.layerModel.persistence.IdTime;
import org.orbisgis.core.layerModel.persistence.LayerCollectionType;
import org.orbisgis.core.layerModel.persistence.LayerType;
import org.orbisgis.core.layerModel.persistence.SelectedLayer;
import org.orbisgis.core.renderer.Renderer;
import org.orbisgis.progress.IProgressMonitor;
import org.orbisgis.progress.NullProgressMonitor;
import org.orbisgis.utils.I18N;

import com.vividsolutions.jts.geom.Envelope;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.io.ParseException;
import com.vividsolutions.jts.io.WKTReader;
import java.util.AbstractList;
import java.util.Collections;
import java.util.Set;
import org.orbisgis.core.layerModel.persistence.AbstractLayerType;

/**
 * Class that contains the status of the map view.
 */
public class DefaultMapContext implements MapContext {
	//The IDisplayable realizations that will be displayed. (Layer or LayerCollection

	private List<IDisplayable> displayableArtifacts;
	//The layers that have been selected to be displayed.
	private ILayer[] selectedLayers = new ILayer[0];
	//The listeners associated to this MapContext.
	private ArrayList<MapContextListener> listeners = new ArrayList<MapContextListener>();
	//A Listener dedicated to listen to new sources.
	private OpenerListener openerListener;
	//A Listener dedicated to listen to new sources removal.
	private LayerRemovalSourceListener sourceListener;
	//The currently active layer.
	private IDisplayable activeLayer;
	//A boolean chich determines if this map is open or not.
	private boolean open = false;
	//The associated jaxb context
	private org.orbisgis.core.layerModel.persistence.MapContext jaxbMapContext;
	//The bounding box of the map.
	private Envelope boundingBox;
	//An ID based on the creation date of the map context.
	private long idTime;

	/**
	 * Create a new DefaultMapContext
	 */
	public DefaultMapContext() {
		openerListener = new OpenerListener();
		sourceListener = new LayerRemovalSourceListener();
		displayableArtifacts = new ArrayList<IDisplayable>();
		this.jaxbMapContext = null;
		idTime = System.currentTimeMillis();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void addMapContextListener(MapContextListener listener) {
		listeners.add(listener);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void removeMapContextListener(MapContextListener listener) {
		listeners.remove(listener);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<IDisplayable> getLayerModel() throws IllegalStateException {
		if (isOpen()) {
			return this.displayableArtifacts;
		} else {
			throw new IllegalStateException("you can't get the model of a context which is not open !");
		}
	}

	/**
	 * Add a new Layer or LayerCollection in this context
	 * @param layer
	 * @throws LayerException
	 */
	@Override
	public void add(IDisplayable layer) throws LayerException {
		if (layer != null && !displayableArtifacts.contains(layer)) {
			this.add(layer, false);
		} else {
			if (layer == null) {
				throw new LayerException("How did you manage to add a null layer ?");
			}
			if (displayableArtifacts.contains(layer)) {
				throw new LayerException("This Layer is already contained in this Map");
			}
		}
	}

	/**
	 * Add a new Layer or LayerCollection in this context. The boolean here is used to determine if this layer
	 * is new ly created, or if it is a layer moved from an inner collection.
	 * @param layer
	 * @param isMoving
	 */
	public void add(IDisplayable layer, boolean isMoving) throws LayerException{
		if (layer != null && !displayableArtifacts.contains(layer)) {
			if(isMoving && layer.getParent()!=null){
				displayableArtifacts.add(layer);
				layer.getParent().remove((ILayer) layer);
				((ILayer) layer).setParent(null);
			} else if (!isMoving){
				layer.setName(layer.getName());
				displayableArtifacts.add(layer);
				fireLayerAddedEvent(new IDisplayable[] { layer });
			}
		} else {
			if (layer == null) {
				throw new LayerException("How did you manage to add a null layer ?");
			}
			if (displayableArtifacts.contains(layer)) {
				throw new LayerException("This Layer is already contained in this Map");
			}
		}
	}


	/**
	 * Remove a Layer or LayerCollection from this context
	 * @param layer
	 * @throws LayerException
	 */
	@Override
	public void remove(IDisplayable layer) throws LayerException {
		if (layer != null  ) {
			if(displayableArtifacts.contains(layer)){
				displayableArtifacts.remove(layer);
				IDisplayable[] toRemove = new IDisplayable[]{layer};
				if (fireLayerRemovingEvent(toRemove)) {
					if (displayableArtifacts.remove(layer)) {
						fireLayerRemovedEvent(toRemove);
					}
				}
			} else if(layer instanceof ILayer) {
				for(IDisplayable dis : displayableArtifacts){
					if(dis.isCollection() && dis.getLayerList().contains((ILayer)layer)){
						((LayerCollection) dis).remove((ILayer) layer);
					}
				}
			}
		} else {
			throw new LayerException("You can't remove a Layer that is not in the Map");
		}
	}
	@SuppressWarnings("unchecked")
	protected void fireLayerAddedEvent(IDisplayable[] added) {
		ArrayList<LayerListener> l = (ArrayList<LayerListener>) listeners.clone();
		for (LayerListener listener : l) {
			listener.layerAdded(new LayerCollectionEvent(null, added));
		}
	}

	@SuppressWarnings("unchecked")
	protected void fireLayerRemovedEvent(IDisplayable[] removed) {
		ArrayList<LayerListener> l = (ArrayList<LayerListener>) listeners.clone();
		for (LayerListener listener : l) {
			listener.layerRemoved(new LayerCollectionEvent(null, removed));
		}
	}

	@SuppressWarnings("unchecked")
	protected boolean fireLayerRemovingEvent(IDisplayable[] toRemove) {
		ArrayList<LayerListener> l = (ArrayList<LayerListener>) listeners.clone();
		for (LayerListener listener : l) {
			if (!listener.layerRemoving(new LayerCollectionEvent(null, toRemove))) {
				return false;
			}
		}
		return true;
	}
	@Override
	public void insertLayer(IDisplayable dis, int i) throws IndexOutOfBoundsException {
		displayableArtifacts.add(i, dis);
	}

	/**
	 * Move a Layer or LayerCollection in this context.
	 * @param layer
	 * @param i
	 * @throws LayerException
	 */
	@Override
	public void moveLayerBefore(IDisplayable layer, IDisplayable position) throws LayerException {
		if(displayableArtifacts.contains(layer) && displayableArtifacts.contains(position)){
			displayableArtifacts.remove(layer);
			int i = displayableArtifacts.indexOf(position);
			displayableArtifacts.add(i, layer);
		}
	}

	@Override
	public long getIdTime() {
		return idTime;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Envelope getBoundingBox() {
		return boundingBox;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setBoundingBox(Envelope envelope) {
		this.boundingBox = envelope;
	}

	@Override
	public Envelope getEnvelope() {
		Envelope env = new Envelope();
		for (IDisplayable dis : displayableArtifacts) {
			env.expandToInclude(dis.getEnvelope());
		}
		return env;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public IDisplayable getLayer(int i){
		return displayableArtifacts.get(i);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ILayer[] getLayers() {
		checkIsOpen();
		ArrayList<IDisplayable> layers = new ArrayList<IDisplayable>();
		for (IDisplayable dis : this.displayableArtifacts) {
			layers.addAll(dis.getLayerList());
		}
		return layers.toArray(new ILayer[layers.size()]);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public IDisplayable[] getAllLayers() {
		checkIsOpen();
		ArrayList<IDisplayable> layers = new ArrayList<IDisplayable>();
		for (IDisplayable dis : this.displayableArtifacts) {
			if(dis instanceof LayerCollection){
				layers.add(dis);
			}
			layers.addAll(dis.getLayerList());
		}
		return layers.toArray(new IDisplayable[layers.size()]);
	}
	/**
	 * {@inheritDoc}
	 */
	@Override
	public ILayer[] getSelectedLayers() {
		checkIsOpen();
		return selectedLayers;
	}

	/**
	 * Get all the raster layers of this map context
	 * @return
	 * @throws IllegalStateException
	 */
	@Override
	public ILayer[] getRasterLayers() throws IllegalStateException {
		checkIsOpen();
		ArrayList<ILayer> list = new ArrayList<ILayer>();
		for (ILayer lay : getLayers()) {
			try {
				if (lay.isRaster()) {
					list.add(lay);
				}
			} catch (DriverException ex) {
				Logger.getLogger("Problem while evaluating the state of this Layer");
			}
		}
		return list.toArray(new ILayer[list.size()]);
	}

	/**
	 * Get all the raster layers of this map context
	 * @return
	 * @throws IllegalStateException
	 */
	@Override
	public ILayer[] getVectorLayers() throws IllegalStateException {
		checkIsOpen();
		ArrayList<ILayer> list = new ArrayList<ILayer>();
		for (ILayer lay : getLayers()) {
			try {
				if (lay.isVectorial()) {
					list.add(lay);
				}
			} catch (DriverException ex) {
				Logger.getLogger("Problem while evaluating the state of this Layer");
			}
		}
		return list.toArray(new ILayer[list.size()]);
	}

	/**
	 * Get all the raster layers of this map context
	 * @return
	 * @throws IllegalStateException
	 */
	@Override
	public ILayer[] getWMSLayers() throws IllegalStateException {
		checkIsOpen();
		ArrayList<ILayer> list = new ArrayList<ILayer>();
		for (ILayer lay : getLayers()) {
			if (lay.isWMS()) {
				list.add(lay);
			}
		}
		return list.toArray(new ILayer[list.size()]);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public IDisplayable getLayerByName(String name) {
		for (IDisplayable dis : this.displayableArtifacts) {
			if (name.contentEquals(dis.getName())) {
				return dis;
			}
			if (dis instanceof LayerCollection) {
				ILayer lay = ((LayerCollection) dis).getLayerByName(name);
				if (lay != null) {
					return lay;
				}
			}
		}
		return null;
	}

	/**
	 * Get the number of layers in the root level of this MapContext
	 * @return
	 */
	@Override
	public int getLayerCount() {
		return displayableArtifacts.size();
	}

	@Override
	public int getAllLayersCount() {
		int c = 0;
		if (displayableArtifacts != null) {
			c = displayableArtifacts.size();
			for (IDisplayable dis : displayableArtifacts) {
				if (dis.isCollection()) {
					c = c + dis.getLayerList().size();
				}
			}
		}
		return c;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setSelectedLayers(IDisplayable[] selectedLayers) {
		checkIsOpen();
		ArrayList<IDisplayable> filtered = new ArrayList<IDisplayable>();
		for (IDisplayable layer : selectedLayers) {
			if (getLayerByName(layer.getName()) != null) {
				filtered.add(layer);
			}
		}
		this.selectedLayers = filtered.toArray(new ILayer[filtered.size()]);

		for (MapContextListener listener : listeners) {
			listener.layerSelectionChanged(this);
		}
	}

	/*
	 * private void checkLayerCRS(final ILayer layer) throws LayerException,
	 * DriverException {
	 * 
	 * CoordinateReferenceSystem layerCRS = layer.getDataSource().getCRS();
	 * 
	 * if (crs == null) { crs = layerCRS;
	 * 
	 * } else if (!checkCRSProjection(layerCRS)) { throw new
	 * LayerException("Cannot add a layer with CRS" +
	 * "' because it's different from map CRS."); }
	 * 
	 * }
	 * 
	 * public boolean checkCRSProjection(CoordinateReferenceSystem layerCRS) {
	 * 
	 * return (layerCRS.getProjection().equals(crs.getProjection()));
	 * 
	 * }
	 */
	/**
	 * Fill the context from the information obtained in the specified XML mapped
	 * object. Layers that cannot be created are removed from the layer tree and
	 * an error message is sent to the ErrorManager service
	 * 
	 * @param layer
	 * @return
	 */
	public void recoverContext(List<AbstractLayerType> layerList,
		HashMap<ILayer, LayerType> layerPersistenceMap) {
		DataManager dataManager = (DataManager) Services.getService(DataManager.class);
		IDisplayable ret;
		this.displayableArtifacts=new ArrayList<IDisplayable>();
		for (AbstractLayerType layer : layerList) {
			ret = null;
			if (layer instanceof LayerCollectionType) {
				//We have a LayerCollection, we instanciate it.
				LayerCollectionType xmlLayerCollection = (LayerCollectionType) layer;
				ret = dataManager.createLayerCollection(layer.getName(), this);
				//We try to recover the children of this collection. They are supposed to
				//be Layer instances.
				List<LayerType> xmlChildren = xmlLayerCollection.getLayer();
				for (LayerType layerType : xmlChildren) {
					ILayer lyr = null;
					try {
						lyr = dataManager.createLayer(layerType.getSourceName(), this);
					} catch (LayerException e) {
						Services.getErrorManager().error(
							"Cannot recover layer: " + layer.getName(), e);
					}
					if (lyr != null) {
						try {
							((LayerCollection) ret).addLayer(lyr);
						} catch (Exception e) {
							Services.getErrorManager().error(
								"Cannot add layer to collection: "
								+ lyr.getName(), e);
						}
					}
				}
			} else {//This layer is not a collection.
				try {
					ret = dataManager.createLayer(((LayerType) layer).getSourceName(), this);
					layerPersistenceMap.put((ILayer) ret, (LayerType) layer);
				} catch (LayerException e) {
					Services.getErrorManager().error(
						"Cannot recover layer: " + layer.getName(), e);
				}
			}
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void draw(BufferedImage inProcessImage, Envelope extent,
		IProgressMonitor pm) {
		checkIsOpen();
		Renderer renderer = new Renderer();
		renderer.draw(inProcessImage, extent, getLayerModel(), pm);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Set<String> getAllLayersNames() {
		ILayer[] layers = getLayers();
		final Set<String> allLayersNames = new HashSet<String>();
		for (ILayer layer : layers) {
			allLayersNames.add(layer.getName());
				}
		for(IDisplayable dis : displayableArtifacts){
			if(dis.isCollection()){
				allLayersNames.add(dis.getName());
			}
		}
		return allLayersNames;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public IDisplayable getActiveLayer() {
		checkIsOpen();
		return activeLayer;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setActiveLayer(IDisplayable activeLayer) {
		checkIsOpen();
		IDisplayable lastActive = this.activeLayer;
		this.activeLayer = activeLayer;
		for (MapContextListener listener : listeners) {
			listener.activeLayerChanged(lastActive, this);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Object getJAXBObject() {
		if (jaxbMapContext != null) {
			return jaxbMapContext;
		} else {
			org.orbisgis.core.layerModel.persistence.MapContext xmlMapContext = new org.orbisgis.core.layerModel.persistence.MapContext();

			// Set the id time
			IdTime idTime = new IdTime();
			idTime.setName(getIdTime());

			xmlMapContext.setIdTime(idTime);

			// get BoundinBox from ,persistence file
			BoundingBox boundingBox = new BoundingBox();
			if (getBoundingBox() != null) {
				GeometryFactory geomF = new GeometryFactory();
				Geometry geom = geomF.toGeometry(getBoundingBox());
				boundingBox.setName(geom.toText());
			} else {
				boundingBox.setName("");
			}
			xmlMapContext.setBoundingBox(boundingBox);

			for (ILayer selected : selectedLayers) {
				SelectedLayer sl = new SelectedLayer();
				sl.setName(selected.getName());
				xmlMapContext.getSelectedLayer().add(sl);
			}
			List<AbstractLayerType> listALT = xmlMapContext.getAbstractLayer();
			AbstractLayerType abstrLayer;
			for (IDisplayable dis : displayableArtifacts) {
				abstrLayer = null;
				if (dis instanceof LayerCollection) {
					LayerCollectionType lct = new LayerCollectionType();
					List<LayerType> list = lct.getLayer();
					List<ILayer> layerList = ((LayerCollection) dis).getLayerCollection();
					for (ILayer lay : layerList) {
						list.add(lay.saveLayer());
					}
					abstrLayer = lct;
				} else {
					LayerType layType = ((ILayer) dis).saveLayer();
					abstrLayer = layType;
				}
				if (abstrLayer != null) {
					listALT.add(abstrLayer);
				}
			}
			/*
			 * OgcCrs ogcCrs = new OgcCrs(); if (getCoordinateReferenceSystem()
			 * == null) { ogcCrs.setName(""); } else { //
			 * ogcCrs.setName(crs.toWkt()); } xmlMapContext.setOgcCrs(ogcCrs);
			 */

			return xmlMapContext;
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setJAXBObject(Object jaxbObject) {
		if (isOpen()) {
			throw new IllegalStateException("The map must"
				+ " be closed to invoke this method");
		}
		org.orbisgis.core.layerModel.persistence.MapContext mapContext = (org.orbisgis.core.layerModel.persistence.MapContext) jaxbObject;

		this.jaxbMapContext = mapContext;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void close(IProgressMonitor pm) {

		checkIsOpen();

		jaxbMapContext = (org.orbisgis.core.layerModel.persistence.MapContext) getJAXBObject();

		// Close the layers
		if (pm == null) {
			pm = new NullProgressMonitor();
		}
		ILayer[] layers = this.getLayers();
		for (int i = 0; i < layers.length; i++) {
			pm.progressTo(i * 100 / layers.length);
			try {
				layers[i].close();
			} catch (LayerException e) {
				Services.getErrorManager().error(
					"Could not close layer: " + layers[i].getName());
			}
		}
		removeLayerListenerRecursively(openerListener);
		// Listen source removal events
		DataManager dm = Services.getService(DataManager.class);
		dm.getSourceManager().removeSourceListener(sourceListener);

		this.open = false;
	}

	/**
	 * {@inheritDoc}
	 * @param ll
	 */
	@Override
	public void removeLayerListenerRecursively(LayerListener ll) {
		for (IDisplayable dis : displayableArtifacts) {
			dis.removeLayerListenerRecursively(openerListener);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void addLayerListenerRecursively(LayerListener ll) {
		for (IDisplayable dis : displayableArtifacts) {
			dis.addLayerListenerRecursively(openerListener);
		}
	}

	/**
	 * Open the map. If an XML config file has been given before, this method will try to
	 * recreate te context from it.
	 * @param pm
	 * @throws LayerException
	 */
	@Override
	public void open(IProgressMonitor pm) throws LayerException {

		if (isOpen()) {
			throw new IllegalStateException("The map is already open");
		}

		this.activeLayer = null;

		// Recover IDisplayable collection
		HashMap<ILayer, LayerType> layerPersistenceMap = null;
		if (jaxbMapContext != null) {
			List<AbstractLayerType> layer = jaxbMapContext.getAbstractLayer();
			layerPersistenceMap = new HashMap<ILayer, LayerType>();
			recoverContext(layer, layerPersistenceMap);
			try {

				if (jaxbMapContext.getIdTime() != null) {
					idTime = jaxbMapContext.getIdTime().getName();
				}

				String boundingBoxValue = jaxbMapContext.getBoundingBox().getName();

				if (!boundingBoxValue.equals("")) {
					boundingBox = new WKTReader().read(boundingBoxValue).getEnvelopeInternal();
				} else {
					boundingBox = null;
				}
			} catch (ParseException e) {
				Services.getErrorManager().error(
					"Cannot read the bounding box", e);
			}
			/*
			 * /String wkt = jaxbMapContext.getOgcCrs().getName(); if (wkt !=
			 * null) { if (wkt.length() > 0) { crs =
			 * PRJUtils.getCRSFromWKT(wkt); } }
			 */
		}

		// Listen source removal events
		DataManager dm = Services.getService(DataManager.class);
		dm.getSourceManager().addSourceListener(sourceListener);

		// open layers
		if (pm == null) {
			pm = new NullProgressMonitor();
		}
		if(displayableArtifacts!=null && !displayableArtifacts.isEmpty()){
			IDisplayable[] layers = this.getAllLayersOffline();
			int i = 0;
			try {
				ArrayList<IDisplayable> toRemove = new ArrayList<IDisplayable>();
				for (i = 0; i < layers.length; i++) {
					pm.progressTo(i * 100 / layers.length);
					if (!(layers[i] instanceof LayerCollection)) {
						try {
							layers[i].open();
						} catch (LayerException e) {
							Services.getService(ErrorManager.class).warning(
								"Cannot open '" + layers[i].getName()
								+ "'. Layer is removed", e);
							toRemove.add(layers[i]);
						}
					}
					if (layerPersistenceMap != null) {
						if (!toRemove.contains(layers[i])) {
							try {
								if(layers[i] instanceof ILayer){
									((ILayer) layers[i]).restoreLayer(layerPersistenceMap.get((ILayer) layers[i]));
								}
							} catch (LayerException e) {
								Services.getService(ErrorManager.class).warning(
									"Cannot restore '" + layers[i].getName()
									+ "'. Layer is removed", e);
								toRemove.add(layers[i]);
							}
						}
					}
				}

				for (IDisplayable layer : toRemove) {
					if(layer instanceof ILayer){
						((ILayer) layer).getParent().remove((ILayer) layer);
					}
				}
			} catch (LayerException e) {
				for (int j = 0; j < i; j++) {
					pm.progressTo(j * 100 / i);
					if (layers[j] instanceof ILayer) {
						try {
							layers[j].close();
						} catch (LayerException e1) {
							// ignore
						}
					}
				}

				throw e;
			}
		}
		this.open = true;

		if (jaxbMapContext != null) {
			// Recover selected layers
			List<SelectedLayer> selectedLayerList = jaxbMapContext.getSelectedLayer();
			final ArrayList<IDisplayable> selected = new ArrayList<IDisplayable>();
			for (final SelectedLayer selectedLayer : selectedLayerList) {
				processLayersNodes(new ILayerAction() {

					@Override
					public void action(IDisplayable layer) {
						if (selectedLayer.getName().equals(layer.getName())) {
							selected.add(layer);
							return;
						}
					}
				});
			}
			setSelectedLayers(selected.toArray(new ILayer[selected.size()]));
		}
		jaxbMapContext = null;
	}

	@Override
	public boolean isOpen() {
		return open;
	}

	/**
	 * Apply action to each leave of this layer tree
	 * @param root
	 * @param action
	 */
	public void processLayersLeaves(List<IDisplayable> root, ILayerAction action) {
		for (IDisplayable layer : root) {
			action.action(layer);
		}
	}

	/**
	 * Performs action on every IDisplayable element in this map context.
	 * @param action
	 */
	public void processLayersNodes(ILayerAction action) {
		for (IDisplayable dis : displayableArtifacts) {
			action.action(dis);
			if (dis instanceof LayerCollection) {
				for (ILayer lay : ((LayerCollection) dis).getLayerCollection()) {
					action.action(lay);
				}
				action.action(dis);
			}
		}
	}

	/**********************************************************************/
	/***********************Private Methods********************************/
	/**********************************************************************/
	private void checkIsOpen() throws IllegalStateException {
		if (!isOpen()) {
			throw new IllegalStateException(
				I18N.getText("orbisgis.core.ui.plugins.views.geocognition.wizards.newMap"));
		}
	}

	private IDisplayable[] getAllLayersOffline(){
		ArrayList<IDisplayable> layers = new ArrayList<IDisplayable>();
		for (IDisplayable dis : this.displayableArtifacts) {
			if(dis instanceof LayerCollection){
				layers.add(dis);
			}
			layers.addAll(dis.getLayerList());
		}
		return layers.toArray(new IDisplayable[layers.size()]);
	}
	/**********************************************************************/
	/***********************Private Classes********************************/
	/**********************************************************************/
	/*
	 * A listener for removal actions
	 */
	private final class LayerRemovalSourceListener implements SourceListener {

		@Override
		public void sourceRemoved(final SourceRemovalEvent e) {
			processLayersLeaves(displayableArtifacts,
				new DeleteLayerFromResourceAction(e));
		}

		@Override
		public void sourceNameChanged(SourceEvent e) {
		}

		@Override
		public void sourceAdded(SourceEvent e) {
		}
	}

	/*
	 * A listener for deleting actions
	 */
	private final class DeleteLayerFromResourceAction implements
		org.orbisgis.core.layerModel.ILayerAction {

		private ArrayList<String> resourceNames = new ArrayList<String>();

		private DeleteLayerFromResourceAction(SourceRemovalEvent e) {
			String[] aliases = e.getNames();
			resourceNames.addAll(Arrays.asList(aliases));
			resourceNames.add(e.getName());
		}

		@Override
		public void action(IDisplayable layer) {
			String layerName = layer.getName();
			if (resourceNames.contains(layerName)) {
				try {
					if (layer instanceof LayerCollection) {
						LayerCollection lc = ((ILayer) layer).getParent();
						if (lc != null) {
							lc.remove((ILayer) layer);
						} else {
							layer.getMapContext().remove(layer);
						}
					}
				} catch (LayerException e) {
					Services.getErrorManager().error(
						"Cannot associate layer: " + layer.getName()
						+ ". The layer must be removed manually.");
				}
			}
		}
	}

	/*
	 * A listener dedicated to opening operations.
	 */
	private final class OpenerListener extends LayerListenerAdapter implements
		LayerListener {

		@Override
		public void layerAdded(LayerCollectionEvent e) {
			if (isOpen()) {
				for (final IDisplayable layer : e.getAffected()) {
					try {
						layer.open();
						layer.addLayerListenerRecursively(openerListener);
						// checkLayerCRS(layer);
					} catch (LayerException ex) {
						Services.getErrorManager().error(
							"Cannot open layer: " + layer.getName()
							+ ". The layer is removed from view.", ex);
						try {
							remove(layer);
						} catch (LayerException e1) {
							Services.getErrorManager().error(
								"Cannot remove layer: " + layer.getName(), ex);
						}
					}
				}
			}
		}

		@Override
		public void layerRemoved(LayerCollectionEvent e) {
			HashSet<IDisplayable> newSelection = new HashSet<IDisplayable>();
			for (ILayer selectedLayer : selectedLayers) {
				newSelection.add(selectedLayer);
			}
			IDisplayable[] affected = e.getAffected();
			for (final IDisplayable layer : affected) {
				// Check active
				if (activeLayer == layer) {
					setActiveLayer(null);
				}

				// Check selection
				newSelection.remove(layer);
				layer.removeLayerListenerRecursively(openerListener);
				if (isOpen()) {
					try {
						layer.close();
					} catch (LayerException e1) {
						Services.getErrorManager().warning(
							"Cannot close layer: " + layer.getName(), e1);
					}
				}
			}
			selectedLayers = newSelection.toArray(new ILayer[newSelection.size()]);
		}
	}
	/**
	 * A mapcontext must have only one {@link CoordinateReferenceSystem} By
	 * default the crs is set to null.
	 */
	/*
	 * public CoordinateReferenceSystem getCoordinateReferenceSystem() { return
	 * crs; }
	 * 
	 * /** Set a {@link CoordinateReferenceSystem} to the mapContext
	 * 
	 * @param crs
	 *//*
	 * public void setCoordinateReferenceSystem(CoordinateReferenceSystem
	 * crs) { this.crs = crs; }
	 */

}
