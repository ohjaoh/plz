package kr.ac.yuhan.cs.admin;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import kr.ac.yuhan.cs.admin.adapter.MemberAdapter;
import kr.ac.yuhan.cs.admin.data.MemberData;
import kr.ac.yuhan.cs.admin.func.ChangeTextColor;
import kr.ac.yuhan.cs.admin.ui.login.LoginActivity;
import kr.ac.yuhan.cs.admin.util.ChangeMode;
import soup.neumorphism.NeumorphButton;
import soup.neumorphism.NeumorphCardView;
import soup.neumorphism.NeumorphImageView;

public class MainActivity extends AppCompatActivity {

    private int mode = 0;

    // body
    private NeumorphCardView mainCardView;
    private NeumorphCardView footer_menu;
    // 하단 Bar 메뉴
    private NeumorphImageView homeBtn;
    private NeumorphImageView memberBtn;
    private NeumorphImageView productBtn;
    private NeumorphImageView payHistoryBtn;
    private NeumorphImageView productPushBtn;

    // 회원 메뉴
    private NeumorphButton userSearchBtn;

    // HOME 메뉴
    private NeumorphCardView adminBtn;
    private NeumorphCardView adminScheduleBtn;
    private NeumorphCardView callBtn;
    private NeumorphCardView login;

    // 상품등록 메뉴
    private NeumorphCardView input_productName;
    private NeumorphCardView input_productQuantity;
    private NeumorphCardView input_productCategory;
    private NeumorphCardView input_productPrice;
    private NeumorphButton createQRBtn;
    private NeumorphButton createProductBtn;

    // 상단 버튼 이미지
    private NeumorphImageView setting;
    private NeumorphImageView changeMode;

    // HOME 메뉴 이미지
    private ImageView adminSchedule;
    private ImageView adminList;
    private ImageView adminLogin;
    private ImageView call;

    // 하단 메뉴 아이콘
    private NeumorphImageView usersIcon;
    private NeumorphImageView productIcon;
    private NeumorphImageView homeIcon;
    private NeumorphImageView paymentIcon;
    private NeumorphImageView productRegisterIcon;
    private ViewFlipper vFlipper;

    // 배경 기본색
    private int backgroundColor;
    private int mainBackgroundColor = Color.rgb(236, 240, 243);
    private int darkModeBackgroundColor = Color.rgb(97, 97, 97);
    private int btnColor = Color.rgb(0, 174, 142);

    private ListView listView;

    //오자현추가부분
    private FirebaseFirestore dbFirestore;
    private ImageView imageViewProduct;
    private Uri fileuri = null;
    private static final int PICK_FILE_REQUEST = 2; // 파일 선택을 위한 요청 코드
    private EditText editProductName, editProductPrice, editProductStock, editProductCategory;

