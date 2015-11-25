package com.devbrackets.android.androidmarkup.text.style;

import android.annotation.SuppressLint;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.os.Build;
import android.text.Layout;
import android.text.style.LeadingMarginSpan;

public class ListSpan implements LeadingMarginSpan {
    public static final int DEFAULT_GAP_WIDTH = 40; //px
    public static final int DEFAULT_BULLET_RADIUS = 4; //px

    public enum Type {
        BULLET,
        NUMERICAL
    }

    private static Path bulletPath = null;

    protected final int gapWidth;
    protected final int bulletRadius;
    protected final Type type;

    //Used for the Numerical list
    private int number = 0;
    private int lastBaseline = -1;

    public ListSpan() {
        this(Type.BULLET, DEFAULT_GAP_WIDTH, DEFAULT_BULLET_RADIUS);
    }

    public ListSpan(int gapWidth) {
        this(Type.BULLET, gapWidth, DEFAULT_BULLET_RADIUS);
    }

    public ListSpan(Type type) {
        this(type, DEFAULT_GAP_WIDTH, DEFAULT_BULLET_RADIUS);
    }

    public ListSpan(Type type, int gapWidth) {
        this(type, gapWidth, DEFAULT_BULLET_RADIUS);
    }

    public ListSpan(Type type, int gapWidth, int bulletRadius) {
        this.gapWidth = gapWidth;
        this.bulletRadius = bulletRadius;
        this.type = type;
    }

    @Override
    public int getLeadingMargin(boolean first) {
        return 2 * bulletRadius + gapWidth;
    }

    @Override
    public void drawLeadingMargin(Canvas canvas, Paint paint, int marginPosition, int direction, int top, int baseline, int bottom, CharSequence text, int start, int end, boolean first, Layout layout) {
        //Cache the style so our changes don't affect others
        Paint.Style style = paint.getStyle();

        if (type == Type.BULLET) {
            drawBulletMargin(canvas, paint, marginPosition, direction, top, baseline, bottom);
        } else {
            drawNumericalMargin(canvas, paint, marginPosition, direction, top, baseline, bottom);
        }

        paint.setStyle(style);
    }

    public Type getType() {
        return type;
    }

    @SuppressLint("NewApi")
    protected void drawBulletMargin(Canvas canvas, Paint paint, int marginPosition, int direction, int top, int baseline, int bottom) {
        paint.setStyle(Paint.Style.FILL);
        int verticalCenter = (top + bottom) / 2;

        //If we don't support hardware acceleration just draw the circle
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB || !canvas.isHardwareAccelerated()) {
            canvas.drawCircle(marginPosition + direction * bulletRadius, verticalCenter, bulletRadius, paint);
            return;
        }

        //Since we support hardware acceleration make sure we have a vector to draw
        if (bulletPath == null) {
            bulletPath = new Path();
            // Bullet is slightly better to avoid aliasing artifacts on mdpi devices.
            bulletPath.addCircle(0.0f, 0.0f, bulletRadius, Path.Direction.CW);
        }

        canvas.save();
        canvas.translate(marginPosition + direction * bulletRadius, verticalCenter);
        canvas.drawPath(bulletPath, paint);
        canvas.restore();
    }

    @SuppressLint("NewApi")
    protected void drawNumericalMargin(Canvas canvas, Paint paint, int marginPosition, int direction, int top, int baseline, int bottom) {
        if (baseline > lastBaseline) {
            lastBaseline = baseline;
            number++;
        } else if (baseline < lastBaseline) {
            lastBaseline = baseline;
            number = 1;
        }

        canvas.drawText(number + ".", marginPosition + direction * bulletRadius, baseline, paint);
    }
}