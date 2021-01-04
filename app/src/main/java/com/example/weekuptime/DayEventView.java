package com.example.weekuptime;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Region;
import android.util.AttributeSet;

import android.util.Log;
import android.view.View;

import androidx.annotation.Nullable;

import java.util.List;

public class DayEventView extends View {

    private static final int DAY_IN_SECONDS = 3600 * 24;
    private List<DayEvent> dayEvents;

    private int hourSpace;
    private float topMargin, bottomMargin;
    private Paint paint;

    public DayEventView(Context context) {
        super(context);
        setParameters();
    }

    public DayEventView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        setParameters();
    }

    private void setParameters() {
        hourSpace = dip2px(100);
        topMargin = dip2px(20);
        bottomMargin = dip2px(20);
        paint = new Paint();
        paint.setAntiAlias(true);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int width = measureDimension(dip2px(300), widthMeasureSpec);
        float defaultHeight = 24 * hourSpace + topMargin + bottomMargin;
        int height = measureDimension((int) defaultHeight, heightMeasureSpec);
        setMeasuredDimension(width, height);
    }

    private int measureDimension(int defaultSize, int measureSpec) {
        int specMode = MeasureSpec.getMode(measureSpec);
        int specSize = MeasureSpec.getSize(measureSpec);

        int result;
        if (specMode == MeasureSpec.EXACTLY) { // match_parent & specified value
            result = specSize;
        } else {
            result = defaultSize;   //UNSPECIFIED
            if (specMode == MeasureSpec.AT_MOST) { // wrap_content
                result = Math.min(result, specSize);
            }
        }
        return result;
    }

    public void setDayEvents(List<DayEvent> dayEvents) {
        this.dayEvents = dayEvents;
        invalidate();
        requestLayout();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        float yOffset = topMargin;
        float daySpace = 24 * hourSpace;

        drawHourIndicators(canvas, yOffset);
        drawDayEvents(canvas, daySpace);
    }

    private void drawHourIndicators(Canvas canvas, float yOffset) {
        drawHourIndicator(canvas, 0, yOffset);
        for (int i = 1; i <= 24; i++) {
            int hour = i;
            if (i == 24) {
                hour = 0;
            }
            drawHourIndicator(canvas, hour, yOffset + i * hourSpace);
        }
    }

    private void drawHourIndicator(Canvas canvas, int hour, float y) {
        float textSize = dip2px(12);
        paint.setColor(Color.BLACK);
        paint.setStyle(Paint.Style.FILL);
        paint.setTextSize(textSize);
        String hourText = hour < 10 ? "0" + hour : "" + hour;
        hourText = hourText + ":00";
        canvas.drawText(hourText,
                dip2px(5), y + getTextVerticalOffset(hourText, textSize), paint);

        paint.setColor(Color.LTGRAY);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(dip2px(1));
        float xIndicatorLine = dip2px(5) + getTextWidth(hourText) + dip2px(5);
        canvas.drawLine(xIndicatorLine, y, getWidth(), y, paint);
    }

    private void drawDayEvents(Canvas canvas, float daySpace) {
        if (dayEvents != null) {
            for (DayEvent dayEvent : dayEvents) {
                drawEvent(canvas, dayEvent, daySpace);
            }
        }
    }

    private void drawEvent(Canvas canvas, DayEvent dayEvent, float daySpace) {
        paint.setColor(Color.parseColor("#5FFF0000"));
        paint.setStyle(Paint.Style.FILL);

        int x1 = dip2px(50);
        int y1 = (int) ((float) dayEvent.start / DAY_IN_SECONDS * daySpace + topMargin);
        int x2 = getWidth();
        int y2 = (int) ((float) dayEvent.end / DAY_IN_SECONDS * daySpace + topMargin);

        float textSize = dip2px(12);
        float textStartMargin = dip2px(10);
        float textTopMargin = dip2px(8);
        float xRadius =  dip2px(5);
        float yRadius =  dip2px(5);

        if ((y2 - y1) < textSize) { // TODO: when day event start & end to closed
            textSize = (y2 - y1) * 0.8f;
            textTopMargin = dip2px(1);
        }

        Log.d("TAG", "drawEvent: dayEvent.label=" + dayEvent.label + ", textSize=" + textSize);
        canvas.drawRoundRect(x1, y1, x2, y2, xRadius, yRadius, paint);

        paint.setColor(Color.WHITE);
        paint.setTextSize(textSize);
        canvas.drawText(dayEvent.label, x1 + textStartMargin,
                y1 + textTopMargin + getTextVerticalOffset(dayEvent.label, textSize), paint);

        paint.setColor(Color.parseColor("#FFFF0000"));
        canvas.clipRect(x1 + dip2px(3), y1, x2, y2, Region.Op.DIFFERENCE);
        canvas.drawRoundRect(x1, y1, x2, y2, dip2px(5), dip2px(5), paint);
    }

    public static class DayEvent {
        int start; // in second
        int end; // in second
        String label;

        public DayEvent(int start, int end, String label) {
            this.start = start;
            this.end = end;
            this.label = label;
        }
    }

    private int dip2px(float dpValue) {
        final float scale = getContext().getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    private float getTextVerticalOffset(String text, float textSize) {
        Rect textBounds = new Rect();
        paint.setTextSize(textSize);
        paint.getTextBounds(text, 0, text.length(), textBounds);
        return (float) (textBounds.height() / 2);
    }

    private float getTextWidth(String text) {
        Rect textBounds = new Rect();
        paint.getTextBounds(text, 0, text.length(), textBounds);
        return textBounds.width();
    }
}
