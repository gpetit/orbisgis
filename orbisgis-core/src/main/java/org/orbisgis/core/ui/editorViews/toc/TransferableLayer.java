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

import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;
import java.util.ArrayList;

import org.orbisgis.core.edition.EditableElement;
import org.orbisgis.core.layerModel.IDisplayable;
import org.orbisgis.core.layerModel.ILayer;
import org.orbisgis.core.ui.plugins.views.editor.TransferableEditableElement;

public class TransferableLayer implements Transferable {

	private static DataFlavor layerFlavor = new DataFlavor(IDisplayable.class,
			"Resource");

	private IDisplayable[] nodes = null;
	private EditableElement element;

	public TransferableLayer(EditableElement element, IDisplayable[] node) {
		this.element = element;
		ArrayList<IDisplayable> nodes = new ArrayList<IDisplayable>();
		for (int i = 0; i < node.length; i++) {
			if (!contains(nodes, node[i])) {
				removeContained(nodes, node[i]);
				nodes.add(node[i]);
			}
		}
		this.nodes = nodes.toArray(new ILayer[nodes.size()]);
	}

    @Override
	public Object getTransferData(DataFlavor flavor)
			throws UnsupportedFlavorException, IOException {
		Object ret = null;
		if (flavor.equals(layerFlavor)) {
			ret = nodes;
		} else if (flavor
				.equals(TransferableEditableElement.editableElementFlavor)) {
			EditableElement[] elems = new EditableElement[nodes.length];
			for (int i = 0; i < nodes.length; i++) {
				elems[i] = new EditableLayer(element, nodes[i]);
			}
			ret = elems;
		} else if (flavor.equals(DataFlavor.stringFlavor)) {
			String retString = "";
			String separator = "";
			for (IDisplayable node : nodes) {
				retString = retString + separator + node.getName();
				separator = ", ";
			}
			ret = retString;
		}

		return ret;
	}

    @Override
	public DataFlavor[] getTransferDataFlavors() {
		return (new DataFlavor[] { layerFlavor,
				TransferableEditableElement.editableElementFlavor,
				DataFlavor.stringFlavor });
	}

    @Override
	public boolean isDataFlavorSupported(DataFlavor flavor) {
		return flavor.equals(TransferableEditableElement.editableElementFlavor)
				|| flavor.equals(getLayerFlavor())
				|| flavor.equals(DataFlavor.stringFlavor);
	}

    /**
     * Get the data flavor which is used to represent layers.
     * @return
     */
	public static DataFlavor getLayerFlavor() {
		return layerFlavor;
	}

        /**
         * Get the nodes contained in this transferable layer.
         * @return
         */
	public IDisplayable[] getNodes() {
		return nodes;
	}

        /*
         * Check whether the IDIsplayable resource is a direct child of a node of the list nodes.
         */
	private boolean contains(ArrayList<IDisplayable> nodes, IDisplayable resource) {
            for(IDisplayable dis : nodes){
                IDisplayable[] subtree = dis.getLayers();
                for (IDisplayable descendant : subtree) {
                        if (descendant == resource) {
                                return true;
                        }
                }
            }
		return false;
	}

        /*
         * This method is recursive. During the first execution, it will remove resource
         * from nodes. Then, it will remove all the IDisplayable objects that are contained
         * both in nodes and in another node of nodes.
         */
	private void removeContained(ArrayList<IDisplayable> nodes, IDisplayable resource) {
		for (IDisplayable dis : nodes) {
			if (resource == dis) {
				nodes.remove(dis);
			} else {
				ILayer[] children = resource.getLayers();
				for (ILayer child : children) {
					removeContained(nodes, child);
				}
			}
		}
	}

}
