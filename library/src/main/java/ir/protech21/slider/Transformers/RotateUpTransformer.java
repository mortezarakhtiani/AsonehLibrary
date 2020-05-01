package ir.protech21.slider.Transformers;

import android.view.View;


import com.nineoldandroids.view.ViewHelper;

public class RotateUpTransformer extends BaseTransformer {
    private static final float ROT_MOD = -15.0F;

    public RotateUpTransformer() {
    }

    protected void onTransform(View view, float position) {
        float width = (float)view.getWidth();
        float rotation = -15.0F * position;
        ViewHelper.setPivotX(view, width * 0.5F);
        ViewHelper.setPivotY(view, 0.0F);
        ViewHelper.setTranslationX(view, 0.0F);
        ViewHelper.setRotation(view, rotation);
    }

    protected boolean isPagingEnabled() {
        return true;
    }
}
