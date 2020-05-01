package ir.protech21.image;

import android.graphics.drawable.Drawable;

import androidx.annotation.Nullable;

import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

import ir.protech21.shimmer.ShimmerLayout;

import carbon.widget.ImageView;

public class ImageLoader implements RequestListener<Drawable> {


    private final ImageView imageView;
    private final ShimmerLayout shimmerLayout;

    public ImageLoader(ImageView imageView, ShimmerLayout shimmerLayout) {

        this.imageView = imageView;
        this.shimmerLayout = shimmerLayout;
    }


    @Override
    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
        return false;
    }

    @Override
    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
        shimmerLayout.stopShimmerAnimation();
        imageView.animate().setDuration(700).alpha(1).start();
        return false;
    }
}
