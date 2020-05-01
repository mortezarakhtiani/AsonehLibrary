package ir.protech21.slider.Transformers;


import android.view.View;

import com.nineoldandroids.view.ViewHelper;

public class ZoomOutSlideTransformer extends BaseTransformer {
    private static final float MIN_SCALE = 0.85F;
    private static final float MIN_ALPHA = 0.5F;

    public ZoomOutSlideTransformer() {
    }

    protected void onTransform(View view, float position) {
        if (position >= -1.0F || position <= 1.0F) {
            float height = (float)view.getHeight();
            float scaleFactor = Math.max(0.85F, 1.0F - Math.abs(position));
            float vertMargin = height * (1.0F - scaleFactor) / 2.0F;
            float horzMargin = (float)view.getWidth() * (1.0F - scaleFactor) / 2.0F;
            ViewHelper.setPivotY(view, 0.5F * height);
            if (position < 0.0F) {
                ViewHelper.setTranslationX(view, horzMargin - vertMargin / 2.0F);
            } else {
                ViewHelper.setTranslationX(view, -horzMargin + vertMargin / 2.0F);
            }

            ViewHelper.setScaleX(view, scaleFactor);
            ViewHelper.setScaleY(view, scaleFactor);
            ViewHelper.setAlpha(view, 0.5F + (scaleFactor - 0.85F) / 0.14999998F * 0.5F);
        }

    }
}
