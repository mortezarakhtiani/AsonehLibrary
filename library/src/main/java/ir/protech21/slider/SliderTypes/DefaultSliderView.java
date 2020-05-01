package ir.protech21.slider.SliderTypes;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import ir.protech21.R;

public class DefaultSliderView extends BaseSliderView {
    public DefaultSliderView(Context context) {
        super(context);
    }

    public View getView() {
        View v = LayoutInflater.from(this.getContext()).inflate(R.layout.render_type_default, (ViewGroup)null);
        ImageView target = (ImageView)v.findViewById(R.id.daimajia_slider_image);
        this.bindEventAndShow(v, target);
        return v;
    }
}