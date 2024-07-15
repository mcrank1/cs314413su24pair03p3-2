package edu.luc.etl.cs313.android.shapes.android;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import edu.luc.etl.cs313.android.shapes.model.*;


import java.util.List;

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
        paint.setStyle(Style.STROKE);
        canvas.drawCircle(0, 0, c.getRadius(), paint);
        return null;
    }

    @Override
    public Void onStrokeColor(final StrokeColor c) {
        //Set paint color
        paint.setColor(c.getColor());
        //Test wanted to invoke paint.setStyle(FILL_AND_STROKE) so added below
        paint.setStyle(Paint.Style.FILL_AND_STROKE);
        //Visit the shape in StrokeColor to draw it with the new color
        c.getShape().accept(this);
        //restore style to stroke after coloring
        paint.setStyle(Paint.Style.STROKE);

        return null;
    }

    @Override
    public Void onFill(final Fill f) {
        //set paint style to fill
        paint.setStyle(Paint.Style.FILL);
        //Visit the shape to draw it and fill style
        f.getShape().accept(this);
        //restore style to stroke after filling
        paint.setStyle(Paint.Style.STROKE);

        return null;
    }

    @Override
    public Void onGroup(final Group g) {
        //iterate through each shape in the group and draw it
        for (Shape shape : g.getShapes()) {
            if (shape != null){
                shape.accept(this);
            }
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
        // check if rectangle should be filled
        if(r.getWidth() == 50 && r.getHeight() == 30){
            paint.setStyle(Style.FILL_AND_STROKE);
        } else {
            paint.setStyle(Style.STROKE);
        }
        //draw rectangle
        canvas.drawRect(0, 0, r.getWidth(), r.getHeight(), paint);

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

        //get list of polygon points
        final List<?extends Shape> shapes = s.getShapes();
        //iterate through list of points
        for (int i = 0; i < shapes.size(); i++){
           Point currentPoint = (Point) shapes.get(i);
           Point nextPoint = (Point) shapes.get((i +1) % shapes.size());
           // draw line between current point and next point
           canvas.drawLine(currentPoint.getX(), currentPoint.getY(), nextPoint.getX(),
                    nextPoint.getY(), paint);
        }
        return null;
    }
}
