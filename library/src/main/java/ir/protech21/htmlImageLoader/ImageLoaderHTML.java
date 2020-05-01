package ir.protech21.htmlImageLoader;

import android.graphics.drawable.Drawable;
import android.graphics.drawable.LevelListDrawable;
import androidx.annotation.Nullable;
import android.text.Html;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

import ir.protech21.R;

public class ImageLoaderHTML implements Html.ImageGetter {
    private final TextView textView;
    private Drawable empty;
    public ImageLoaderHTML(TextView textView) {
        this.textView = textView;
    }

    @Override
    public Drawable getDrawable(String source) {
        LevelListDrawable d = new LevelListDrawable();
        empty = textView.getContext().getResources().getDrawable(R.drawable.carbon_tab_text);
        d.addLevel(0, 0, empty);
        d.setBounds(0, 0, empty.getIntrinsicWidth(), empty.getIntrinsicHeight());
        Glide.with(textView.getContext()).load(source).listener(new RequestListener<Drawable>() {
            @Override
            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                return false;
            }

            @Override
            public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                d.addLevel(1, 1, resource);
                d.setBounds(0, 0, empty.getIntrinsicWidth(), empty.getIntrinsicHeight());
                d.setLevel(1);
                CharSequence t = textView.getText();
                textView.setText(t);
                return true;
            }
        }).into(100,100);
        return d;
    }
}
