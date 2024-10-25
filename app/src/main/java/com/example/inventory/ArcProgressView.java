package com.example.inventory;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.View;
import androidx.core.content.ContextCompat;

public class ArcProgressView extends View {
    private int rawMaterialsProgress = 10;  // Default value for testing
    private int processingItemsProgress = 30; // Default value for testing
    private int finishedGoodsProgress = 60; // Default value for testing

    private Paint segmentPaint;
    private Paint textPaint;
    private RectF arcRect;

    private static final int SEGMENT_STROKE_WIDTH = 50;
    private static final int TEXT_SIZE = 40;

    // Colors for each segment
    private int[] segmentColors;

    public ArcProgressView(Context context) {
        super(context);
        init();
    }

    public ArcProgressView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public ArcProgressView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        segmentPaint = new Paint();
        segmentPaint.setAntiAlias(true);
        segmentPaint.setStyle(Paint.Style.STROKE);
        segmentPaint.setStrokeWidth(SEGMENT_STROKE_WIDTH);

        textPaint = new Paint();
        textPaint.setAntiAlias(true);
        textPaint.setTextSize(TEXT_SIZE);
        textPaint.setColor(ContextCompat.getColor(getContext(), android.R.color.white));
        textPaint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));

        arcRect = new RectF();

        // Set default colors if none are provided
        segmentColors = new int[]{
                ContextCompat.getColor(getContext(), android.R.color.holo_orange_dark),   // Raw Materials
                ContextCompat.getColor(getContext(), android.R.color.holo_purple), // Processing Items
                ContextCompat.getColor(getContext(), android.R.color.holo_blue_bright)   // Finished Goods
        };
    }

    // Methods to update each segment's progress
    public void setRawMaterialsProgress(int progress) {
        this.rawMaterialsProgress = progress;
        invalidate();
    }

    public void setProcessingItemsProgress(int progress) {
        this.processingItemsProgress = progress;
        invalidate();
    }

    public void setFinishedGoodsProgress(int progress) {
        this.finishedGoodsProgress = progress;
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        int width = getWidth();
        int height = getHeight();
        float radius = Math.min(width, height) / 2f;
        float offset = SEGMENT_STROKE_WIDTH / 2f;
        arcRect.set(width / 2f - radius + offset, height / 2f - radius + offset,
                width / 2f + radius - offset, height / 2f + radius - offset);

        int totalProgress = rawMaterialsProgress + processingItemsProgress + finishedGoodsProgress;
        if (totalProgress == 0) return; // Avoid division by zero

        float[] segmentValues = new float[]{
                (rawMaterialsProgress / (float) totalProgress) * 100,
                (processingItemsProgress / (float) totalProgress) * 100,
                (finishedGoodsProgress / (float) totalProgress) * 100
        };

        float startAngle = -90f;
        for (int i = 0; i < segmentValues.length; i++) {
            segmentPaint.setColor(segmentColors[i]);
            float sweepAngle = segmentValues[i] / 100f * 360f;
            canvas.drawArc(arcRect, startAngle, sweepAngle, false, segmentPaint);
            startAngle += sweepAngle;
        }
    }
}
