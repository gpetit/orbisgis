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

import java.awt.Rectangle;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.dnd.DragGestureEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.JComponent;
import javax.swing.JPopupMenu;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.TreePath;

import org.gdms.data.DataSource;
import org.gdms.data.DataSourceListener;
import org.gdms.data.edition.EditionEvent;
import org.gdms.data.edition.EditionListener;
import org.gdms.data.edition.MultipleEditionEvent;
import org.gdms.driver.DriverException;
import org.orbisgis.core.DataManager;
import org.orbisgis.core.Services;
import org.orbisgis.core.background.BackgroundJob;
import org.orbisgis.core.background.BackgroundManager;
import org.orbisgis.core.edition.EditableElement;
import org.orbisgis.core.layerModel.DefaultMapContext;
import org.orbisgis.core.layerModel.IDisplayable;
import org.orbisgis.core.layerModel.ILayer;
import org.orbisgis.core.layerModel.LayerCollection;
import org.orbisgis.core.layerModel.LayerCollectionEvent;
import org.orbisgis.core.layerModel.LayerException;
import org.orbisgis.core.layerModel.LayerListener;
import org.orbisgis.core.layerModel.LayerListenerEvent;
import org.orbisgis.core.layerModel.MapContext;
import org.orbisgis.core.layerModel.MapContextListener;
import org.orbisgis.core.layerModel.SelectionEvent;
import org.orbisgis.core.renderer.legend.Legend;
import org.orbisgis.core.ui.components.resourceTree.MyTreeUI;
import org.orbisgis.core.ui.components.resourceTree.ResourceTree;
import org.orbisgis.core.ui.editor.IEditor;
import org.orbisgis.core.ui.editorViews.toc.TocTreeModel.LegendNode;
import org.orbisgis.core.ui.pluginSystem.workbench.WorkbenchFrame;
import org.orbisgis.core.ui.plugins.views.MapEditorPlugIn;
import org.orbisgis.core.ui.plugins.views.editor.EditorManager;
import org.orbisgis.core.ui.plugins.views.geocatalog.TransferableSource;
import org.orbisgis.progress.IProgressMonitor;

/**
 *
 * @author alexis
 */
public class Toc extends ResourceTree implements WorkbenchFrame {

	private MyLayerListener ll;

	private TocRenderer tocRenderer;

	private TocTreeModel treeModel;

	private boolean ignoreSelection = false;

	private MyMapContextListener myMapContextListener;

	private MapContext mapContext = null;

	private EditableElement element = null;

	private MapEditorPlugIn mapEditor;

	private org.orbisgis.core.ui.pluginSystem.menu.MenuTree menuTree;

	public Toc() {
		mapContext = new DefaultMapContext();
		try{
			mapContext.open(null);
		} catch (LayerException e ){
			Services.getErrorManager().error("Cannot create mapcontext", e);
		}
		menuTree = new org.orbisgis.core.ui.pluginSystem.menu.MenuTree();
		this.ll = new MyLayerListener();

		tocRenderer = new TocRenderer(this);
		DataManager dataManager = (DataManager) Services
				.getService(DataManager.class);
		treeModel = new TocTreeModel(mapContext, getTree());
		this.setModel(treeModel);
		this.setTreeCellRenderer(tocRenderer);
		this.setTreeCellEditor(new TocEditor(tree));

		tree.addMouseListener(new MouseAdapter() {

			@Override
			public void mouseClicked(MouseEvent e) {
				final int x = e.getX();
				final int y = e.getY();
				final int mouseButton = e.getButton();
				TreePath path = tree.getPathForLocation(x, y);
				Rectangle layerNodeLocation = Toc.this.tree.getPathBounds(path);

				if (path != null) {
					if (path.getLastPathComponent() instanceof ILayer) {
						ILayer layer = (ILayer) path.getLastPathComponent();
						Rectangle checkBoxBounds = tocRenderer
								.getCheckBoxBounds();
						checkBoxBounds.translate(
								(int) layerNodeLocation.getX(),
								(int) layerNodeLocation.getY());
						if ((checkBoxBounds.contains(e.getPoint()))
								&& (MouseEvent.BUTTON1 == mouseButton)
								&& (1 == e.getClickCount())) {
							// mouse click inside checkbox

							try {
								layer.setVisible(!layer.isVisible());

								tree.repaint();
							} catch (LayerException e1) {
							}
						}
					} else if (path.getLastPathComponent() instanceof LegendNode) {

						LegendNode legendNode = (LegendNode) path
								.getLastPathComponent();
						Rectangle checkBoxBounds = tocRenderer
								.getCheckBoxBounds();
						checkBoxBounds.translate(
								(int) layerNodeLocation.getX(),
								(int) layerNodeLocation.getY());
						if ((checkBoxBounds.contains(e.getPoint()))
								&& (MouseEvent.BUTTON1 == mouseButton)
								&& (1 == e.getClickCount())) {
							try {
								Legend legend = legendNode.getLayer()
										.getRenderingLegend()[legendNode
										.getLegendIndex()];
								if (!legend.isVisible()) {
									legend.setVisible(true);
								} else {
									legend.setVisible(false);
								}
								IDisplayable layer = legendNode.getLayer();
								if (layer.isVisible()) {
									layer.setVisible(true);
								}
								tree.repaint();
							} catch (DriverException e1) {
								e1.printStackTrace();
							} catch (LayerException e1) {
								e1.printStackTrace();
							}

						}
					}
				}
			}

		});

		myMapContextListener = new MyMapContextListener();

		this.getTree().getSelectionModel().addTreeSelectionListener(
				new TreeSelectionListener() {

					public void valueChanged(TreeSelectionEvent e) {
						if (!ignoreSelection) {
							TreePath[] selectedPaths = Toc.this.getSelection();
							ArrayList<ILayer> layers = getSelectedLayers(selectedPaths);

							ignoreSelection = true;
							mapContext.setSelectedLayers(layers
									.toArray(new ILayer[layers.size()]));
							ignoreSelection = false;
						}
					}

				});
	}


