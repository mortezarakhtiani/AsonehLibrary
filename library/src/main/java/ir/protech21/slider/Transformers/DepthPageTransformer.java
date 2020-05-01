package ir.protech21.slider.Transformers;


import android.view.View;


import com.nineoldandroids.view.ViewHelper;

public class DepthPageTransformer extends BaseTransformer {
    private static final float MIN_SCALE = 0.75F;

    public DepthPageTransformer() {
    }

    protected void onTransform(View view, float position) {
        if (position <= 0.0F) {
            ViewHelper.setTranslationX(view, 0.0F);
            ViewHelper.setScaleX(view, 1.0F);
            ViewHelper.setScaleY(view, 1.0F);
        } else if (position <= 1.0F) {
            float scaleFactor = 0.75F + 0.25F * (1.0F - Math.abs(position));
            ViewHelper.setAlpha(view, 1.0F - position);
            ViewHelper.setPivotY(view, 0.5F * (float)view.getHeight());
            ViewHelper.setTranslationX(view, (float)view.getWidth() * -position);
            ViewHelper.setScaleX(view, scaleFactor);
            ViewHelper.setScaleY(view, scaleFactor);
        }

    }

    protected boolean isPagingEnabled() {
        return true;
    }
}
