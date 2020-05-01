package ir.protech21.slider.Transformers;



import android.graphics.Camera;
import android.graphics.Matrix;
import android.view.View;

import com.nineoldandroids.view.ViewHelper;

public class TabletTransformer extends BaseTransformer {
    private static final Matrix OFFSET_MATRIX = new Matrix();
    private static final Camera OFFSET_CAMERA = new Camera();
    private static final float[] OFFSET_TEMP_FLOAT = new float[2];

    public TabletTransformer() {
    }

    protected void onTransform(View view, float position) {
        float rotation = (position < 0.0F ? 30.0F : -30.0F) * Math.abs(position);
        ViewHelper.setTranslationX(view, getOffsetXForRotation(rotation, view.getWidth(), view.getHeight()));
        ViewHelper.setPivotX(view, (float)view.getWidth() * 0.5F);
        ViewHelper.setPivotY(view, 0.0F);
        ViewHelper.setRotationY(view, rotation);
    }

    protected static final float getOffsetXForRotation(float degrees, int width, int height) {
        OFFSET_MATRIX.reset();
        OFFSET_CAMERA.save();
        OFFSET_CAMERA.rotateY(Math.abs(degrees));
        OFFSET_CAMERA.getMatrix(OFFSET_MATRIX);
        OFFSET_CAMERA.restore();
        OFFSET_MATRIX.preTranslate((float)(-width) * 0.5F, (float)(-height) * 0.5F);
        OFFSET_MATRIX.postTranslate((float)width * 0.5F, (float)height * 0.5F);
        OFFSET_TEMP_FLOAT[0] = (float)width;
        OFFSET_TEMP_FLOAT[1] = (float)height;
        OFFSET_MATRIX.mapPoints(OFFSET_TEMP_FLOAT);
        return ((float)width - OFFSET_TEMP_FLOAT[0]) * (degrees > 0.0F ? 1.0F : -1.0F);
    }
}
