package ir.protech21.textChanged;

import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

public class TextChange implements TextWatcher {
    private final EditText et;
    private final String pattern;
    private final int index;
    private  Finish finish;
    private  Delete delete;
    private int len = 0;
    private int maxLength;
    private String ir;

    public TextChange(EditText editText, String pattern, int index) {
        this.et = editText;
        this.pattern = pattern;
        this.index = index;
    }


    @Override
    public void afterTextChanged(Editable s) {
        et.removeTextChangedListener(this);
        if (len < s.length()) {
            String textClear = s.toString().replace(pattern, "");
            String[] strings = textClear.split("");
            if (textClear.length()>=maxLength&&finish!=null){
                finish.finish(et);
            }
            StringBuilder stringBuilder = new StringBuilder();
            for (int i = 1; i<strings.length; i++) {
                if ((i-1)>0&& (i-1) % index == 0 ) {
                    stringBuilder.append(pattern);
                }
                stringBuilder.append(strings[i]);

            }
            et.setText(stringBuilder);
            et.setSelection(stringBuilder.toString().length());

        } else {
            if (s.toString().length()>(index-1)&&s.toString().substring(s.length()-1,s.length()).equals(pattern)){
                s.delete(s.length()-1,s.length());
            }
            if (delete!=null){
                delete.deleted(et);
            }
        }
        len = s.length();
        et.addTextChangedListener(this);
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {



    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        if (ir==null){
            return;
        }
        if (s.length()<ir.length()){
            et.setText(ir);
            et.setSelection(ir.length());
        }
        if (!et.getText().toString().substring(0, ir.length()).equals(ir)){
            et.getText().replace(0,ir.length(),ir);
        }
    }



    public void setOnFinish(int maxLength,Finish finish) {

        this.maxLength = maxLength;
        this.finish = finish;
    }

    public void setOnDelete(Delete delete) {
        this.delete = delete;
    }

    public void setStartBy(String ir) {

        this.ir = ir;
    }

    public interface Finish{
        void finish(EditText et);

    }
    public interface Delete{
        void deleted(EditText et);
    }
}