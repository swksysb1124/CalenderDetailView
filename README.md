# CalenderDetailView

## Layout 設定
```xml
// CalendarWeekView
<com.example.weekuptime.CalendarWeekView
    android:id="@+id/calendar_week_view"
    android:layout_width="match_parent"
    android:layout_height="wrap_content" />

// DayEventView
<com.example.weekuptime.DayEventView
    android:id="@+id/day_event_view"
    android:layout_width="match_parent"
    android:layout_height="wrap_content" />
```

## 程式碼
```java
// Use CalendarWeekView
private CalendarWeekView calendarWeekView;
...
// Get CalendarWeekView instance
calendarWeekView = findViewById(R.id.calendar_week_view);
// Set OnDateSelectedListener 
calendarWeekView.setDateSelectedListener(
    new CalendarWeekView.OnDateSelectedListener() {
        @Override
        public void onDateSelected(int year, int month, int date, int dayOfWeek) {
            setTitle(year + "/" + month);
            updateDate(year, month, date);
        }
    });
    
// Select Date
calendarWeekView.selectDate(selectedYear, selectedMonth, selectedDate);

// Forward one day
calendarWeekView.forward();

// Backward one day
calendarWeekView.backward();

// Mock Data
List<DayEventView.DayEvent> dayEventList = new ArrayList<>();
dayEventList.add(new DayEventView.DayEvent(10, 120, "打電動"));
dayEventList.add(new DayEventView.DayEvent(130, 1000, "打電動"));
dayEventList.add(new DayEventView.DayEvent(1200, 3000, "睡覺"));
dayEventList.add(new DayEventView.DayEvent(3500, 7800, "工作"));
dayEventList.add(new DayEventView.DayEvent(20000, 40000, "看電視"));

DayEventView dayEventView = findViewById(R.id.day_event_view);
dayEventView.setDayEvents(dayEventList);
```
