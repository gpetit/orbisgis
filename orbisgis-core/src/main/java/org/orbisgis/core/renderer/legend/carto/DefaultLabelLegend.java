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
package org.orbisgis.core.renderer.legend.carto;

import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;

import org.gdms.data.types.Type;
import org.gdms.data.values.Value;
import org.gdms.driver.DriverException;
import org.orbisgis.core.renderer.legend.Legend;
import org.orbisgis.core.renderer.legend.RenderException;
import org.orbisgis.core.renderer.legend.carto.persistence.LabelLegendType;
import org.orbisgis.core.renderer.legend.carto.persistence.LegendContainer;
import org.orbisgis.core.renderer.symbol.Symbol;
import org.orbisgis.core.renderer.symbol.SymbolFactory;
import org.orbisgis.core.ui.editorViews.toc.actions.cui.legends.GeometryProperties;
import org.orbisgis.utils.FormatUtils;

import com.vividsolutions.jts.geom.Geometry;
import org.gdms.data.DataSource;

public class DefaultLabelLegend extends AbstractCartoLegend implements
		LabelLegend {

	private String labelSizeField;

	private int fontSize = 10;

	private String fieldName;

	private boolean smartPlacing;

	private int getSize(DataSource sds, long row)
			throws RenderException, DriverException {
		if (labelSizeField == null) {
			return fontSize;
		} else {
			int fieldIndex = sds.getFieldIndexByName(labelSizeField);
			if (fieldIndex != -1) {
				throw new RenderException("The label size field '"
						+ labelSizeField + "' does not exist");
			} else {
				return sds.getFieldValue(row, fieldIndex).getAsInt();
			}
		}
	}

	public void setLabelSizeField(String fieldName) {
		this.labelSizeField = fieldName;
		fireLegendInvalid();
	}

	public String getLabelSizeField() {
		return this.labelSizeField;
	}

	public void setFontSize(int fontSize) {
		this.fontSize = fontSize;
		fireLegendInvalid();
	}

	public int getFontSize() {
		return this.fontSize;
	}

	public Symbol getSymbol(DataSource sds, long row)
			throws RenderException {
		try {
			Value v;
			if (GeometryProperties.isFieldName(fieldName)) {
				Geometry geom = sds.getGeometry(row);
				v = GeometryProperties.getPropertyValue(fieldName, geom);
			} else {
				int fieldIndex = sds.getFieldIndexByName(fieldName);
				v = sds.getFieldValue(row, fieldIndex);
			}
			int type = v.getType();
			Symbol symbol = null;
			switch (type) {
			case Type.STRING:
			case Type.DATE:
			case Type.BOOLEAN:
			case Type.TIME:
				symbol = SymbolFactory.createLabelSymbol(v.toString(),
						getSize(sds, row), smartPlacing);
				break;

			case Type.DOUBLE:
			case Type.FLOAT:
				double value = FormatUtils.round(v.getAsDouble(), 3);
				symbol = SymbolFactory.createLabelSymbol(Double.valueOf(value)
						.toString(), getSize(sds, row), smartPlacing);
				break;

			case Type.INT:
			case Type.LONG:
			case Type.SHORT:
				symbol = SymbolFactory.createLabelSymbol(v.toString(),
                                        getSize(sds, row), smartPlacing);
				break;

			default:
				break;
			}

			return symbol;
		} catch (DriverException e) {
			throw new RenderException("Cannot access layer contents" + e);
		}

	}

	public Object getJAXBObject() {
		LabelLegendType xmlLegend = new LabelLegendType();
		save(xmlLegend);
		xmlLegend.setFieldFontSize(getLabelSizeField());
		xmlLegend.setFieldName(getClassificationField());
		xmlLegend.setFontSize(getFontSize());
		xmlLegend.setSmartPlacing(smartPlacing);
		LegendContainer xml = new LegendContainer();
		xml.setLegendDescription(xmlLegend);
		return xml;
	}

	public void setJAXBObject(Object jaxbObject) {
		LegendContainer xml = (LegendContainer) jaxbObject;
		LabelLegendType xmlLegend = (LabelLegendType) xml
				.getLegendDescription();
		load(xmlLegend);
		setClassificationField(xmlLegend.getFieldName());
		setFontSize(xmlLegend.getFontSize());
		setLabelSizeField(xmlLegend.getFieldFontSize());
		setSmartPlacing(xmlLegend.isSmartPlacing());
	}

	public Legend newInstance() {
		return new DefaultLabelLegend();
	}

	public String getLegendTypeId() {
		return "org.orbisgis.legend.Label";
	}

	public String getClassificationField() {
		return fieldName;
	}

	public void setClassificationField(String fieldName) {
		this.fieldName = fieldName;
	}

	public void drawImage(Graphics2D g) {
		g.setColor(Color.black);
		FontMetrics fm = g.getFontMetrics();
		String text = getDrawingText();
		Rectangle2D r = fm.getStringBounds(text, g);
		g.drawString(text, 5, (int) (r.getHeight() * 1.2));
	}

	public int[] getImageSize(Graphics2D g) {
		FontMetrics fm = g.getFontMetrics();
		String text = getDrawingText();
		Rectangle2D r = fm.getStringBounds(text, g);
		return new int[] { 5 + (int) r.getWidth(), (int) (r.getHeight() * 1.4) };
	}

	private String getDrawingText() {
		return "abc  Label on " + fieldName;
	}

	@Override
	public String getLegendTypeName() {
		return "Label";
	}

	@Override
	public int getSymbolsToUpdateOnRowModification() {
		return ONLY_AFFECTED;
	}

	@Override
	public void setSmartPlacing(boolean placing) {
		this.smartPlacing = placing;
	}

	@Override
	public boolean isSmartPlacing() {
		return smartPlacing;
	}

}
