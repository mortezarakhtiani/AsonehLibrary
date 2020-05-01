package ir.protech21.slider.Transformers;


import android.os.Build.VERSION;
import android.view.View;


import com.nineoldandroids.view.ViewHelper;
import ir.protech21.slider.Tricks.ViewPagerEx;

public class FlipPageViewTransformer extends BaseTransformer {
    public FlipPageViewTransformer() {
    }

    protected void onTransform(View view, float position) {
        float percentage = 1.0F - Math.abs(position);
        if (VERSION.SDK_INT >= 13) {
            view.setCameraDistance(12000.0F);
        }

        this.setVisibility(view, position);
        this.setTranslation(view);
        this.setSize(view, position, percentage);
        this.setRotation(view, position, percentage);
    }

    private void setVisibility(View page, float position) {
        if ((double)position < 0.5D && (double)position > -0.5D) {
            page.setVisibility(0);
        } else {
            page.setVisibility(4);
        }

    }

    private void setTranslation(View view) {
        ViewPagerEx viewPager = (ViewPagerEx)view.getParent();
        int scroll = viewPager.getScrollX() - view.getLeft();
        ViewHelper.setTranslationX(view, (float)scroll);
    }

    private void setSize(View view, float position, float percentage) {
        ViewHelper.setScaleX(view, position != 0.0F && position != 1.0F ? percentage : 1.0F);
        ViewHelper.setScaleY(view, position != 0.0F && position != 1.0F ? percentage : 1.0F);
    }

    private void setRotation(View view, float position, float percentage) {
        if (position > 0.0F) {
            ViewHelper.setRotationY(view, -180.0F * (percentage + 1.0F));
        } else {
            ViewHelper.setRotationY(view, 180.0F * (percentage + 1.0F));
        }

    }
}
