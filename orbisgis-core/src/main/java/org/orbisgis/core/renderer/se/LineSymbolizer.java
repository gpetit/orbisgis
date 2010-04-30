package org.orbisgis.core.renderer.se;


import java.awt.Graphics2D;
import java.io.IOException;

import org.gdms.data.DataSource;
import org.orbisgis.core.renderer.liteShape.LiteShape;
import org.orbisgis.core.renderer.se.common.Uom;
import org.orbisgis.core.renderer.se.parameter.ParameterException;
import org.orbisgis.core.renderer.se.parameter.real.RealParameter;
import org.orbisgis.core.renderer.se.stroke.PenStroke;
import org.orbisgis.core.renderer.se.stroke.Stroke;

/**
 * Define a style for line features (or area feature's perimeter...)
 * Only conains a stroke
 *
 * @todo add perpendicular offset
 *
 * @author maxence
 */
public class LineSymbolizer extends VectorSymbolizer {

    public LineSymbolizer(){
        super();
        uom = Uom.MM;
        stroke = new PenStroke();
    }

    public Stroke getStroke() {
        return stroke;
    }

    public void setStroke(Stroke stroke) {
        this.stroke = stroke;
        stroke.setParent(this);
    }


    public RealParameter getPerpendicularOffset() {
        return perpendicularOffset;
    }

    public void setPerpendicularOffset(RealParameter perpendicularOffset) {
        this.perpendicularOffset = perpendicularOffset;
    }


    /**
     *
     * @param g2
     * @param ds
     * @param fid
     * @throws ParameterException
     * @todo make sure the geom is a line or an area; implement p_offset
     */
    @Override
    public void draw(Graphics2D g2, DataSource ds, int fid) throws ParameterException, IOException{
        if (stroke != null){
            LiteShape shp = this.getLiteShape(ds, fid);

            if (perpendicularOffset != null){
                double offset = perpendicularOffset.getValue(ds, fid);
                // apply perpendicular offset
            }


            // TODO perpendicular offset !
            stroke.draw(g2, shp, ds, fid);
        }
    }

    private RealParameter perpendicularOffset;
    private Stroke stroke;
}