	public MapContext getMapContext() {
		return mapContext;
	}

	public org.orbisgis.core.ui.pluginSystem.menu.MenuTree getMenuTreePopup() {
		return menuTree;
	}

	@Override
	public JPopupMenu getPopup() {
		JPopupMenu newPopup = new JPopupMenu();
		JComponent[] menus = menuTree.getJMenus();
		for (JComponent menu : menus) {
			newPopup.add(menu);
		}
		return newPopup;
	}

	@Override
	public boolean doDrop(Transferable trans, Object node) {
		IDisplayable dropNode;
		MapContext mc=(MapContext) treeModel.getRoot();
		if (node instanceof TocTreeModel.LegendNode) {
			dropNode = ((TocTreeModel.LegendNode) node).getLayer();
		} else {
			dropNode = (ILayer) node;
		}
		try {
			if (trans.isDataFlavorSupported(TransferableLayer.getLayerFlavor())) {
				IDisplayable[] draggedLayers = (IDisplayable[]) trans
						.getTransferData(TransferableLayer.getLayerFlavor());
				//We are targeting an actual node, not the mapContext.
				if(dropNode!=null){
					//First case : the targeted node is a collection.
					//We can insert new nodes in it only if they are not LayerCollections too.
					if (dropNode.isCollection()) {
						for (IDisplayable layer : draggedLayers) {
							try {
								if(!layer.isCollection()){
									((ILayer)layer).moveTo((LayerCollection) dropNode);
								} else {
					//If layer is a collection, we insert it just before dropNode
									mc.moveLayerBefore(layer, dropNode);
								}
							} catch (LayerException e) {
								Services.getErrorManager().error("Cannot move layer", e);
							}
						}
					} else {
						IDisplayable parent = dropNode.getParent();
						if (parent != null) {
					//dropNode is an IDisplayable which has a parent, so it is an ILayer
					//We can only organize sublayers that are in the same LayerCollection.
					//It means that if we want our drop to have some effect, the dropped node
					//must come from the same LayerCollection than the targeted one.
							for (IDisplayable layer : draggedLayers) {
								if (layer.getParent() == dropNode.getParent()) {
									int index = ((LayerCollection)parent).getIndex((ILayer)dropNode);
									try {
										((ILayer) layer).moveTo((LayerCollection)parent, index);
									} catch (LayerException e) {
										Services.getErrorManager().error(
												"Cannot move layer: "
														+ layer.getName());
									}
								}
							}
						} else {
					//dropNode is not a collection and doesn't have any parent. Conesquently, it is
					//an orphan ILayer. Artifacts that are dropped on it are placed just before,
					//if and only if they don't have a paren (Such an artifact is a LayerCollection,
					//or another orphan ILayer).
							for(IDisplayable layer : draggedLayers){
								try{
									if(layer.getParent() == null){
										mc.moveLayerBefore(layer, dropNode);
									}
								} catch(LayerException e){
									Services.getErrorManager().error("Cannot move layer", e);
								}
							}
						}
					}
				} else {
					for(IDisplayable layer : draggedLayers){
						try{
							if(mc.getLayerModel().contains(layer)){
								mc.remove(layer);
								mc.add(layer);
							} else {
								mc.add(layer);
								if(layer.getParent() != null && layer instanceof ILayer){
									((ILayer) layer).setParent(null);
								}
							}
						}catch(LayerException e){
							Services.getErrorManager().error("Cannot move layer", e);
						}
					}
				}
			} else if (trans.isDataFlavorSupported(TransferableSource
					.getResourceFlavor())) {
				final String[] draggedResources = (String[]) trans
						.getTransferData(TransferableSource.getResourceFlavor());
				BackgroundManager bm = (BackgroundManager) Services
						.getService(BackgroundManager.class);
				bm.backgroundOperation(new MoveProcess(draggedResources,
						dropNode));
			} else {
				return false;
			}
		} catch (UnsupportedFlavorException e1) {
			throw new RuntimeException("You shouldn't try to manage such objects here", e1);
		} catch (IOException e1) {
			throw new RuntimeException("The data is no longer available :-(", e1);
		}
		return true;
	}

