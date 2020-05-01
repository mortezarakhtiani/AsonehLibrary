package ir.protech21.slider.Transformers;

import android.view.View;


import com.nineoldandroids.view.ViewHelper;

public class StackTransformer extends BaseTransformer {
    public StackTransformer() {
    }

    protected void onTransform(View view, float position) {
        ViewHelper.setTranslationX(view, position < 0.0F ? 0.0F : (float)(-view.getWidth()) * position);
    }
}
