package org.orbisgis.editorViews.toc.actions.cui.gui.widgets;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Stroke;
import java.awt.geom.AffineTransform;

import javax.swing.JPanel;

import org.gdms.data.types.GeometryConstraint;
import org.gdms.driver.DriverException;
import org.orbisgis.renderer.RenderPermission;
import org.orbisgis.renderer.legend.CircleSymbol;
import org.orbisgis.renderer.legend.LineSymbol;
import org.orbisgis.renderer.legend.PolygonSymbol;
import org.orbisgis.renderer.legend.Symbol;
import org.orbisgis.renderer.legend.SymbolComposite;
import org.orbisgis.renderer.legend.SymbolFactory;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Envelope;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.LinearRing;
import com.vividsolutions.jts.geom.impl.CoordinateArraySequence;

public class Canvas extends JPanel {

	Symbol s;
	int constraint;
	boolean isSelected=false;

	public Canvas( ){
		super();
		s = SymbolFactory.createNullSymbol();
		constraint=GeometryConstraint.MIXED;
		this.setSize(126, 70);
	}

	@Override
	 public void paintComponent(Graphics g) {
		g.setColor(Color.white);
		g.fillRect(0, 0, getWidth(), getHeight());

		GeometryFactory gf = new GeometryFactory();
		Geometry geom = null;
		constraint=getConstraint(s);

		try {
			Stroke st = new BasicStroke();
			 if (isSelected){
				 g.setColor(Color.BLUE);
				 st = ((Graphics2D)g).getStroke();
				 ((Graphics2D)g).setStroke(new BasicStroke(new Float(2.0)));
			 }else{
				 g.setColor(Color.GRAY);
			 }
			 g.drawRect(1, 1, 124, 68); //Painting a Rectangle for the presentation and selection

			 ((Graphics2D)g).setStroke(st);

			switch (constraint) {
				case GeometryConstraint.LINESTRING:
				case GeometryConstraint.MULTI_LINESTRING:
					geom = gf.createLineString(new Coordinate[] {
							 new Coordinate(20, 10), new Coordinate(40, 40),
							 new Coordinate(60, 10) , new Coordinate(80, 40)});

					s.draw((Graphics2D) g, geom, new AffineTransform(), new RenderPermission() {

						 public boolean canDraw(Envelope env) {
							 return true;
						 }

					 });

					break;
				case GeometryConstraint.POINT:
				case GeometryConstraint.MULTI_POINT:
					geom = gf.createPoint(new Coordinate(25, 25));

					s.draw((Graphics2D) g, geom, new AffineTransform(), new RenderPermission() {

						 public boolean canDraw(Envelope env) {
							 return true;
						 }

					 });

					break;
				case GeometryConstraint.POLYGON:
				case GeometryConstraint.MULTI_POLYGON:
					Coordinate[] coords = {new Coordinate(15,15), new Coordinate(75,15), new Coordinate(75, 35), new Coordinate(15,35), new Coordinate(15,15)};
					CoordinateArraySequence seq = new CoordinateArraySequence( coords );
					geom = gf.createPolygon(new LinearRing( seq, gf), null);

					s.draw((Graphics2D) g, geom, new AffineTransform(), new RenderPermission() {

						 public boolean canDraw(Envelope env) {
							 return true;
						 }

					 });

					break;
				case GeometryConstraint.MIXED:
					SymbolComposite comp = (SymbolComposite) s;
					Symbol sym;
					int numberOfSymbols = comp.getSymbolCount();
					for (int i=0; i<numberOfSymbols; i++){
						sym = comp.getSymbol(i);
						if (sym instanceof LineSymbol) {
							geom = gf.createLineString(new Coordinate[] {
									 new Coordinate(20, 10), new Coordinate(40, 40),
									 new Coordinate(60, 10) , new Coordinate(80, 40)});

							sym.draw((Graphics2D) g, geom, new AffineTransform(), new RenderPermission() {

								 public boolean canDraw(Envelope env) {
									 return true;
								 }

							 });
						}

						if (sym instanceof CircleSymbol) {
							geom = gf.createPoint(new Coordinate(25, 25));

							sym.draw((Graphics2D) g, geom, new AffineTransform(), new RenderPermission() {

								 public boolean canDraw(Envelope env) {
									 return true;
								 }

							 });
						}

						if (sym instanceof PolygonSymbol) {
							Coordinate[] coordsP = {new Coordinate(15,15), new Coordinate(75,15), new Coordinate(75, 35), new Coordinate(15,35), new Coordinate(15,15)};
							CoordinateArraySequence seqP = new CoordinateArraySequence( coordsP );
							geom = gf.createPolygon(new LinearRing( seqP, gf), null);

							sym.draw((Graphics2D) g, geom, new AffineTransform(), new RenderPermission() {

								 public boolean canDraw(Envelope env) {
									 return true;
								 }

							 });
						}

					}
					break;

			}

		 } catch (DriverException e) {
			 ((Graphics2D)g).drawString("Cannot generate preview", 0, 0);
		 } catch (NullPointerException e){
			 ((Graphics2D)g).drawString("Cannot generate preview: ", 0, 0);
			 System.out.println(e.getMessage());
		 }
	}


	public void setLegend( Symbol sym, int constraint ){
		this.s = sym;
		this.constraint = getConstraint( sym );
	}

	public int getConstraint( Symbol sym ){
		if (sym instanceof LineSymbol) {
			return GeometryConstraint.LINESTRING;
		}
		if (sym instanceof CircleSymbol) {
			return GeometryConstraint.POINT;
		}
		if (sym instanceof PolygonSymbol) {
			return GeometryConstraint.POLYGON;
		}
		if (sym instanceof SymbolComposite) {
			return GeometryConstraint.MIXED;
		}
		return GeometryConstraint.MIXED;
	}

	public void setSelected(boolean selected){
		isSelected=selected;
	}

	public Symbol getSymbol(){
		return s;
	}
}