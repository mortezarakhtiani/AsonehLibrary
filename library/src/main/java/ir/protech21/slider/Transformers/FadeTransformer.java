package ir.protech21.slider.Transformers;

import android.view.View;


import com.nineoldandroids.view.ViewHelper;

public class FadeTransformer extends BaseTransformer {
    public FadeTransformer() {
    }

    protected void onTransform(View view, float position) {
        if (position >= -1.0F && position <= 1.0F) {
            if (position > 0.0F && position > 1.0F) {
                if (position == 0.0F) {
                    ViewHelper.setAlpha(view, 1.0F);
                }
            } else {
                float alpha = position <= 0.0F ? position + 1.0F : 1.0F - position;
                ViewHelper.setAlpha(view, alpha);
            }
        } else {
            ViewHelper.setAlpha(view, 0.6F);
        }

    }
}
