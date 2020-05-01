package ir.protech21.slider.SliderTypes;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import ir.protech21.R;

public class TextSliderView extends BaseSliderView {
    public TextSliderView(Context context) {
        super(context);
    }

    public View getView() {
        View v = LayoutInflater.from(this.getContext()).inflate(R.layout.render_type_text, (ViewGroup)null);
        ImageView target = (ImageView)v.findViewById(R.id.daimajia_slider_image);
        TextView description = (TextView)v.findViewById(R.id.description);
        description.setText(this.getDescription());
        this.bindEventAndShow(v, target);
        return v;
    }
}
