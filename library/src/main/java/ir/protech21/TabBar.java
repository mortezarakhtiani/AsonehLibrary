package ir.protech21;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.Typeface;
import android.widget.LinearLayout;
import android.widget.TextView;


import java.util.ArrayList;


public class TabBar {
    private Activity activity;
    private ArrayList<TextView> textViews = new ArrayList<>();
    private int backStackEntryCount = 0;
    public TabBar(Activity activity) {
        this.activity=activity;
    }

    public void setSize(int size, LinearLayout linearLayout) {
        for (int i = 0; i < size; i++) {
            TextView textView = new TextView(activity);
            if (i == 0) {
                textView.setText("مرحله اول");

            }
            if (i == 1) {
                textView.setText("مرحله دوم");
            }
            if (i == 2) {
                textView.setText("مرحله سوم");
            }
            if (i == 3) {
                textView.setText("مرحله چهارم");
            }
            if (i == 4) {
                textView.setText("مرحله پنجم");
            }
            if (i == 5) {
                textView.setText("مرحله ششم");
            }
            if (i == 6) {
                textView.setText("مرحله هفتم");
            }
            if (i == 7) {
                textView.setText("مرحله هشتم");
            }
            if (i == 8) {
                textView.setText("مرحله نهم");
            }
            if (i == 9) {
                textView.setText("مرحله دهم");
            }
            if (i == 10) {
                textView.setText("مرحله یازدهم");
            }
            if (i == 11) {
                textView.setText("مرحله دوازدهم");
            }
            if (i == 12) {
                textView.setText("مرحله سیزدهم");
            }
            if (i == 13) {
                textView.setText("مرحله چهاردهم");
            }
            if (i>13){
                textView.setText(String.valueOf(i));
            }
            if (i != size - 1) {
                textView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_keyboard_arrow_left_blue_24dp, 0, 0, 0);
            }
            textViews.add(textView);

        }
        for (int i = textViews.size()-1;i>=0;i--){
            textViews.get(i).setTypeface(Typeface.createFromAsset(activity.getAssets(), "fonts/iransans.ttf"));
            textViews.get(i).setTextColor(activity.getResources().getColor(R.color.colorblue));
            linearLayout.addView(textViews.get(i));
        }
        textViews.get(backStackEntryCount).setTextColor(Color.WHITE);
    }


    public void add() {
        backStackEntryCount++;
        for (int i = 0; i < textViews.size(); i++) {
            if (i == backStackEntryCount) {
                textViews.get(i).setTextColor(Color.WHITE);
            } else {
                textViews.get(i).setTextColor(activity.getResources().getColor(R.color.colorblue));
            }
        }
    }


    public void remove() {
        backStackEntryCount--;
        for (int i = 0; i < textViews.size(); i++) {
            if (i == backStackEntryCount) {
                textViews.get(i).setTextColor(Color.WHITE);
            } else {
                textViews.get(i).setTextColor(activity.getResources().getColor(R.color.colorblue));
            }
        }
    }

    public int getCount() {
        return backStackEntryCount;
    }
}
