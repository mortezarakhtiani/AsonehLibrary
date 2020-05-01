package ir.protech21.slider.Transformers;

import android.view.View;


import com.nineoldandroids.view.ViewHelper;

public class RotateDownTransformer extends BaseTransformer {
    private static final float ROT_MOD = -15.0F;

    public RotateDownTransformer() {
    }

    protected void onTransform(View view, float position) {
        float width = (float)view.getWidth();
        float height = (float)view.getHeight();
        float rotation = -15.0F * position * -1.25F;
        ViewHelper.setPivotX(view, width * 0.5F);
        ViewHelper.setPivotY(view, height);
        ViewHelper.setRotation(view, rotation);
    }

    protected boolean isPagingEnabled() {
        return true;
    }
}
