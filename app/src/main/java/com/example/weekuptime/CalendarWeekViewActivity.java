package com.example.weekuptime;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;
import androidx.viewpager2.widget.ViewPager2;

import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class CalendarWeekViewActivity extends AppCompatActivity {

    private static final String TAG = "LOG_" + CalendarWeekViewActivity.class.getSimpleName();

    private CalendarWeekView calendarWeekView;
    //    private ViewPager detailPager;
    private ViewPager2 detailPager;

    private int selectedYear;
    private int selectedMonth;
    private int selectedDate;
    private long todayTime;
    private int dayCount = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar_week_view);

        Calendar today = Calendar.getInstance();
        selectedYear = today.get(Calendar.YEAR);
        selectedMonth = today.get(Calendar.MONTH) + 1;
        selectedDate = today.get(Calendar.DAY_OF_MONTH);
        todayTime = today.getTimeInMillis(); // today

        today.add(Calendar.MONTH, -11);
        long fromTime = today.getTimeInMillis(); // before 11 month

        dayCount = (int) ((todayTime - fromTime) / (1000 * 3600 * 24)) + 1;

        calendarWeekView = findViewById(R.id.calendar_week_view);
        calendarWeekView.setDateSelectedListener(
                new CalendarWeekView.OnDateSelectedListener() {
                    @Override
                    public void onDateSelected(int year, int month, int date, int dayOfWeek) {
                        setTitle(year + "/" + month);
                        updateDate(year, month, date);
                    }
                });
        calendarWeekView.selectDate(selectedYear, selectedMonth, selectedDate);

        List<Long> times = new ArrayList<>();
        for (int i = 0; i < dayCount; i++) {
            long time = getTimeByPosition(i);
            times.add(time);
        }
        DetailPagerAdapter detailAdapter = new DetailPagerAdapter(this, times);

        detailPager = findViewById(R.id.detail_pager);
        detailPager.setPageTransformer(new ZoomOutPageTransformer());
        detailPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {
                if (state == ViewPager.SCROLL_STATE_IDLE) {
                    updateCalendarWeekByPosition();
                }
            }
        });
        detailPager.setAdapter(detailAdapter);

        detailPager.setCurrentItem(dayCount - 1); // set to last day; that is today
    }

    private void updateCalendarWeekByPosition() {
        long time = getTimeByPosition(detailPager.getCurrentItem());
        Calendar c = Calendar.getInstance();
        c.setTimeInMillis(time);
        calendarWeekView.selectDate(
                c.get(Calendar.YEAR),
                c.get(Calendar.MONTH) + 1,
                c.get(Calendar.DAY_OF_MONTH));
    }

    private void updateDate(int year, int month, int date) {
        this.selectedYear = year;
        this.selectedMonth = month;
        this.selectedDate = date;

        Calendar calendar = Calendar.getInstance();
        calendar.set(selectedYear, selectedMonth - 1, selectedDate, 0, 0, 0);
        long time = calendar.getTimeInMillis();
        if (time > todayTime) {
            findViewById(R.id.detail_pager).setVisibility(View.GONE);
            findViewById(R.id.no_data_hint).setVisibility(View.VISIBLE);
            return;
        }
        findViewById(R.id.detail_pager).setVisibility(View.VISIBLE);
        findViewById(R.id.no_data_hint).setVisibility(View.GONE);
        int position = getPositionByTime(time);
        if (detailPager != null) {
            detailPager.setCurrentItem(position);
        }
    }

    private long getTimeByPosition(int position) {
        return todayTime - (long) (dayCount - position - 1) * (1000 * 3600 * 24);
    }

    private int getPositionByTime(long time) {
        return (int) (dayCount - 1 - (todayTime - time) / (1000 * 3600 * 24));
    }

    public void backward(View view) {
        calendarWeekView.backward();
    }

    public void forward(View view) {
        calendarWeekView.forward();
    }

    public void backToToday(View view) {
        Calendar today = Calendar.getInstance();
        selectedYear = today.get(Calendar.YEAR);
        selectedMonth = today.get(Calendar.MONTH) + 1;
        selectedDate = today.get(Calendar.DAY_OF_MONTH);
        calendarWeekView.selectDate(selectedYear, selectedMonth, selectedDate);
    }
}