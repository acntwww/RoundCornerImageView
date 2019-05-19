package com.acnt.android.widgets.radius;


import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Path;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.v4.util.ObjectsCompat;
import android.support.v7.widget.AppCompatImageView;
import android.util.AttributeSet;


/**
 * <p/>
 * Created by <a href="mailto:acntwww@gmail.com">Kerwin Niu</a> on 2019/5/17.
 * <br/>
 */
public class RoundCornerImageView extends AppCompatImageView {

    private int radius;
    private int topLeftRadius, topRightRadius, bottomLeftRadius, bottomRightRadius;

    private Paint paint;
    private Path path = new Path();

    private int width;
    private int height;
    private Bitmap lastBitmap;

    public RoundCornerImageView(final Context context) {
        this(context, null);
    }

    public RoundCornerImageView(final Context context, final AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RoundCornerImageView(final Context context, final AttributeSet attrs, final int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }


    private void init(Context context, AttributeSet attributeSet) {
        TypedArray a = context.obtainStyledAttributes(attributeSet, R.styleable.RoundCornerImageView);
        radius = a.getDimensionPixelOffset(R.styleable.RoundCornerImageView_fullRadius, -1);
        topRightRadius = a.getDimensionPixelOffset(R.styleable.RoundCornerImageView_topRightRadius, -1);
        topLeftRadius = a.getDimensionPixelOffset(R.styleable.RoundCornerImageView_topLeftRadius, -1);
        bottomLeftRadius = a.getDimensionPixelOffset(R.styleable.RoundCornerImageView_bottomLeftRadius, -1);
        bottomRightRadius = a.getDimensionPixelOffset(R.styleable.RoundCornerImageView_bottomRightRadius, -1);
        a.recycle();

        paint = new Paint();
        paint.setStyle(Style.FILL);
        paint.setAntiAlias(true);
        updatePaint();
    }

    private void updatePaint() {

        if (width <= 0) {
            width = getWidth();
        }

        if (height <= 0) {
            height = getHeight();
        }

        Drawable drawable = getDrawable();

        if (!(drawable instanceof BitmapDrawable)) {
            return;
        }

        Bitmap bitmap = ((BitmapDrawable) drawable).getBitmap();
        if (bitmap == null || ObjectsCompat.equals(bitmap, lastBitmap)) {
            return;
        }

        lastBitmap = bitmap;
        final BitmapShader bitmapShader = new BitmapShader(bitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);
        paint.setShader(bitmapShader);
    }

    public void updateCorner(int corner) {
        radius = corner;
        invalidate();
    }

    public void updateCorner(int topLeft, int topRight, int bottomLeft, int bottomRight) {
        topRightRadius = topRight;
        topLeftRadius = topLeft;
        bottomLeftRadius = bottomLeft;
        bottomRightRadius = bottomRight;
        invalidate();
    }

    @Override
    protected void onDraw(final Canvas canvas) {
        updatePaint();
        if (hasNoRadius()) {
            canvas.drawRect(0, 0, width, height, paint);
        } else {
            drawRoundCorner(canvas);
        }
    }

    private RectF tmpRect = new RectF();

    private void drawRoundCorner(Canvas canvas) {

        if (radius > 0) {
            tmpRect.set(0, 0, width, height);
            canvas.drawRoundRect(tmpRect, radius, radius, paint);
        } else {
            path.reset();

            if (topLeftRadius > 0) {
                path.moveTo(0, height - topLeftRadius);
                tmpRect.set(0, 0, topLeftRadius * 2, topLeftRadius * 2);
                path.arcTo(tmpRect, 180, 90);
            } else {
                path.moveTo(0, 0);
            }

            if (topRightRadius > 0) {
                tmpRect.set(width - topRightRadius * 2, 0, width, topRightRadius * 2);
                path.arcTo(tmpRect, 270, 90);
            } else {
                path.lineTo(width, 0);
            }

            if (bottomRightRadius > 0) {
                tmpRect.set(width - bottomRightRadius * 2, height - bottomRightRadius * 2, width, height);
                path.arcTo(tmpRect, 0, 90);
            } else {
                path.lineTo(width, height);
            }

            if (bottomLeftRadius > 0) {
                tmpRect.set(0, height - bottomLeftRadius * 2, bottomLeftRadius * 2, height);
                path.arcTo(tmpRect, 90, 90);
            } else {
                path.lineTo(0, height);
            }

            canvas.drawPath(path, paint);
        }

    }


    private boolean hasNoRadius() {
        if (radius < 0) {
            return topLeftRadius < 0 && topRightRadius < 0 && bottomRightRadius < 0 && bottomLeftRadius < 0;
        } else {
            return false;
        }
    }


}
