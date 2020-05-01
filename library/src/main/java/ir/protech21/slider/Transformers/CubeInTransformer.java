package ir.protech21.slider.Transformers;
import android.view.View;


import com.nineoldandroids.view.ViewHelper;

public class CubeInTransformer extends BaseTransformer {
    public CubeInTransformer() {
    }

    protected void onTransform(View view, float position) {
        ViewHelper.setPivotX(view, position > 0.0F ? 0.0F : (float)view.getWidth());
        ViewHelper.setPivotY(view, 0.0F);
        ViewHelper.setRotation(view, -90.0F * position);
    }

    public boolean isPagingEnabled() {
        return true;
    }
}
