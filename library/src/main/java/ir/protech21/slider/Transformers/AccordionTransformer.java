package ir.protech21.slider.Transformers;


import android.view.View;


import com.nineoldandroids.view.ViewHelper;

public class AccordionTransformer extends BaseTransformer {
    public AccordionTransformer() {
    }

    protected void onTransform(View view, float position) {
        ViewHelper.setPivotX(view, position < 0.0F ? 0.0F : (float)view.getWidth());
        ViewHelper.setScaleX(view, position < 0.0F ? 1.0F + position : 1.0F - position);
    }
}
