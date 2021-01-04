package com.example.weekuptime;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;

public class MainActivity extends AppCompatActivity {

    public static final int MILLISECONDS_OF_DAY = 1000 * 3600 * 24;
    TextView[] dayLabels;
    long[] dayTimes;

    // select year/month/date
    int selectedYear = 2020;
    int selectedMonth = 12;
    int selectedDate = 29;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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

        findViewById(R.id.forward).setOnClickListener(forwardBackwardDate);
        findViewById(R.id.backward).setOnClickListener(forwardBackwardDate);

        // update month and day labels
        updateYearMonth();
        updateWeekOfSelectedDate();
    }

    private void updateYearMonth() {
        setTitle(selectedYear + "/" + selectedMonth);
    }

    public void updateWeekOfSelectedDate() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(selectedYear, selectedMonth - 1, selectedDate, 0, 0, 0);
        long selectedTime = calendar.getTime().getTime();

        int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
        for (int i = 1; i <= 7; i++) {
            long otherTime = selectedTime + (i - dayOfWeek) * MILLISECONDS_OF_DAY;
            calendar.setTimeInMillis(otherTime);

            dayTimes[i - 1] = otherTime;

            TextView dayLabel = dayLabels[i - 1];
            dayLabel.setText(String.valueOf(calendar.get(Calendar.DATE)));
            dayLabel.setTextColor(Color.MAGENTA);
            dayLabel.setBackground(null);
            if (i == 1 || i == 7) {
                dayLabel.setTextColor(Color.GRAY);
            }
            if (selectedTime == otherTime) {
                dayLabel.setTextColor(Color.WHITE);
                dayLabel.setBackground(getResources().getDrawable(R.drawable.bg_selected_date));
            }
        }
    }

    View.OnClickListener selectDayOfWeek = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            int index = coverToIndex(v);
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(dayTimes[index]);
            Toast.makeText(MainActivity.this, calendar.getTime().toString(), Toast.LENGTH_SHORT).show();

            selectDate(
                    calendar.get(Calendar.YEAR),
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

    View.OnClickListener forwardBackwardDate = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Calendar calendar = Calendar.getInstance();
            calendar.set(selectedYear,
                    selectedMonth - 1,
                    selectedDate,
                    0, 0, 0);

            long diff;
            if (v.getId() == R.id.backward) {
                System.out.println("BACKWARD");
                diff = -MILLISECONDS_OF_DAY;
            } else {
                System.out.println("FORWARD");
                diff = MILLISECONDS_OF_DAY;
            }
            long newTime = calendar.getTimeInMillis() + diff;
            calendar.setTimeInMillis(newTime);

            selectDate(
                    calendar.get(Calendar.YEAR),
                    calendar.get(Calendar.MONTH) + 1,
                    calendar.get(Calendar.DATE));
        }
    };

    private void selectDate(int year, int month, int date) {
        selectedYear = year;
        selectedMonth = month;
        selectedDate = date;

        updateYearMonth();
        updateWeekOfSelectedDate();
        // TODO: update daily detail information
    }
}