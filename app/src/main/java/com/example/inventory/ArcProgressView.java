
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

    private Paint segmentPaint;
    private Paint textPaint; // Paint for text labels
    private RectF arcRect;
    private float[] segmentValues = new float[]{30f, 45f, 25f}; // Default segment values
    private String[] segmentLabels = new String[]{"Segment 1", "Segment 2", "Segment 3"}; // Default labels
    private int[] segmentColors;

    private static final int SEGMENT_STROKE_WIDTH = 50; // Thickness for segments
    private static final int TEXT_SIZE = 40; // Text size for segment labels

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
        segmentPaint.setStrokeWidth(SEGMENT_STROKE_WIDTH); // Uniform thickness

        textPaint = new Paint();
        textPaint.setAntiAlias(true);
        textPaint.setTextSize(TEXT_SIZE);
        textPaint.setColor(ContextCompat.getColor(getContext(), android.R.color.white));
        textPaint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));

        arcRect = new RectF();
    }

    // Set the percentage values for each segment
    public void setSegmentValues(float[] segmentValues) {
        this.segmentValues = segmentValues;
        invalidate(); // Redraw the view when data changes
    }

    // Set the colors for each segment
    public void setSegmentColors(int[] segmentColors) {
        this.segmentColors = segmentColors;
        invalidate();
    }

    // Set the labels for each segment
    public void setSegmentLabels(String[] segmentLabels) {
        this.segmentLabels = segmentLabels;
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (segmentColors == null || segmentValues == null) return; // Avoid drawing if no colors or values are set

        // Define the bounds of the arc
        int width = getWidth();
        int height = getHeight();
        float radius = Math.min(width, height) / 2f;

        // Account for the stroke width to avoid clipping
        float offset = SEGMENT_STROKE_WIDTH / 2f;
        arcRect.set(width / 2f - radius + offset, height / 2f - radius + offset,
                width / 2f + radius - offset, height / 2f + radius - offset);

        // Draw the segments
        float startAngle = -90f; // Start at the top
        for (int i = 0; i < segmentValues.length; i++) {
            segmentPaint.setColor(segmentColors[i]);
            float sweepAngle = segmentValues[i] / 100f * 360f;
            canvas.drawArc(arcRect, startAngle, sweepAngle, false, segmentPaint);

            // Draw the labels
            drawSegmentLabel(canvas, startAngle + sweepAngle / 2, segmentLabels[i], radius);

            startAngle += sweepAngle;
        }
    }

    private void drawSegmentLabel(Canvas canvas, float angle, String label, float radius) {
        // Convert angle to radians
        double radians = Math.toRadians(angle);

        // Calculate the label's position
        float labelX = (float) (getWidth() / 2 + radius / 1.5 * Math.cos(radians)); // Adjusted for distance from center
        float labelY = (float) (getHeight() / 2 + radius / 1.5 * Math.sin(radians));

        // Draw the text label at the calculated position
        canvas.drawText(label, labelX, labelY, textPaint);
    }
}

