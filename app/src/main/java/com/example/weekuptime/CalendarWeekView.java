package com.example.weekuptime;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.core.content.res.ResourcesCompat;

import java.util.Calendar;

public class CalendarWeekView extends LinearLayout {

    public static final int MILLISECONDS_OF_DAY = 1000 * 3600 * 24;

    private TextView[] dayLabels;
    private long[] dayTimes;

    private int selectedYear = 1983;
    private int selectedMonth = 11;
    private int selectedDate = 24;

    private OnDateSelectedListener dateSelectedListener;

    interface OnDateSelectedListener {
        void onDateSelected(int year, int month, int date, int dayOfWeek);
    }

    public void setDateSelectedListener(OnDateSelectedListener dateSelectedListener) {
        this.dateSelectedListener = dateSelectedListener;
    }

    public CalendarWeekView(Context context) {
        super(context);
        inflateView(context);
    }

    public CalendarWeekView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        inflateView(context);
    }

    public CalendarWeekView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        inflateView(context);
    }

    private void inflateView(Context context) {
        LayoutInflater inflater = (LayoutInflater)
                context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.calendar_week_view, this);
        setOrientation(VERTICAL);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();

        dayTimes = new long[7];

        // set day labels
        dayLabels = new TextView[7];
        dayLabels[0] = findViewById(R.id.txtDay1);
        dayLabels[1] = findViewById(R.id.txtDay2);
        dayLabels[2] = findViewById(R.id.txtDay3);
        dayLabels[3] = findViewById(R.id.txtDay4);
        dayLabels[4] = findViewById(R.id.txtDay5);
        dayLabels[5] = findViewById(R.id.txtDay6);
        dayLabels[6] = findViewById(R.id.txtDay7);
        for (TextView dayLabel : dayLabels) {
            dayLabel.setOnClickListener(selectDayOfWeek);
        }

        updateWeekOfSelectedDate();
    }

    public void selectDate(int year, int month, int date) {
        selectedYear = year;
        selectedMonth = month;
        selectedDate = date;

        Calendar calendar = Calendar.getInstance();
        calendar.set(selectedYear, selectedMonth - 1, selectedDate, 0, 0, 0);
        int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);

        updateWeekOfSelectedDate();

        if (dateSelectedListener != null) {
            dateSelectedListener.onDateSelected(selectedYear, selectedMonth, selectedDate, dayOfWeek);
        }
    }

    public void forward() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(selectedYear, selectedMonth - 1, selectedDate,
                0, 0, 0);

        long newTime = calendar.getTimeInMillis() + MILLISECONDS_OF_DAY;
        calendar.setTimeInMillis(newTime);

        selectDate(calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH) + 1,
                calendar.get(Calendar.DATE));
    }

    public void backward() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(selectedYear, selectedMonth - 1, selectedDate,
                0, 0, 0);

        long newTime = calendar.getTimeInMillis() - MILLISECONDS_OF_DAY;
        calendar.setTimeInMillis(newTime);

        selectDate(calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH) + 1,
                calendar.get(Calendar.DATE));
    }

    private View.OnClickListener selectDayOfWeek = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            int index = coverToIndex(v);
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(dayTimes[index]);

            selectDate(calendar.get(Calendar.YEAR),
                    calendar.get(Calendar.MONTH) + 1,
                    calendar.get(Calendar.DATE));
        }
    };

    private int coverToIndex(View v) {
        switch (v.getId()) {
            case R.id.txtDay1:
                return 0;
            case R.id.txtDay2:
                return 1;
            case R.id.txtDay3:
                return 2;
            case R.id.txtDay4:
                return 3;
            case R.id.txtDay5:
                return 4;
            case R.id.txtDay6:
                return 5;
            case R.id.txtDay7:
                return 6;
        }
        return 0;
    }

    private void updateWeekOfSelectedDate() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(selectedYear, selectedMonth - 1, selectedDate,
                0, 0, 0);
        final long selectedTime = calendar.getTime().getTime();
        final int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
        for (int i = 1; i <= 7; i++) {
            long otherTime = selectedTime + (long) (i - dayOfWeek) * MILLISECONDS_OF_DAY;
            dayTimes[i - 1] = otherTime;

            calendar.setTimeInMillis(otherTime);
            TextView dayLabel = dayLabels[i - 1];
            // normal case
            dayLabel.setText(String.valueOf(calendar.get(Calendar.DATE)));
            dayLabel.setTextColor(Color.MAGENTA);
            dayLabel.setBackground(null);
            // when weekend
            if (i == 1 || i == 7) {
                dayLabel.setTextColor(Color.GRAY);
            }
            // when selected
            if (selectedTime == otherTime) {
                dayLabel.setTextColor(Color.WHITE);
                dayLabel.setBackground(ResourcesCompat.getDrawable(getResources(), R.drawable.bg_selected_date, null));
            }
        }
    }
}
