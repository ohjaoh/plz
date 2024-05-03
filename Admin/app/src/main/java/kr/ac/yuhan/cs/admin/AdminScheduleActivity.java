package kr.ac.yuhan.cs.admin;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import java.util.HashMap;
import java.util.Map;

import kr.ac.yuhan.cs.admin.func.ChangeTextColor;
import soup.neumorphism.NeumorphButton;
import soup.neumorphism.NeumorphCardView;
import soup.neumorphism.NeumorphImageView;

public class AdminScheduleActivity extends AppCompatActivity {

    private LinearLayout schedulePage;
    private NeumorphCardView scheduleCardView;
    private NeumorphCardView scheduleRegisterCardView;
    private NeumorphCardView input_todo;
    private TextView selectedDateTextView;
    private EditText todoEditText;
    private TextView todoTextView;
    private CalendarView calendarView;
    private NeumorphButton addTodoButton;
    private NeumorphButton handle;
    private NeumorphImageView backBtn;

    // Map to store tasks for each date
    private Map<String, String> tasks = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_schedule);

        schedulePage = (LinearLayout) findViewById(R.id.schedulePage);

        // MainActivity에서 전달된 배경 색상 값을 받음
        int backgroundColor = getIntent().getIntExtra("background_color", Color.rgb(236, 240, 243));
        // 배경 색상을 설정
        View backgroundView = getWindow().getDecorView().getRootView();
        backgroundView.setBackgroundColor(backgroundColor);

        if(backgroundColor == -10395295) {

            // 폰트 색상 변경
            ChangeTextColor.changeDarkTextColor(schedulePage, Color.WHITE);


            backBtn = (NeumorphImageView) findViewById(R.id.backBtn);
            backBtn.setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_IN);
            backBtn.setShadowColorLight(Color.GRAY);
            backBtn.setShadowColorDark(Color.BLACK);

            handle = (NeumorphButton) findViewById(R.id.handle);
            handle.setShadowColorLight(Color.GRAY);
            handle.setShadowColorDark(Color.BLACK);

            scheduleCardView = (NeumorphCardView) findViewById(R.id.scheduleCardView);
            scheduleCardView.setShadowColorLight(Color.GRAY);
            scheduleCardView.setShadowColorDark(Color.BLACK);

            scheduleRegisterCardView = (NeumorphCardView) findViewById(R.id.scheduleRegisterCardView);
            scheduleRegisterCardView.setShadowColorLight(Color.GRAY);
            scheduleRegisterCardView.setShadowColorDark(Color.BLACK);

            input_todo = (NeumorphCardView) findViewById(R.id.input_todo);
            input_todo.setShadowColorLight(Color.GRAY);
            input_todo.setShadowColorDark(Color.BLACK);

            addTodoButton = (NeumorphButton) findViewById(R.id.addTodoButton);
            addTodoButton.setShadowColorLight(Color.GRAY);
            addTodoButton.setShadowColorDark(Color.BLACK);
        }

        selectedDateTextView = findViewById(R.id.selectedDateTextView);
        todoEditText = findViewById(R.id.todoEditText);
        todoTextView = findViewById(R.id.todoTextView);
        calendarView = findViewById(R.id.calendarView);
        addTodoButton = findViewById(R.id.addTodoButton);

        // CalendarView에서 날짜가 선택되었을 때 이벤트 처리
        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                // 선택된 날짜를 TextView에 표시
                String selectedDate = dayOfMonth + "/" + (month + 1) + "/" + year;
                selectedDateTextView.setText("선택된 날짜 : " + selectedDate);

                // 선택된 날짜에 해당하는 할일을 표시
                String task = tasks.get(selectedDate);
                if (task != null) {
                    todoTextView.setText("선택된 날짜의 할일 목록\n" + task + "\n");
                } else {
                    todoTextView.setText("선택된 날짜의 할일이 없습니다.");
                }
            }
        });

        // 할일 추가 버튼 클릭 이벤트 처리
        addTodoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String selectedDate = selectedDateTextView.getText().toString().replace("Selected Date: ", "");
                String task = todoEditText.getText().toString();

                // 선택된 날짜에 해당하는 할일을 저장
                tasks.put(selectedDate, task);
                todoTextView.setText("Tasks for selected date: " + task);
                todoEditText.getText().clear();
            }
        });

        backBtn = (NeumorphImageView) findViewById(R.id.backBtn);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 클릭될 때 ShapeType을 'pressed'로 변경
                backBtn.setShapeType(1);
                // 클릭된 후에는 다시 FLAT으로 변경
                v.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        backBtn.setShapeType(0);
                    }
                }, 200);
                finish();
            }
        });
    }

}
