package com.example.weekuptime;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.Calendar;
import java.util.List;

public class DetailPageFragment extends Fragment {

    private static final String TAG = "LOG_" + DetailPageFragment.class.getSimpleName();
    private static final String ARG_TIME = "time";
    private long time;

    // Model
    private DetailModel detailModel;

    public DetailPageFragment() {
        // Required empty public constructor
    }

    public static DetailPageFragment newInstance(long time) {
        DetailPageFragment fragment = new DetailPageFragment();
        Bundle args = new Bundle();
        args.putLong(ARG_TIME, time);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            time = getArguments().getLong(ARG_TIME);
        }
        detailModel = new DetailModel();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.
                fragment_detail_page, container, false);
    }

    @Override
    public void onResume() {
        super.onResume();
        update();
    }

    public void update() {
        Log.i(TAG, "update: ");
        updateContentTime(time);
        detailModel.fetchDayEventsByTime(time, new DetailModel.Callback() {
            @Override
            public void onDataReceived(String data) {

            }

            @Override
            public void onDayEventsReceived(List<DayEventView.DayEvent> dayEventList) {
                if (getView() != null) {
                    DayEventView dayEventView = getView().findViewById(R.id.day_event_view);
                    dayEventView.setDayEvents(dayEventList);
                }
            }
        });

    }

    private void updateContentTime(long time) {
        if (getView() != null) {
            TextView contentTime = getView().findViewById(R.id.content_time);
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(time);
            contentTime.setText(calendar.getTime().toString());
        }
    }

}