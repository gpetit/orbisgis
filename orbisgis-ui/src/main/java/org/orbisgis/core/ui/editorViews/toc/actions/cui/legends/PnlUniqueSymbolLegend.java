/**
 * OrbisGIS is a GIS application dedicated to scientific spatial simulation.
 * This cross-platform GIS is developed at French IRSTV institute and is able to
 * manipulate and create vector and raster spatial information.
 *
 * OrbisGIS is distributed under GPL 3 license. It is produced by the "Atelier SIG"
 * team of the IRSTV Institute <http://www.irstv.fr/> CNRS FR 2488.
 *
 * Copyright (C) 2007-1012 IRSTV (FR CNRS 2488)
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
 * or contact directly:
 * info_at_ orbisgis.org
 */
/*
 * JPanelSimpleLegend.java
 *
 * Created on 22 de febrero de 2008, 16:33
 */

package org.orbisgis.core.ui.editorViews.toc.actions.cui.legends;

import java.awt.BorderLayout;
import java.awt.Component;
import java.net.URL;

import org.orbisgis.core.renderer.legend.Legend;
import org.orbisgis.core.renderer.legend.carto.LegendFactory;
import org.orbisgis.core.renderer.legend.carto.UniqueSymbolLegend;
import org.orbisgis.core.renderer.symbol.Symbol;
import org.orbisgis.core.sif.UIFactory;
import org.orbisgis.core.sif.UIPanel;
import org.orbisgis.core.ui.editorViews.toc.actions.cui.ConstraintSymbolFilter;
import org.orbisgis.core.ui.editorViews.toc.actions.cui.LegendContext;
import org.orbisgis.core.ui.editorViews.toc.actions.cui.SymbolBuilder;
import org.orbisgis.core.ui.editorViews.toc.actions.cui.SymbolFilter;
import org.orbisgis.core.ui.editorViews.toc.actions.cui.legend.ILegendPanel;

/**
 * 
 * @author david
 */
public class PnlUniqueSymbolLegend extends javax.swing.JPanel implements
		ILegendPanel, UIPanel {

	private UniqueSymbolLegend leg = null;
	private SymbolBuilder symbolEditor;
	private LegendContext legendContext;

	private SymbolFilter getSymbolFilter() {
		return new ConstraintSymbolFilter(legendContext.getGeometryTypeConstraint());
	}

	/**
	 * Returns an UniqueSymbolLegend with a composite symbol with all the
	 * symbols in the list.
	 */
	public Legend getLegend() {
		Symbol sym = symbolEditor.getSymbolComposite();
		leg.setSymbol(sym);

		return leg;
	}

	public Component getComponent() {
		return this;
	}

	public String getInfoText() {
		return "Set a Unique symbol legend to the selected layer";
	}

	public String getTitle() {
		return "Unique symbol legend";
	}

	public URL getIconURL() {
		return UIFactory.getDefaultIcon();
	}

	public String initialize() {
		return null;
	}

	public String postProcess() {
		return null;
	}

	public String validateInput() {
		if ((symbolEditor.getSymbolComposite() == null)
				|| (symbolEditor.getSymbolComposite().getSymbolCount() == 0)) {
			return "At least one symbol must be created";
		}
		return null;
	}

	public boolean acceptsGeometryType(int geometryType) {
		return true;
	}

	public ILegendPanel newInstance() {
		return new PnlUniqueSymbolLegend();
	}

	public void setLegend(Legend legend) {
		this.leg = (UniqueSymbolLegend) legend;
		symbolEditor.setSymbol(leg.getSymbol());
	}

	public void initialize(LegendContext lc) {
		leg = LegendFactory.createUniqueSymbolLegend();
		leg.setName(leg.getLegendTypeName());
		this.legendContext = lc;
		symbolEditor = new SymbolBuilder(true, legendContext, getSymbolFilter());
		this.setLayout(new BorderLayout());
		this.add(symbolEditor, BorderLayout.CENTER);
	}

}