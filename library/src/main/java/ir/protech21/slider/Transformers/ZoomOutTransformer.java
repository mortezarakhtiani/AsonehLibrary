package ir.protech21.slider.Transformers;
import android.view.View;

import com.nineoldandroids.view.ViewHelper;

public class ZoomOutTransformer extends BaseTransformer {
    public ZoomOutTransformer() {
    }

    protected void onTransform(View view, float position) {
        float scale = 1.0F + Math.abs(position);
        ViewHelper.setScaleX(view, scale);
        ViewHelper.setScaleY(view, scale);
        ViewHelper.setPivotX(view, (float)view.getWidth() * 0.5F);
        ViewHelper.setPivotY(view, (float)view.getWidth() * 0.5F);
        ViewHelper.setAlpha(view, position >= -1.0F && position <= 1.0F ? 1.0F - (scale - 1.0F) : 0.0F);
        if ((double)position < -0.9D) {
            ViewHelper.setTranslationX(view, (float)view.getWidth() * position);
        }

    }
}
