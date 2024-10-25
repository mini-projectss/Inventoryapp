package com.example.inventory;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import java.util.ArrayList;

public class BarChartView extends View {

    private Paint barPaint;
    private Paint labelPaint;
    private ArrayList<Integer> barValues = new ArrayList<>();
    private ArrayList<String> labels = new ArrayList<>();

    public BarChartView(Context context, AttributeSet attrs) {
        super(context, attrs);
        barPaint = new Paint();
        barPaint.setColor(Color.BLUE);
        barPaint.setStyle(Paint.Style.FILL);

        labelPaint = new Paint();
        labelPaint.setColor(Color.BLACK);
        labelPaint.setTextSize(30);
    }

    // Method to set data for the bar chart
    public void setBarData(ArrayList<Integer> values, ArrayList<String> labels) {
        this.barValues = values;
        this.labels = labels;
        invalidate(); // Redraw the view with updated data
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (barValues == null || barValues.size() == 0) return;

        int width = getWidth();
        int height = getHeight();
        int barWidth = width / (barValues.size() * 2);

        for (int i = 0; i < barValues.size(); i++) {
            int barHeight = (int) ((barValues.get(i) / 100.0) * height);
            int x = i * 2 * barWidth;
            int y = height - barHeight;

            // Draw each bar
            canvas.drawRect(x, y, x + barWidth, height, barPaint);

            // Draw label below each bar
            canvas.drawText(labels.get(i), x + (barWidth / 4), height + 30, labelPaint);
        }
    }
}
