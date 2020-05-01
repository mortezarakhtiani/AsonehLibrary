package ir.protech21.slider;


import android.content.Context;
import androidx.viewpager.widget.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import ir.protech21.slider.SliderTypes.BaseSliderView;

import java.util.ArrayList;
import java.util.Iterator;

public class SliderAdapter extends PagerAdapter implements BaseSliderView.ImageLoadListener {
    private Context mContext;
    private ArrayList<BaseSliderView> mImageContents;

    public SliderAdapter(Context context) {
        this.mContext = context;
        this.mImageContents = new ArrayList();
    }

    public <T extends BaseSliderView> void addSlider(T slider) {
        slider.setOnImageLoadListener(this);
        this.mImageContents.add(slider);
        this.notifyDataSetChanged();
    }

    public BaseSliderView getSliderView(int position) {
        return position >= 0 && position < this.mImageContents.size() ? (BaseSliderView)this.mImageContents.get(position) : null;
    }

    public int getItemPosition(Object object) {
        return -2;
    }

    public <T extends BaseSliderView> void removeSlider(T slider) {
        if (this.mImageContents.contains(slider)) {
            this.mImageContents.remove(slider);
            this.notifyDataSetChanged();
        }

    }

    public void removeSliderAt(int position) {
        if (this.mImageContents.size() > position) {
            this.mImageContents.remove(position);
            this.notifyDataSetChanged();
        }

    }

    public void removeAllSliders() {
        this.mImageContents.clear();
        this.notifyDataSetChanged();
    }

    public int getCount() {
        return this.mImageContents.size();
    }

    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View)object);
    }

    public Object instantiateItem(ViewGroup container, int position) {
        BaseSliderView b = (BaseSliderView)this.mImageContents.get(position);
        View v = b.getView();
        container.addView(v);
        return v;
    }

    public void onStart(BaseSliderView target) {
    }

    public void onEnd(boolean result, BaseSliderView target) {
        if (target.isErrorDisappear() && !result) {
            Iterator i$ = this.mImageContents.iterator();

            while(i$.hasNext()) {
                BaseSliderView slider = (BaseSliderView)i$.next();
                if (slider.equals(target)) {
                    this.removeSlider(target);
                    break;
                }
            }

        }
    }
}
