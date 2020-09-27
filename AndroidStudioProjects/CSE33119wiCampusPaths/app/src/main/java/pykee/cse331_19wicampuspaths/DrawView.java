package pykee.cse331_19wicampuspaths;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.v7.widget.AppCompatImageView;
import android.util.AttributeSet;
import hw8.*;

public class DrawView extends AppCompatImageView {

    private boolean drawCircle = false;
    private boolean drawPoint = false;
    private Canvas canvas;

    public DrawView(Context context) {
        super(context);
    }

    public DrawView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public DrawView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        this.canvas = canvas;
        Paint paint = new Paint();
        paint.setColor(Color.RED);

        if (drawCircle) {
            canvas.drawCircle(50.f, 50.f, 50.f, paint);
        }
    }

    public void toggleDrawCircle() {
        drawCircle = !drawCircle;
        this.invalidate();
    }

    public void drawMapPoint(Coordinate c) {
        Paint paint = new Paint();
        paint.setColor(Color.YELLOW);
        canvas.drawCircle((float)(0.23* c.getX()), (float) (0.23*c.getY()), 15.f, paint);
    }
}
