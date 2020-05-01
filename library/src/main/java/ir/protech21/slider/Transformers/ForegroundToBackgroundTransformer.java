package ir.protech21.slider.Transformers;

import android.view.View;


import com.nineoldandroids.view.ViewHelper;

public class ForegroundToBackgroundTransformer extends BaseTransformer {
    public ForegroundToBackgroundTransformer() {
    }

    protected void onTransform(View view, float position) {
        float height = (float)view.getHeight();
        float width = (float)view.getWidth();
        float scale = min(position > 0.0F ? 1.0F : Math.abs(1.0F + position), 0.5F);
        ViewHelper.setScaleX(view, scale);
        ViewHelper.setScaleY(view, scale);
        ViewHelper.setPivotX(view, width * 0.5F);
        ViewHelper.setPivotY(view, height * 0.5F);
        ViewHelper.setTranslationX(view, position > 0.0F ? width * position : -width * position * 0.25F);
    }

    private static final float min(float val, float min) {
        return val < min ? min : val;
    }
}
