plugins {
    id("com.android.application")
    id("com.google.gms.google-services")
}

android {
    namespace = "kr.ac.yuhan.cs.admin"
    compileSdk = 34

    defaultConfig {
        applicationId = "kr.ac.yuhan.cs.admin"
        minSdk = 21
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    buildFeatures {
        viewBinding = true
    }
}

dependencies {
    // QR 스캔 라이브러리 - ZXing ("Zebra Crossing") 기반 QR 코드 스캐너
    implementation("com.journeyapps:zxing-android-embedded:4.3.0")
    // CircleImageView - 이미지를 원형으로 표시하는 뷰 라이브러리
    implementation("de.hdodenhof:circleimageview:3.1.0")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.11.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    implementation("androidx.annotation:annotation:1.6.0")
    implementation("androidx.lifecycle:lifecycle-livedata-ktx:2.7.0")
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.7.0")
    implementation("com.google.firebase:firebase-firestore:24.11.1")
    implementation("com.google.firebase:firebase-storage:20.3.0")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")

    // 뉴모피즘 추가
    implementation("com.github.fornewid:neumorphism:0.3.2")

    // flex
    implementation("com.google.android.flexbox:flexbox:3.0.0")


    // Glide 라이브러리 추가
    implementation("com.github.bumptech.glide:glide:4.13.2");
    annotationProcessor("com.github.bumptech.glide:compiler:4.13.2");
}