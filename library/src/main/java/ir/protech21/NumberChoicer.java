package ir.protech21;

import android.content.Context;
import androidx.appcompat.widget.AppCompatImageView;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.Gravity;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;



public class NumberChoicer extends carbon.widget.FrameLayout {
    int number =1;
    EditText editText;
    public NumberChoicer(Context context) {
        super(context);
    }

    public NumberChoicer(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context,attrs);
    }

    public NumberChoicer(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context,attrs);
    }

    private void init(Context context,AttributeSet attrs) {
        LinearLayout linearLayout = new LinearLayout(context);
        AppCompatImageView compatImageView = new AppCompatImageView(context);
        compatImageView.setPadding(8,8,8,8);
        compatImageView.setBackgroundResource(android.R.color.holo_green_dark);
        compatImageView.setImageResource(R.drawable.ic_add_white_24dp);
        linearLayout.addView(compatImageView, FrameLayout.LayoutParams.WRAP_CONTENT, FrameLayout.LayoutParams.MATCH_PARENT);

        editText = new EditText(context);
        editText.setPadding(16,8,16,8);
        editText.addTextChangedListener(new NumberChoicer.OnTextChange());
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(0, FrameLayout.LayoutParams.MATCH_PARENT);
        layoutParams.weight=1;
        editText.setGravity(Gravity.CENTER);
        editText.setText(""+number);


        editText.setFocusableInTouchMode(false);
        editText.setClickable(false);
        editText.setOnLongClickListener(v -> {
            editText.setFocusableInTouchMode(true);
            editText.setClickable(true);
            editText.requestFocus();
            return false;
        });
        editText.setInputType(InputType.TYPE_CLASS_NUMBER);
        editText.setTextColor(getResources().getColor(R.color.colorgold));
        editText.setBackgroundColor(getResources().getColor(R.color.colorBGMain));
        linearLayout.addView(editText,layoutParams);
        AppCompatImageView imageView = new AppCompatImageView(context);
        imageView.setImageResource(R.drawable.ic_remove_black_24dp);
        imageView.setPadding(8,8,8,8);
        imageView.setBackgroundResource(R.color.mdtp_red);
        linearLayout.addView(imageView, FrameLayout.LayoutParams.WRAP_CONTENT, FrameLayout.LayoutParams.MATCH_PARENT);
        addView(linearLayout);
        compatImageView.setOnClickListener(v->{

            if (number<999){
                number++;
                editText.setText(""+number);
            }
            editText.clearFocus();
            editText.setFocusableInTouchMode(false);
            editText.setClickable(false);
        });
        imageView.setOnClickListener(v->{
            if (number>1){
                number--;
                editText.setText(""+number);
            }
            editText.clearFocus();
            editText.setFocusableInTouchMode(false);
            editText.setClickable(false);
        });
    }

    public Editable getText() {
        return editText.getText();
    }


    public class OnTextChange implements TextWatcher {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            int number = 0;
            try {
                number = Integer.parseInt(s.toString());
            }catch (Exception e){}
            if (number>999){
                s.replace(0,s.length(),""+999);
            }
            if (number<1){
                s.replace(0,s.length(),""+1);
            }
                editText.setFocusableInTouchMode(true);
                editText.setClickable(true);

            NumberChoicer.this.number=number;
        }
    }
}
