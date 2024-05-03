package kr.ac.yuhan.cs.admin.ui.login;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import kr.ac.yuhan.cs.admin.MainActivity;
import kr.ac.yuhan.cs.admin.R;
import kr.ac.yuhan.cs.admin.func.ChangeTextColor;
import soup.neumorphism.NeumorphButton;
import soup.neumorphism.NeumorphCardView;
import soup.neumorphism.NeumorphImageView;

public class LoginActivity extends AppCompatActivity {
    private LinearLayout loginPage;
    private NeumorphCardView loginCardView;
    private NeumorphCardView editTextIdField;
    private NeumorphCardView editTextPwField;
    private NeumorphButton loginBtn;
    private NeumorphImageView backBtn;
    private EditText input_id;
    private EditText input_pw;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        loginPage = (LinearLayout) findViewById(R.id.loginPage);

        // 현재 mode값 받음
        int modeValue = getIntent().getIntExtra("mode", 1);

        // MainActivity에서 전달된 배경 색상 값을 받음
        int backgroundColor = getIntent().getIntExtra("background_color", Color.rgb(236, 240, 243));
        // 배경 색상을 설정
        View backgroundView = getWindow().getDecorView().getRootView();
        backgroundView.setBackgroundColor(backgroundColor);

        System.out.println(backgroundColor);// 현재 색상이 무엇인지 체크하는 코드

        if(backgroundColor == -10395295) {

            // 폰트 색상 변경
            ChangeTextColor.changeDarkTextColor(loginPage, Color.WHITE);

            // 새 이미지로 바꿔주세요.
            Drawable darkIdImage = getResources().getDrawable(R.drawable.user);
            darkIdImage.setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_IN);
            Drawable darkPwImage = getResources().getDrawable(R.drawable.lock);
            darkPwImage.setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_IN);

            backBtn = (NeumorphImageView) findViewById(R.id.backBtn);
            backBtn.setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_IN);
            backBtn.setShadowColorLight(Color.GRAY);
            backBtn.setShadowColorDark(Color.BLACK);

            loginCardView = (NeumorphCardView) findViewById(R.id.loginCardView);
            loginCardView.setShadowColorLight(Color.GRAY);
            loginCardView.setShadowColorDark(Color.BLACK);

            editTextIdField = (NeumorphCardView) findViewById(R.id.editTextIdField);
            editTextIdField.setShadowColorLight(Color.GRAY);
            editTextIdField.setShadowColorDark(Color.BLACK);

            editTextPwField = (NeumorphCardView) findViewById(R.id.editTextPwField);
            editTextPwField.setShadowColorLight(Color.GRAY);
            editTextPwField.setShadowColorDark(Color.BLACK);

            loginBtn = (NeumorphButton) findViewById(R.id.loginBtn);
            loginBtn.setShadowColorLight(Color.GRAY);
            loginBtn.setShadowColorDark(Color.BLACK);

            input_id = (EditText) findViewById(R.id.input_id);
            input_id.setCompoundDrawablesWithIntrinsicBounds(darkIdImage, null, null, null);

            input_pw = (EditText) findViewById(R.id.input_pw);
            input_pw.setCompoundDrawablesWithIntrinsicBounds(darkPwImage, null, null, null);
        }

        loginBtn = findViewById(R.id.loginBtn);
        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 클릭될 때 ShapeType을 'pressed'로 변경
                loginBtn.setShapeType(1);
                // 클릭된 후에는 다시 FLAT으로 변경
                v.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        loginBtn.setShapeType(0);
                    }
                }, 200);

                Toast tMsg = Toast.makeText(LoginActivity.this, "Login Success", Toast.LENGTH_SHORT);
                Display display = ((WindowManager) getSystemService(WINDOW_SERVICE))
                        .getDefaultDisplay();
                int xOffset = (int) (Math.random() * display.getWidth()); // x좌표
                int yOffset = (int) (Math.random() * display.getHeight()); // y좌표
                tMsg.setGravity(Gravity.TOP | Gravity.LEFT, xOffset, yOffset);
                tMsg.show();

                finish();
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