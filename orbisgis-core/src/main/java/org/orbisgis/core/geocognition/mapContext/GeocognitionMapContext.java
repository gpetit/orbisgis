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
 * Copyright (C) 2010 Erwan BOCHER, Alexis GUEGANNO, Maxence LAURENT, Antoine GOURLAY
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

package org.orbisgis.core.geocognition.mapContext;

import org.orbisgis.core.OrbisGISPersitenceConfig;
import org.orbisgis.core.edition.EditableElementException;
import org.orbisgis.core.geocognition.AbstractExtensionElement;
import org.orbisgis.core.geocognition.GeocognitionElementContentListener;
import org.orbisgis.core.geocognition.GeocognitionElementFactory;
import org.orbisgis.core.geocognition.GeocognitionExtensionElement;
import org.orbisgis.core.layerModel.DefaultMapContext;
import org.orbisgis.core.layerModel.IDisplayable;
import org.orbisgis.core.layerModel.ILayer;
import org.orbisgis.core.layerModel.LayerCollectionEvent;
import org.orbisgis.core.layerModel.LayerException;
import org.orbisgis.core.layerModel.LayerListener;
import org.orbisgis.core.layerModel.LayerListenerAdapter;
import org.orbisgis.core.layerModel.LayerListenerEvent;
import org.orbisgis.core.layerModel.MapContext;
import org.orbisgis.core.layerModel.MapContextListener;
import org.orbisgis.core.layerModel.SelectionEvent;
import org.orbisgis.progress.IProgressMonitor;
import org.orbisgis.progress.NullProgressMonitor;

public class GeocognitionMapContext extends AbstractExtensionElement implements
		GeocognitionExtensionElement{

	private MapContext mapContext;
	private Object revertStatus;
	private ChangeListener changeListener;
	private GeocognitionElementContentListener elementListener;		
	
	public GeocognitionMapContext(MapContext map,
			GeocognitionElementFactory factory) {
		super(factory);
		this.mapContext = map;
	}

	public GeocognitionMapContext(Object xmlObject,
			GeocognitionElementFactory factory) {
		super(factory);
		mapContext = new DefaultMapContext();
		mapContext.setJAXBObject(xmlObject);
		revertStatus = xmlObject;	
	}

	@Override
	public Object getJAXBObject() {
		return mapContext.getJAXBObject();
	}

	@Override
	public Object getObject() throws UnsupportedOperationException {
		return mapContext;
	}

	@Override
	public String getTypeId() {
		return OrbisGISPersitenceConfig.GeocognitionMapContextFactory_id;
	}

	@Override
	public void close(IProgressMonitor progressMonitor)
			throws UnsupportedOperationException {
		mapContext.removeMapContextListener(changeListener);
		mapContext.removeLayerListenerRecursively(changeListener);
		mapContext.close(progressMonitor);		
		mapContext.setJAXBObject(revertStatus);
	}

	@Override
	public void open(IProgressMonitor progressMonitor)
			throws UnsupportedOperationException, EditableElementException {
		try {
			if (revertStatus == null) {
				revertStatus = mapContext.getJAXBObject();
			}

			mapContext.open(progressMonitor);
			changeListener = new ChangeListener();
			mapContext.addMapContextListener(changeListener);
			mapContext.addLayerListenerRecursively(changeListener);
		} catch (LayerException e) {
			throw new EditableElementException("Cannot open map", e);
		}
	}

	@Override
	public void save() throws UnsupportedOperationException {
		revertStatus = mapContext.getJAXBObject();
	}

	public boolean isModified() {
		ILayer[] layers = mapContext.getLayers();
		for (ILayer layer : layers) {
			if ((layer.getDataSource() != null)
					&& layer.getDataSource().isModified()) {
				return true;
			}
		}
		return false;
	}

	/**
	 * This listener can be associated both to layers and to mapcontexts.
	 * When a change occurs in such objects, it is reported to this
	 * GeocognitionMapContext by using the method fireContentChanged.
	 */
	private final class ChangeListener extends LayerListenerAdapter implements
			MapContextListener, LayerListener{
		@Override
		public void layerSelectionChanged(MapContext mapContext) {
			fireContentChanged();
		}

		@Override
		public void activeLayerChanged(IDisplayable previousActiveLayer,
				MapContext mapContext) {
			fireContentChanged();
		}

		@Override
		public void layerAdded(final LayerCollectionEvent e) {
			for (final IDisplayable layer : e.getAffected()) {
				layer.addLayerListenerRecursively(changeListener);
			}
			fireContentChanged();
		}

		@Override
		public void layerMoved(LayerCollectionEvent e) {
			fireContentChanged();
		}

		@Override
		public void layerRemoved(LayerCollectionEvent e) {
			for (final IDisplayable layer : e.getAffected()) {
				layer.removeLayerListenerRecursively(changeListener);
			}
			fireContentChanged();
		}

		@Override
		public void nameChanged(LayerListenerEvent e) {
			fireContentChanged();
		}

		@Override
		public void selectionChanged(SelectionEvent e) {
			fireContentChanged();
		}

		@Override
		public void styleChanged(LayerListenerEvent e) {
			fireContentChanged();
		}

		@Override
		public void visibilityChanged(LayerListenerEvent e) {
			fireContentChanged();
		}
	}

	private void fireContentChanged() {
		this.elementListener.contentChanged();
	}

	@Override
	public Object getRevertJAXBObject() {
		return revertStatus;
	}

	@Override
	public void setElementListener(GeocognitionElementContentListener listener) {
		this.elementListener = listener;
	}

	@Override
	public void setJAXBObject(Object jaxbObject) throws GeocognitionException {
		// Set the content
		boolean isOpen = mapContext.isOpen();
		if (isOpen) {
			mapContext.close(new NullProgressMonitor());
		}
		Object previousJAXBObject = mapContext.getJAXBObject();
		mapContext.setJAXBObject(jaxbObject);
		if (isOpen) {
			try {
				mapContext.open(null);
			} catch (IllegalStateException e) {
				mapContext.setJAXBObject(previousJAXBObject);
				throw new GeocognitionException("Cannot reopen map", e);
			} catch (LayerException e) {
				mapContext.setJAXBObject(previousJAXBObject);
				throw new GeocognitionException("Cannot reopen map", e);
			}
		}
	}
}
