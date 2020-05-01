package ir.protech21.slider.Transformers;

import android.view.View;


import com.nineoldandroids.view.ViewHelper;

public class FlipHorizontalTransformer extends BaseTransformer {
    public FlipHorizontalTransformer() {
    }

    protected void onTransform(View view, float position) {
        float rotation = 180.0F * position;
        ViewHelper.setAlpha(view, rotation <= 90.0F && rotation >= -90.0F ? 1.0F : 0.0F);
        ViewHelper.setPivotY(view, (float)view.getHeight() * 0.5F);
        ViewHelper.setPivotX(view, (float)view.getWidth() * 0.5F);
        ViewHelper.setRotationY(view, rotation);
    }
}
