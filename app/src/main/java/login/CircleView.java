package login;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import com.odedtech.mff.client.R;

public class CircleView extends View {
    private static final int DEFAULT_CIRCLE_COLOR = Color.RED;

    private int circleColor = DEFAULT_CIRCLE_COLOR;
    private Paint paint;

    public CircleView(Context context) {
        super(context);
        init(context, null);
    }

    public CircleView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        paint = new Paint();
        paint.setAntiAlias(true);
    }

    public void setCircleColor(int circleColor) {
        this.circleColor = circleColor;
        invalidate();
    }

    public int getCircleColor() {
        return circleColor;
    }

    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        int w = getWidth();
        int h = getHeight();

        int pl = getPaddingLeft();
        int pr = getPaddingRight();
        int pt = getPaddingTop();
        int pb = getPaddingBottom();

        int usableWidth = w - (pl + pr);
        int usableHeight = h - (pt + pb);

        int radius = Math.min(usableWidth, usableHeight) / 2;
        int cx = pl + (usableWidth / 2);
        int cy = pt + (usableHeight / 2);

        paint.setColor(circleColor);
        canvas.drawCircle(cx, cy, radius, paint);
        Paint paint1 = new Paint();
        paint1.setColor(circleColor);
        paint1.setStyle(Paint.Style.STROKE);
        paint1.setStrokeWidth(10);
        paint1.setPathEffect(new DashPathEffect(new float[]{20,10}, 10));
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.calendar);
        canvas.drawBitmap(bitmap, (float)((w - bitmap.getWidth()) * 0.5), (float)((h - bitmap.getHeight()) * 0.5), paint1);
    }
}