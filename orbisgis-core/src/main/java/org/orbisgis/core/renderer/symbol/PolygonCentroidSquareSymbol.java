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
package org.orbisgis.core.renderer.symbol;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.NoninvertibleTransformException;
import java.awt.geom.Point2D;

import org.gdms.driver.DriverException;
import org.orbisgis.core.map.MapTransform;
import org.orbisgis.core.renderer.RenderContext;

import com.vividsolutions.jts.geom.Envelope;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.MultiPolygon;
import com.vividsolutions.jts.geom.Point;
import com.vividsolutions.jts.geom.Polygon;
import org.gdms.data.types.Type;

public class PolygonCentroidSquareSymbol extends AbstractSquarePointSymbol {

	PolygonCentroidSquareSymbol(Color outline, int lineWidth, Color fillColor,
			int size, boolean mapUnits) {
		super(outline, lineWidth, fillColor, size, mapUnits);
	}

	@Override
	public boolean willDrawSimpleGeometry(Geometry geom) {
		return geom instanceof Polygon || geom instanceof MultiPolygon;
	}

	@Override
	public boolean acceptGeometryType(Type geomType) {
		if (geomType == null || geomType.getTypeCode() == Type.NULL) {
			return true;
		} else {
			int geometryType = geomType.getTypeCode();
			return (geometryType == Type.POLYGON)
					|| (geometryType == Type.MULTIPOLYGON);
		}
	}

	@Override
	public Envelope draw(Graphics2D g, Geometry geom, MapTransform mt,
			RenderContext permission) throws DriverException {
		Point point = geom.getInteriorPoint();
		Point2D p = new Point2D.Double(point.getX(), point.getY());
		p = mt.getAffineTransform().transform(p, null);
		int drawingSize = size;
		if (mapUnits) {
			try {
				drawingSize = (int) toPixelUnits(size, mt.getAffineTransform());
			} catch (NoninvertibleTransformException e) {
				throw new DriverException("Cannot convert to map units", e);
			}
		}
		paintSquare(g, (int) p.getX(), (int) p.getY(), drawingSize);

		return null;
	}

	@Override
	public Symbol cloneSymbol() {
		return new PolygonCentroidSquareSymbol(outline, lineWidth, fillColor,
				size, mapUnits);
	}

	@Override
	public String getClassName() {
		return "Square in polygon centroid";
	}

	@Override
	public String getId() {
		return "org.orbisgis.symbol.polygon.centroid.Square";
	}

	@Override
	public Symbol deriveSymbol(Color color) {
		return new PolygonCentroidSquareSymbol(color.darker(), lineWidth, color
				.brighter(), size, mapUnits);
	}
}
