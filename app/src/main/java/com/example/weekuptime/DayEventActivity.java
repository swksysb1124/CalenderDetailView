package com.example.weekuptime;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import java.util.ArrayList;
import java.util.List;

public class DayEventActivity extends AppCompatActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_day_event);

        List<DayEventView.DayEvent> dayEventList = new ArrayList<>();
        dayEventList.add(new DayEventView.DayEvent(10, 120, "打電動"));
        dayEventList.add(new DayEventView.DayEvent(130, 1000, "打電動"));
        dayEventList.add(new DayEventView.DayEvent(1200, 3000, "睡覺"));
        dayEventList.add(new DayEventView.DayEvent(3500, 7800, "工作"));
        dayEventList.add(new DayEventView.DayEvent(20000, 40000, "看電視"));

        DayEventView dayEventView = findViewById(R.id.day_event_view);
        dayEventView.setDayEvents(dayEventList);
    }
}