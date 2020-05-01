package ir.protech21.slider.Transformers;


import android.view.View;



public class DefaultTransformer extends BaseTransformer {
    public DefaultTransformer() {
    }

    protected void onTransform(View view, float position) {
    }

    public boolean isPagingEnabled() {
        return true;
    }
}
