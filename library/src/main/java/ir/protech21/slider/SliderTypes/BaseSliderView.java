package ir.protech21.slider.SliderTypes;


import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.RequestCreator;
import java.io.File;

import ir.protech21.R;

public abstract class BaseSliderView {
    protected Context mContext;
    private Bundle mBundle;
    private int mErrorPlaceHolderRes;
    private int mEmptyPlaceHolderRes;
    private String mUrl;
    private File mFile;
    private int mRes;
    protected BaseSliderView.OnSliderClickListener mOnSliderClickListener;
    private boolean mErrorDisappear;
    private BaseSliderView.ImageLoadListener mLoadListener;
    private String mDescription;
    private Picasso mPicasso;
    private BaseSliderView.ScaleType mScaleType;

    protected BaseSliderView(Context context) {
        this.mScaleType = BaseSliderView.ScaleType.Fit;
        this.mContext = context;
    }

    public BaseSliderView empty(int resId) {
        this.mEmptyPlaceHolderRes = resId;
        return this;
    }

    public BaseSliderView errorDisappear(boolean disappear) {
        this.mErrorDisappear = disappear;
        return this;
    }

    public BaseSliderView error(int resId) {
        this.mErrorPlaceHolderRes = resId;
        return this;
    }

    public BaseSliderView description(String description) {
        this.mDescription = description;
        return this;
    }

    public BaseSliderView image(String url) {
        if (this.mFile == null && this.mRes == 0) {
            this.mUrl = url;
            return this;
        } else {
            throw new IllegalStateException("Call multi image function,you only have permission to call it once");
        }
    }

    public BaseSliderView image(File file) {
        if (this.mUrl == null && this.mRes == 0) {
            this.mFile = file;
            return this;
        } else {
            throw new IllegalStateException("Call multi image function,you only have permission to call it once");
        }
    }

    public BaseSliderView image(int res) {
        if (this.mUrl == null && this.mFile == null) {
            this.mRes = res;
            return this;
        } else {
            throw new IllegalStateException("Call multi image function,you only have permission to call it once");
        }
    }

    public BaseSliderView bundle(Bundle bundle) {
        this.mBundle = bundle;
        return this;
    }

    public String getUrl() {
        return this.mUrl;
    }

    public boolean isErrorDisappear() {
        return this.mErrorDisappear;
    }

    public int getEmpty() {
        return this.mEmptyPlaceHolderRes;
    }

    public int getError() {
        return this.mErrorPlaceHolderRes;
    }

    public String getDescription() {
        return this.mDescription;
    }

    public Context getContext() {
        return this.mContext;
    }

    public BaseSliderView setOnSliderClickListener(BaseSliderView.OnSliderClickListener l) {
        this.mOnSliderClickListener = l;
        return this;
    }

    protected void bindEventAndShow(final View v, ImageView targetImageView) {
        v.setOnClickListener(v1 -> {
            if (BaseSliderView.this.mOnSliderClickListener != null) {
                BaseSliderView.this.mOnSliderClickListener.onSliderClick(BaseSliderView.this);
            }

        });
        if (targetImageView != null) {
            if (this.mLoadListener != null) {
                this.mLoadListener.onStart(this);
            }

            Picasso p = this.mPicasso != null ? this.mPicasso : Picasso.with(this.mContext);

            RequestCreator rq = null;
            if (this.mUrl != null) {
                rq = p.load(this.mUrl);
            } else if (this.mFile != null) {
                rq = p.load(this.mFile);
            } else {
                if (this.mRes == 0) {
                    return;
                }

                rq = p.load(this.mRes);
            }

            if (rq != null) {
                if (this.getEmpty() != 0) {
                    rq.placeholder(this.getEmpty());
                }

                if (this.getError() != 0) {
                    rq.error(this.getError());
                }

                switch(this.mScaleType) {
                    case Fit:
                        rq.fit();
                        break;
                    case CenterCrop:
                        rq.fit().centerCrop();
                        break;
                    case CenterInside:
                        rq.fit().centerInside();
                }

                rq.into(targetImageView, new Callback() {
                    public void onSuccess() {
                        if (v.findViewById(R.id.loading_bar) != null) {
                            v.findViewById(R.id.loading_bar).setVisibility(4);

                        }

                    }

                    public void onError() {
                        if (BaseSliderView.this.mLoadListener != null) {
                            BaseSliderView.this.mLoadListener.onEnd(false, BaseSliderView.this);
                        }

                        if (v.findViewById(R.id.loading_bar) != null) {
                            v.findViewById(R.id.loading_bar).setVisibility(4);
                        }

                    }
                });
            }
        }
    }

    public BaseSliderView setScaleType(BaseSliderView.ScaleType type) {
        this.mScaleType = type;
        return this;
    }

    public BaseSliderView.ScaleType getScaleType() {
        return this.mScaleType;
    }

    public abstract View getView();

    public void setOnImageLoadListener(BaseSliderView.ImageLoadListener l) {
        this.mLoadListener = l;
    }

    public Bundle getBundle() {
        return this.mBundle;
    }

    public Picasso getPicasso() {
        return this.mPicasso;
    }

    public void setPicasso(Picasso picasso) {
        this.mPicasso = picasso;
    }

    public interface ImageLoadListener {
        void onStart(BaseSliderView var1);

        void onEnd(boolean var1, BaseSliderView var2);
    }

    public interface OnSliderClickListener {
        void onSliderClick(BaseSliderView var1);
    }

    public static enum ScaleType {
        CenterCrop,
        CenterInside,
        Fit,
        FitCenterCrop;

        private ScaleType() {
        }
    }
}
