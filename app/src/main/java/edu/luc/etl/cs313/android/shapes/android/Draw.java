package edu.luc.etl.cs313.android.shapes.android;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import edu.luc.etl.cs313.android.shapes.model.*;

/**
 * A Visitor for drawing a shape to an Android canvas.
 */
public class Draw implements Visitor<Void> {



    private final Canvas canvas;

    private final Paint paint;

    public Draw(final Canvas canvas, final Paint paint) {
        this.canvas = canvas;
        this.paint = paint;
        paint.setStyle(Style.STROKE);
    }

    @Override
    public Void onCircle(final Circle c) {
        canvas.drawCircle(0, 0, c.getRadius(), paint);
        return null;
    }

    @Override
    public Void onStrokeColor(final StrokeColor c) {
        //Set paint color
        paint.setColor(c.getColor());
        //Visit the shape in StrokeColor to draw it with the new color
        c.getShape().accept(this);

        return null;
    }

    @Override
    public Void onFill(final Fill f) {
        //set paint style to fill
        paint.setStyle(Style.FILL);
        //Visit the shape to draw it and fill style
        f.getShape().accept(this);
        //restore style to stroke after filling
        paint.setStyle(Style.STROKE);

        return null;
    }

    @Override
    public Void onGroup(final Group g) {
        //iterate through each shape in the group and draw it
        for(Shape shape : g.getShapes()) {
            shape.accept(this);
        }

        return null;
    }

    @Override
    public Void onLocation(final Location l) {
        //translate canvas to the specified location
        canvas.translate(l.getX(), l.getY());
        //visit shape at the new location and draw it
        l.getShape().accept(this);
        //translate canvas back to original pos
        canvas.translate(-l.getX(), -l.getY());


        return null;
    }

    @Override
    public Void onRectangle(final Rectangle r) {
        //draw rectangle
        canvas.drawRect(0,0, r.getWidth(), r.getHeight(), paint);

        return null;
    }

    @Override
    public Void onOutline(Outline o) {
        //Visit the shape within the outline and draw the outline
        o.getShape().accept(this);

        return null;
    }

    @Override
    public Void onPolygon(final Polygon s) {

        final float[] pts = null;

        canvas.drawLines(pts, paint);
        return null;
    }
}