	public Transferable getDragData(DragGestureEvent dge) {
		TreePath[] resources = getSelection();
		ArrayList<ILayer> layers = getSelectedLayers(resources);
		if (layers.size() == 0) {
			return null;
		} else {
			return new TransferableLayer(element, layers.toArray(new ILayer[layers.size()]));
		}
	}

	public void delete() {
		if (mapContext != null) {
			mapContext.removeMapContextListener(myMapContextListener);
		}
	}

	public void setMapContext(EditableElement element) {

		// Remove the listeners
		if (this.mapContext != null) {
			for(IDisplayable dis : this.mapContext.getLayerModel()){
			removeLayerListenerRecursively(dis, ll);
			}
			this.mapContext.removeMapContextListener(myMapContextListener);
		}

		if (element != null) {
			this.mapContext = ((MapContext) element.getObject());
			this.element = element;
			// Add the listeners to the new MapContext
			this.mapContext.addMapContextListener(myMapContextListener);
			for(IDisplayable dis : this.mapContext.getLayerModel()){
				addLayerListenerRecursively(dis, ll);
			}
			treeModel = new TocTreeModel(mapContext, tree);
			// Set model clears selection
			ignoreSelection = true;
			Toc.this.setModel(treeModel);
			ignoreSelection = false;
			setTocSelection(Toc.this.mapContext);
			Toc.this.repaint();
		} else {
			// Remove the references to the mapContext
			DataManager dataManager = (DataManager) Services
					.getService(DataManager.class);
			treeModel = new TocTreeModel(mapContext, getTree());
			ignoreSelection = true;
			this.setModel(treeModel);
			ignoreSelection = false;
			this.mapContext = null;
			this.element = null;

			// Patch to remove any reference to the previous model
			myTreeUI = new MyTreeUI();
			((MyTreeUI) tree.getUI()).dispose();
			tree.setUI(myTreeUI);
		}

	}

	boolean isActive(IDisplayable layer) {
		if (mapContext != null) {
			return layer == mapContext.getActiveLayer();
		} else {
			return false;
		}
	}

	public void setMapContext(EditableElement element, MapEditorPlugIn mapEditor) {
		this.mapEditor = mapEditor;
		setMapContext(element);
	}

	///////////////***Protected methods***///////////////

	@Override
	protected boolean isDroppable(TreePath path) {
		return path.getLastPathComponent() instanceof ILayer;
	}

	///////////////***Private methods***////////////////

	private ArrayList<ILayer> getSelectedLayers(TreePath[] selectedPaths) {
		ArrayList<ILayer> layers = new ArrayList<ILayer>();
		for (int i = 0; i < selectedPaths.length; i++) {
			Object lastPathComponent = selectedPaths[i].getLastPathComponent();
			if (lastPathComponent instanceof ILayer) {
				layers.add((ILayer) lastPathComponent);
			}
		}
		return layers;
	}

	/**
	 * This method will add a layerlistener to rootlayer and its children.
	 * @param rootLayer
	 * @param refreshLayerListener
	 */
	private void addLayerListenerRecursively(IDisplayable rootLayer,
			MyLayerListener refreshLayerListener) {
		rootLayer.addLayerListener(refreshLayerListener);
		DataSource dataSource = rootLayer.getDataSource();
		if (dataSource != null) {
			dataSource.addEditionListener(refreshLayerListener);
			dataSource.addDataSourceListener(refreshLayerListener);
		}
		if(rootLayer.isCollection()){
			for (IDisplayable dis : ((LayerCollection) rootLayer).getLayerCollection()) {
				addLayerListenerRecursively(dis,refreshLayerListener);
			}
		}
	}

