package kr.ac.yuhan.cs.admin.util;

import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

public class ChangeMode {
    public static void applyTheme(View rootView, int mode) {
        int textColor;
        int buttonColor;

        if (mode == 0) {
            // 다크 모드
            textColor = Color.WHITE;
            buttonColor = Color.BLACK;
        } else {
            // 라이트 모드
            textColor = Color.rgb(0, 105, 97);
            buttonColor = Color.rgb(0, 174, 142);
        }

        setTextColor(rootView, textColor);
        setButtonColor(rootView, buttonColor);
    }

    private static void setTextColor(View view, int color) {
        if (view instanceof TextView) {
            ((TextView) view).setTextColor(color);
        } else if (view instanceof ViewGroup) {
            ViewGroup viewGroup = (ViewGroup) view;
            for (int i = 0; i < viewGroup.getChildCount(); i++) {
                setTextColor(viewGroup.getChildAt(i), color);
            }
        }
    }

    private static void setButtonColor(View view, int color) {
        if (view instanceof Button) {
            ((Button) view).setBackgroundColor(color);
        } else if (view instanceof ViewGroup) {
            ViewGroup viewGroup = (ViewGroup) view;
            for (int i = 0; i < viewGroup.getChildCount(); i++) {
                setButtonColor(viewGroup.getChildAt(i), color);
            }
        }
    }
}
