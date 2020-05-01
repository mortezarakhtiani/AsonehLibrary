package ir.protech21.slider.Tricks;


import android.os.Parcelable;
import androidx.viewpager.widget.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import ir.protech21.slider.SliderAdapter;

public class InfinitePagerAdapter extends PagerAdapter {
    private static final String TAG = "InfinitePagerAdapter";
    private static final boolean DEBUG = false;
    private SliderAdapter adapter;

    public InfinitePagerAdapter(SliderAdapter adapter) {
        this.adapter = adapter;
    }

    public SliderAdapter getRealAdapter() {
        return this.adapter;
    }

    public int getCount() {
        return 2147483647;
    }

    public int getRealCount() {
        return this.adapter.getCount();
    }

    public Object instantiateItem(ViewGroup container, int position) {
        if (this.getRealCount() == 0) {
            return null;
        } else {
            int virtualPosition = position % this.getRealCount();
            this.debug("instantiateItem: real position: " + position);
            this.debug("instantiateItem: virtual position: " + virtualPosition);
            return this.adapter.instantiateItem(container, virtualPosition);
        }
    }

    public void destroyItem(ViewGroup container, int position, Object object) {
        if (this.getRealCount() != 0) {
            int virtualPosition = position % this.getRealCount();
            this.debug("destroyItem: real position: " + position);
            this.debug("destroyItem: virtual position: " + virtualPosition);
            this.adapter.destroyItem(container, virtualPosition, object);
        }
    }

    public void finishUpdate(ViewGroup container) {
        this.adapter.finishUpdate(container);
    }

    public boolean isViewFromObject(View view, Object object) {
        return this.adapter.isViewFromObject(view, object);
    }

    public void restoreState(Parcelable bundle, ClassLoader classLoader) {
        this.adapter.restoreState(bundle, classLoader);
    }

    public Parcelable saveState() {
        return this.adapter.saveState();
    }

    public void startUpdate(ViewGroup container) {
        this.adapter.startUpdate(container);
    }

    private void debug(String message) {
    }
}