	/**
	 * This method will remove a LayerListener from rootLayer and its children
	 * @param rootLayer
	 * @param refreshLayerListener
	 */
	private void removeLayerListenerRecursively(IDisplayable rootLayer,
			MyLayerListener refreshLayerListener) {
		rootLayer.removeLayerListener(refreshLayerListener);
		DataSource dataSource = rootLayer.getDataSource();
		if (dataSource != null) {
			dataSource.removeEditionListener(refreshLayerListener);
			dataSource.removeDataSourceListener(refreshLayerListener);
		}
		if(rootLayer.isCollection()){
			for (IDisplayable dis : ((LayerCollection) rootLayer).getLayerCollection()) {
				removeLayerListenerRecursively(dis, refreshLayerListener);
			}
		}
	}

	private void setTocSelection(MapContext mapContext) {
		ILayer[] selected = mapContext.getSelectedLayers();
		TreePath[] selectedPaths = new TreePath[selected.length];
		for (int i = 0; i < selectedPaths.length; i++) {
			selectedPaths[i] = new TreePath(selected[i].getLayerPath());
		}

		ignoreSelection = true;
		this.setSelection(selectedPaths);
		ignoreSelection = false;
	}

	////////////////////Private classes////////////////////////

	private class MoveProcess implements BackgroundJob {

		private IDisplayable dropNode;
		private String[] draggedResources;

		public MoveProcess(String[] draggedResources, IDisplayable dropNode) {
			this.draggedResources = draggedResources;
			this.dropNode = dropNode;
		}

		@Override
		public void run(IProgressMonitor pm) {
			int index;
			if (!dropNode.isCollection()) {
				IDisplayable parent = dropNode.getParent();
				if (parent!=null) {
					index = ((LayerCollection)parent).getIndex((ILayer) dropNode);
					dropNode = parent;
				} else {
					Services.getErrorManager().error(
							"Cannot create layer on " + dropNode.getName());
					return;
				}
			} else {
				index = ((LayerCollection) dropNode).getLayerCount();
			}
			DataManager dataManager = (DataManager) Services
					.getService(DataManager.class);
			for (int i = 0; i < draggedResources.length; i++) {
				String sourceName = draggedResources[i];
				if (pm.isCancelled()) {
					break;
				} else {
					pm.progressTo(100 * i / draggedResources.length);
					try {
						((LayerCollection) dropNode).insertLayer( dataManager
								.createLayer(sourceName, dropNode.getMapContext()), index);
					} catch (LayerException e) {
						throw new RuntimeException("Cannot "
								+ "add the layer to the destination", e);
					}
				}
			}
		}

		@Override
		public String getTaskName() {
			return "Importing resources";
		}

	}

	private class MyLayerListener implements LayerListener, EditionListener,
			DataSourceListener {

		@Override
		public void layerAdded(final LayerCollectionEvent e) {
			for (final IDisplayable layer : e.getAffected()) {
				addLayerListenerRecursively(layer, ll);
			}
			treeModel.refresh();
		}

		@Override
		public void layerMoved(LayerCollectionEvent e) {
			treeModel.refresh();
		}

		@Override
		public boolean layerRemoving(LayerCollectionEvent e) {
			// Close editors
			for (final IDisplayable layer : e.getAffected()) {
				IDisplayable[] layers = new IDisplayable [] { layer };
				if (layer.isCollection()) {
					layers = layer.getLayers();
				}
				for (IDisplayable lyr : layers) {
					EditorManager em = Services.getService(EditorManager.class);
					IEditor[] editors = em.getEditor(new EditableLayer(element,
							lyr));
					for (IEditor editor : editors) {
						if (!em.closeEditor(editor)) {
							return false;
						}
					}
				}
			}
			return true;
		}

		@Override
		public void layerRemoved(final LayerCollectionEvent e) {
			for (final IDisplayable layer : e.getAffected()) {
				removeLayerListenerRecursively(layer, ll);
			}
			treeModel.refresh();
		}

		@Override
		public void nameChanged(LayerListenerEvent e) {
		}

		@Override
		public void styleChanged(LayerListenerEvent e) {
			treeModel.refresh();
		}

		@Override
		public void visibilityChanged(LayerListenerEvent e) {
			treeModel.refresh();
		}

		@Override
		public void selectionChanged(SelectionEvent e) {
			treeModel.refresh();
		}

		@Override
		public void multipleModification(MultipleEditionEvent e) {
			treeModel.refresh();
		}

		@Override
		public void singleModification(EditionEvent e) {
			treeModel.refresh();
		}

		@Override
		public void cancel(DataSource ds) {
		}

		@Override
		public void commit(DataSource ds) {
			treeModel.refresh();
		}

		@Override
		public void open(DataSource ds) {
			treeModel.refresh();
		}

	}

	private final class MyMapContextListener implements MapContextListener {
		@Override
		public void layerSelectionChanged(MapContext mapContext) {
			setTocSelection(mapContext);
		}

		@Override
		public void activeLayerChanged(IDisplayable previousActiveLayer,
				MapContext mapContext) {
			treeModel.refresh();
		}
	}
}