    private ListView listView2;
    private ProductAdapter adapter2;
    private Handler handler = new Handler();
    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            loadItemsFromFirestore(); // 파이어베이스에서 데이터를 불러오는 메서드 호출
            handler.postDelayed(this, 5000); // 5초 후에 다시 실행하도록 스케줄링
        }
    };

    private ArrayList<Product> productList = new ArrayList<>(); // 상품 정보를 담을 리스트

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listView = findViewById(R.id.listView);

        // 가짜 데이터 생성
        ArrayList<MemberData> fakeDataList = createFakeData();

        // 어댑터 설정
        MemberAdapter adapter = new MemberAdapter(this, fakeDataList);
        listView.setAdapter(adapter);

        // 오자현추가부분
        listView2 = findViewById(R.id.listViewProducts); // 레이아웃에서 리스트 뷰 연결

        adapter2 = new ProductAdapter(this, productList);// 여기서 객체를 생성할 때 문제가 있음
        listView2.setAdapter(adapter2); // 리스트 뷰에 어댑터 설정

        loadItemsFromFirestore();
        handler.postDelayed(runnable, 5000); // 5초마다 데이터를 새로 고침
        imageViewProduct = findViewById(R.id.imageViewProduct);
        imageViewProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openFileManager();
            }
        });
        editProductName = findViewById(R.id.editProductName);
        editProductPrice = findViewById(R.id.editProductPrice);
        editProductStock = findViewById(R.id.editProductStock);
        editProductCategory = findViewById(R.id.editProductCategory);
        dbFirestore = FirebaseFirestore.getInstance();

        // 여기있던 뷰플리퍼 전역으로 이동 상품등록후 내용초기화하고 메인으로 보내기 위함
        vFlipper = (ViewFlipper) findViewById(R.id.viewFlipper1);

        LinearLayout main = findViewById(R.id.main);
        // 기본 배경색 지정
        backgroundColor = mainBackgroundColor;
        main.setBackgroundColor(backgroundColor);

        Drawable backgroundDrawable = main.getBackground();
        mainBackgroundColor = ((ColorDrawable) backgroundDrawable).getColor();

        createQRBtn = (NeumorphButton) findViewById(R.id.createQRBtn);
        createProductBtn = (NeumorphButton) findViewById(R.id.createProductBtn);
        userSearchBtn = (NeumorphButton) findViewById(R.id.userSearchBtn);

        createQRBtn.setBackgroundColor(btnColor);
        createProductBtn.setBackgroundColor(btnColor);
        userSearchBtn.setBackgroundColor(btnColor);

        callBtn = (NeumorphCardView) findViewById(R.id.callBtn);
        callBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // 클릭될 때 ShapeType을 'pressed'로 변경
                callBtn.setShapeType(1);

                // 클릭된 후에는 다시 FLAT으로 변경
                v.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        callBtn.setShapeType(0);
                    }
                }, 200);

                Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:/010-1234-1234"));
                startActivity(intent);
            }
        });

        changeMode = (NeumorphImageView) findViewById(R.id.darkMode);
        changeMode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // 클릭될 때 ShapeType을 'pressed'로 변경
                changeMode.setShapeType(1);

                // 클릭된 후에는 다시 FLAT으로 변경
                v.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        changeMode.setShapeType(0);
                    }
                }, 200);

                if (mode == 0) {
                    // 다크모드 색상 적용
                    backgroundColor = darkModeBackgroundColor;
                    main.setBackgroundColor(backgroundColor);

                    // 폰트 색상 변경
                    // 루트 뷰에서 모든 TextView를 찾아서 색상을 변경
//                    ChangeTextColor.changeDarkTextColor(main, Color.WHITE);
                    ChangeMode.applyTheme(main, mode);

                    footer_menu = (NeumorphCardView) findViewById(R.id.footer_menu);
                    footer_menu.setShadowColorDark(Color.BLACK);
                    footer_menu.setShadowColorLight(Color.GRAY);

                    mainCardView = (NeumorphCardView) findViewById(R.id.mainCardView);
                    mainCardView.setShadowColorDark(Color.BLACK);
                    mainCardView.setShadowColorLight(Color.GRAY);

                    // 관리자 메인 페이지 CardView
                    adminBtn.setShadowColorDark(Color.BLACK);
                    adminBtn.setShadowColorLight(Color.GRAY);

                    adminScheduleBtn.setShadowColorDark(Color.BLACK);
                    adminScheduleBtn.setShadowColorLight(Color.GRAY);

                    callBtn.setShadowColorDark(Color.BLACK);
                    callBtn.setShadowColorLight(Color.GRAY);

                    login.setShadowColorDark(Color.BLACK);
                    login.setShadowColorLight(Color.GRAY);

                    // 상품등록 페이지 CardView
                    input_productName = (NeumorphCardView) findViewById(R.id.input_productName);
                    input_productName.setShadowColorDark(Color.BLACK);
                    input_productName.setShadowColorLight(Color.GRAY);

                    input_productQuantity = (NeumorphCardView) findViewById(R.id.input_productQuantity);
                    input_productQuantity.setShadowColorDark(Color.BLACK);
                    input_productQuantity.setShadowColorLight(Color.GRAY);

                    input_productCategory = (NeumorphCardView) findViewById(R.id.input_productCategory);
                    input_productCategory.setShadowColorDark(Color.BLACK);
                    input_productCategory.setShadowColorLight(Color.GRAY);

                    input_productPrice = (NeumorphCardView) findViewById(R.id.input_productPrice);
                    input_productPrice.setShadowColorDark(Color.BLACK);
                    input_productPrice.setShadowColorLight(Color.GRAY);

                    createQRBtn = (NeumorphButton) findViewById(R.id.createQRBtn);
                    createQRBtn.setShadowColorDark(Color.BLACK);
                    createQRBtn.setShadowColorLight(Color.GRAY);

                    createProductBtn = (NeumorphButton) findViewById(R.id.createProductBtn);
                    createProductBtn.setShadowColorDark(Color.BLACK);
                    createProductBtn.setShadowColorLight(Color.GRAY);

                    // 이미지 색상 변경
                    adminList = (ImageView) findViewById(R.id.adminList);
                    adminList.setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_IN);

                    call = (ImageView) findViewById(R.id.call);
                    call.setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_IN);

                    adminLogin = (ImageView) findViewById(R.id.adminLogin);
                    adminLogin.setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_IN);

                    adminSchedule = (ImageView) findViewById(R.id.adminSchedule);
                    adminSchedule.setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_IN);

                    // 하단 메뉴
                    usersIcon = (NeumorphImageView) findViewById(R.id.memberBtn);
                    usersIcon.setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_IN);
                    usersIcon.setShadowColorDark(Color.BLACK);
                    usersIcon.setShadowColorLight(Color.GRAY);

                    productIcon = (NeumorphImageView) findViewById(R.id.productBtn);
                    productIcon.setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_IN);
                    productIcon.setShadowColorDark(Color.BLACK);
                    productIcon.setShadowColorLight(Color.GRAY);

                    homeIcon = (NeumorphImageView) findViewById(R.id.homeBtn);
                    homeIcon.setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_IN);
                    homeIcon.setShadowColorDark(Color.BLACK);
                    homeIcon.setShadowColorLight(Color.GRAY);

                    paymentIcon = (NeumorphImageView) findViewById(R.id.payHistoryBtn);
                    paymentIcon.setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_IN);
                    paymentIcon.setShadowColorDark(Color.BLACK);
                    paymentIcon.setShadowColorLight(Color.GRAY);

                    productRegisterIcon = (NeumorphImageView) findViewById(R.id.productPushBtn);
                    productRegisterIcon.setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_IN);
                    productRegisterIcon.setShadowColorDark(Color.BLACK);
                    productRegisterIcon.setShadowColorLight(Color.GRAY);

                    setting = (NeumorphImageView) findViewById(R.id.setting);
                    setting.setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_IN);
                    setting.setShadowColorDark(Color.BLACK);
                    setting.setShadowColorLight(Color.GRAY);

                    // Mode Image 및 색상 변경
                    changeMode.setImageResource(R.drawable.light);
                    changeMode.setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_IN);
                    changeMode.setShadowColorDark(Color.BLACK);
                    changeMode.setShadowColorLight(Color.GRAY);

                    mode++;
                } else if (mode == 1) {
                    // 라이트모드 색상 적용
                    backgroundColor = mainBackgroundColor;
                    main.setBackgroundColor(backgroundColor);
                    // 폰트 색상 변경
//                    ChangeTextColor.changeLightTextColor(main, Color.rgb(0, 105, 97));
                    ChangeMode.applyTheme(main, mode);

                    footer_menu = (NeumorphCardView) findViewById(R.id.footer_menu);
                    footer_menu.setShadowColorDark(Color.rgb(217, 217, 217));
                    footer_menu.setShadowColorLight(Color.WHITE);

                    mainCardView = (NeumorphCardView) findViewById(R.id.mainCardView);
                    mainCardView.setShadowColorDark(Color.rgb(217, 217, 217));
                    mainCardView.setShadowColorLight(Color.WHITE);

                    // 관리자 메인 페이지 cardview
                    adminBtn.setShadowColorDark(Color.rgb(217, 217, 217));
                    adminBtn.setShadowColorLight(Color.WHITE);

                    adminScheduleBtn.setShadowColorDark(Color.rgb(217, 217, 217));
                    adminScheduleBtn.setShadowColorLight(Color.WHITE);

                    callBtn.setShadowColorDark(Color.rgb(217, 217, 217));
                    callBtn.setShadowColorLight(Color.WHITE);

                    login.setShadowColorDark(Color.rgb(217, 217, 217));
                    login.setShadowColorLight(Color.WHITE);

                    // 상품등록 페이지 CardView
                    input_productName = (NeumorphCardView) findViewById(R.id.input_productName);
                    input_productName.setShadowColorDark(Color.rgb(217, 217, 217));
                    input_productName.setShadowColorLight(Color.WHITE);

                    input_productQuantity = (NeumorphCardView) findViewById(R.id.input_productQuantity);
                    input_productQuantity.setShadowColorDark(Color.rgb(217, 217, 217));
                    input_productQuantity.setShadowColorLight(Color.WHITE);

                    input_productCategory = (NeumorphCardView) findViewById(R.id.input_productCategory);
                    input_productCategory.setShadowColorDark(Color.rgb(217, 217, 217));
                    input_productCategory.setShadowColorLight(Color.WHITE);

                    input_productPrice = (NeumorphCardView) findViewById(R.id.input_productPrice);
                    input_productPrice.setShadowColorDark(Color.rgb(217, 217, 217));
                    input_productPrice.setShadowColorLight(Color.WHITE);

                    createQRBtn = (NeumorphButton) findViewById(R.id.createQRBtn);
                    createQRBtn.setBackgroundColor(Color.rgb(0, 174, 142));
                    createQRBtn.setShadowColorDark(Color.rgb(217, 217, 217));
                    createQRBtn.setShadowColorLight(Color.WHITE);

                    createProductBtn = (NeumorphButton) findViewById(R.id.createProductBtn);
                    createProductBtn.setBackgroundColor(Color.rgb(0, 174, 142));
                    createProductBtn.setShadowColorDark(Color.rgb(217, 217, 217));
                    createProductBtn.setShadowColorLight(Color.WHITE);

                    // 라이트모드에 맞는 이미지로 변경
                    adminList = (ImageView) findViewById(R.id.adminList);
                    adminList.setColorFilter(Color.BLACK, PorterDuff.Mode.SRC_IN);

                    call = (ImageView) findViewById(R.id.call);
                    call.setColorFilter(Color.BLACK, PorterDuff.Mode.SRC_IN);

                    adminLogin = (ImageView) findViewById(R.id.adminLogin);
                    adminLogin.setColorFilter(Color.BLACK, PorterDuff.Mode.SRC_IN);

                    adminSchedule = (ImageView) findViewById(R.id.adminSchedule);
                    adminSchedule.setColorFilter(Color.BLACK, PorterDuff.Mode.SRC_IN);

                    // 하단 메뉴
                    usersIcon = (NeumorphImageView) findViewById(R.id.memberBtn);
                    usersIcon.setColorFilter(Color.BLACK, PorterDuff.Mode.SRC_IN);
                    usersIcon.setShadowColorDark(Color.rgb(217, 217, 217));
                    usersIcon.setShadowColorLight(Color.WHITE);

                    productIcon = (NeumorphImageView) findViewById(R.id.productBtn);
                    productIcon.setColorFilter(Color.BLACK, PorterDuff.Mode.SRC_IN);
                    productIcon.setShadowColorDark(Color.rgb(217, 217, 217));
                    productIcon.setShadowColorLight(Color.WHITE);

                    homeIcon = (NeumorphImageView) findViewById(R.id.homeBtn);
                    homeIcon.setColorFilter(Color.BLACK, PorterDuff.Mode.SRC_IN);
                    homeIcon.setShadowColorDark(Color.rgb(217, 217, 217));
                    homeIcon.setShadowColorLight(Color.WHITE);

                    paymentIcon = (NeumorphImageView) findViewById(R.id.payHistoryBtn);
                    paymentIcon.setColorFilter(Color.BLACK, PorterDuff.Mode.SRC_IN);
                    paymentIcon.setShadowColorDark(Color.rgb(217, 217, 217));
                    paymentIcon.setShadowColorLight(Color.WHITE);

                    productRegisterIcon = (NeumorphImageView) findViewById(R.id.productPushBtn);
                    productRegisterIcon.setColorFilter(Color.BLACK, PorterDuff.Mode.SRC_IN);
                    productRegisterIcon.setShadowColorDark(Color.rgb(217, 217, 217));
                    productRegisterIcon.setShadowColorLight(Color.WHITE);

                    setting = (NeumorphImageView) findViewById(R.id.setting);
                    setting.setColorFilter(Color.BLACK, PorterDuff.Mode.SRC_IN);
                    setting.setShadowColorDark(Color.rgb(217, 217, 217));
                    setting.setShadowColorLight(Color.WHITE);

                    // Mode Image 및 색상 변경
                    changeMode.setImageResource(R.drawable.dark);
                    changeMode.setColorFilter(Color.BLACK, PorterDuff.Mode.SRC_IN);
                    changeMode.setShadowColorDark(Color.rgb(217, 217, 217));
                    changeMode.setShadowColorLight(Color.WHITE);

                    mode--;
                } else {
                    showErrorDialog(MainActivity.this, "임성준");
                }
            }
        });
        // 오자현 추가부분
        createQRBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, CreateQR.class);
                startActivity(intent); // CreateQR 액티비티를 시작합니다.
            }
        });


        createProductBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this, "서버와 연동으로 인해 약간의 딜레이가 발생할 수 있습니다.", Toast.LENGTH_SHORT).show();
                uploadFileAndSaveProductInfo();

            }
        });


        login = (NeumorphCardView) findViewById(R.id.login);
        login.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // 클릭될 때 ShapeType을 'pressed'로 변경
                login.setShapeType(1);

                // 클릭된 후에는 다시 FLAT으로 변경
                v.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        login.setShapeType(0);
                    }
                }, 200);

                // Login 페이지로 이동 및 메인페이지 배경색상 전달
                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                intent.putExtra("background_color", backgroundColor);
                intent.putExtra("mode", mode);
                startActivity(intent);
            }
        });

        memberBtn = (NeumorphImageView) findViewById(R.id.memberBtn);
        memberBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                memberBtn.setShapeType(1);

                v.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        memberBtn.setShapeType(0);
                    }
                }, 200);

                vFlipper.setDisplayedChild(1);
            }
        });

        homeBtn = (NeumorphImageView) findViewById(R.id.homeBtn);
        homeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                homeBtn.setShapeType(1);

                v.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        homeBtn.setShapeType(0);
                    }
                }, 200);

                vFlipper.setDisplayedChild(0);
            }
        });

        productBtn = (NeumorphImageView) findViewById(R.id.productBtn);
        productBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                productBtn.setShapeType(1);

                v.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        productBtn.setShapeType(0);
                    }
                }, 200);

                vFlipper.setDisplayedChild(2);
            }
        });

        adminBtn = (NeumorphCardView) findViewById(R.id.adminBtn);
        adminBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                adminBtn.setShapeType(1);

                v.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        adminBtn.setShapeType(0);
                    }
                }, 200);
            }
        });

        payHistoryBtn = (NeumorphImageView) findViewById(R.id.payHistoryBtn);
        payHistoryBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                payHistoryBtn.setShapeType(1);

                v.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        payHistoryBtn.setShapeType(0);
                    }
                }, 200);
                vFlipper.setDisplayedChild(3);
            }
        });

        productPushBtn = (NeumorphImageView) findViewById(R.id.productPushBtn);
        productPushBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                productPushBtn.setShapeType(1);

                v.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        productPushBtn.setShapeType(0);
                    }
                }, 200);
                vFlipper.setDisplayedChild(4);
            }
        });
        adminScheduleBtn = (NeumorphCardView) findViewById(R.id.adminScheduleBtn);
        adminScheduleBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                adminScheduleBtn.setShapeType(1);

                v.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        adminScheduleBtn.setShapeType(0);
                    }
                }, 200);

                // 스케쥴 관리 페이지로 이동 및 메인페이지 배경색상 전달
                Intent intent = new Intent(getApplicationContext(), AdminScheduleActivity.class);
                intent.putExtra("background_color", backgroundColor);
                startActivity(intent);
            }
        });
    }

    private ArrayList<MemberData> createFakeData() {
        ArrayList<MemberData> dataList = new ArrayList<>();
        for (int i = 1; i <= 20; i++) {
            // 가짜 데이터 생성 및 리스트에 추가
            MemberData memberData = new MemberData(i, "User" + i, new Date(), i * 100);
            dataList.add(memberData);
        }
        return dataList;
    }

    public static void showErrorDialog(Context context, String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("오류 발생")
                .setMessage(message)
                .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // 확인 버튼을 눌렀을 때 처리할 내용
                        dialog.dismiss(); // 다이얼로그를 닫습니다.
                    }
                });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    //오자현 추가부분
    void loadItemsFromFirestore() {
        FirebaseFirestore db = FirebaseFirestore.getInstance(); // 파이어베이스 인스턴스 생성
        db.collection("products").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    productList.clear(); // 기존의 리스트를 클리어
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        int code = document.getLong("productCode").intValue();
                        String productName = document.getString("productName");
                        String category = document.getString("category");
                        String imageUrl = document.getString("imageUrl");
                        int price = document.getLong("price").intValue();
                        int stock = document.getLong("stock").intValue();

                        if (imageUrl == null || imageUrl.isEmpty()) {
                            imageUrl = "R.drawable.default_image"; // 기본 이미지 URL 사용
                        }

                        Log.d("DatabaseViewActivity", "Loaded imageUrl: " + imageUrl);
                        productList.add(new Product(code, productName, category, imageUrl, price, stock)); // 리스트에 제품 추가
                    }

                    adapter2.notifyDataSetChanged(); // 데이터 변경을 어댑터에 알림
                } else {
                    Log.e("DatabaseViewActivity", "Error getting documents: ", task.getException());
                }
            }
        });
    }

    private void uploadFileAndSaveProductInfo() {
        Log.d("UploadFile", "uploadFileAndSaveProductInfo started");
        String name = editProductName.getText().toString().trim();
        String priceStr = editProductPrice.getText().toString().trim();
        String stockStr = editProductStock.getText().toString().trim();
        String category = editProductCategory.getText().toString().trim();

        if (name.isEmpty() || priceStr.isEmpty() || stockStr.isEmpty() || fileuri == null || category.isEmpty()) {
            Toast.makeText(this, "모든 필드를 채워주세요", Toast.LENGTH_SHORT).show();
            return;
        }

        int price = Integer.parseInt(priceStr);
        int stock = Integer.parseInt(stockStr);

        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReference();
        StorageReference fileRef = storageRef.child("files/" + System.currentTimeMillis());

        UploadTask uploadTask = fileRef.putFile(fileuri);
        uploadTask.addOnSuccessListener(taskSnapshot -> taskSnapshot.getStorage().getDownloadUrl().addOnSuccessListener(uri -> {
            Log.d("UploadFile", "파이어베이스업로드리스너 진입");
            String fileUrl = uri.toString();

            // Firestore에서 productCounter 문서를 업데이트하고 새 productCode를 가져옵니다.
            // Firestore에서 counters 컬렉션을 만들고  productCounter 문서를 직접 생성한다.
            // lastProductCode 필드에 초기값(예: 0)을 설정합니다. 데이터타입은 number 이 문서가 없으면 프로그램이 진행 안됨
            // 만약에 상품컬렉션을 지웠으면 이거도 관리해서 0으로 만들것(수동임)
            DocumentReference counterRef = dbFirestore.collection("counters").document("productCounter");
            dbFirestore.runTransaction(transaction -> {
                DocumentSnapshot counterSnapshot = transaction.get(counterRef);
                Long lastProductCode = counterSnapshot.getLong("lastProductCode");
                if (lastProductCode == null) lastProductCode = 0L; // 초기값 설정
                Long newProductCode = lastProductCode + 1;
                transaction.update(counterRef, "lastProductCode", newProductCode);

                // 상품 정보와 파일 URL을 Firestore에 저장합니다.
                Map<String, Object> product = new HashMap<>();
                product.put("productName", name);
                product.put("imageUrl", fileUrl);
                product.put("price", price);
                product.put("stock", stock);
                product.put("category", category);
                product.put("productCode", newProductCode); // 새로운 productCode 사용

                dbFirestore.collection("products").add(product).addOnSuccessListener(documentReference -> {
                    Toast.makeText(MainActivity.this, "상품 정보와 파일 URL 파이어베이스에 저장 성공", Toast.LENGTH_SHORT).show();
                    finishActivityWithResult();
                }).addOnFailureListener(e -> {
                    Toast.makeText(MainActivity.this, "파이어베이스 저장 실패: " + e.getMessage(), Toast.LENGTH_LONG).show();
                });

                return null;
            }).addOnFailureListener(e -> {
                Toast.makeText(MainActivity.this, "상품 코드 업데이트 실패: " + e.getMessage(), Toast.LENGTH_LONG).show();
            });
        })).addOnFailureListener(e -> {
            Toast.makeText(MainActivity.this, "파일 업로드 실패: " + e.getMessage(), Toast.LENGTH_LONG).show();
        });
    }

    // 파이어베이스에 업로드 후처리를 위한 메서드(여기선 홈으로 보내고 내용을 초기화함)
    private void finishActivityWithResult() {
        vFlipper.setDisplayedChild(0);
        imageViewProduct.setImageResource(android.R.drawable.ic_menu_camera);
        editProductName.setText("");
        editProductPrice.setText("");
        editProductStock.setText("");
        editProductCategory.setText("");
    }

    private void openFileManager() {
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("*/*"); // 모든 유형의 파일을 허용
        startActivityForResult(intent, PICK_FILE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_FILE_REQUEST && resultCode == RESULT_OK && data != null) {
            Uri selectedFileUri = data.getData();
            fileuri = data.getData();
            // ImageView에 이미지 로드
            imageViewProduct.setImageURI(selectedFileUri);
        }
    }
}
