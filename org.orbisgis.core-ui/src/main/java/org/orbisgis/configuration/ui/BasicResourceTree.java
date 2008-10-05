package org.orbisgis.configuration.ui;

import java.awt.datatransfer.Transferable;
import java.awt.dnd.DragGestureEvent;

import javax.swing.JPopupMenu;

import org.orbisgis.ui.resourceTree.ResourceTree;

/**
 * Resource tree that doesn't allow drag and drop or popup menu
 */
class BasicResourceTree extends ResourceTree {
	@Override
	protected boolean doDrop(Transferable trans, Object node) {
		return false;
	}

	@Override
	protected Transferable getDragData(DragGestureEvent dge) {
		return null;
	}

	@Override
	public JPopupMenu getPopup() {
		return null;
	}
}
