package ir.protech21.slider.Transformers;

import android.view.View;

import com.nineoldandroids.view.ViewHelper;

public class ZoomInTransformer extends BaseTransformer {
    public ZoomInTransformer() {
    }

    protected void onTransform(View view, float position) {
        float scale = position < 0.0F ? position + 1.0F : Math.abs(1.0F - position);
        ViewHelper.setScaleX(view, scale);
        ViewHelper.setScaleY(view, scale);
        ViewHelper.setPivotX(view, (float)view.getWidth() * 0.5F);
        ViewHelper.setPivotY(view, (float)view.getHeight() * 0.5F);
        ViewHelper.setAlpha(view, position >= -1.0F && position <= 1.0F ? 1.0F - (scale - 1.0F) : 0.0F);
    }
}
