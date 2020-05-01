package ir.protech21.views

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.view.WindowManager
import android.view.animation.Animation
import android.view.animation.Transformation
import androidx.gridlayout.widget.GridLayout
import carbon.widget.LinearLayout
import ir.protech21.R

class ExpandableGridLayout : LinearLayout {
    var expanded = true
    var duration = 0

    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs) {
        init(attrs)
    }

    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init(attrs)
    }

    private fun init(attributeSet: AttributeSet?) {
        val customValues = context.obtainStyledAttributes(attributeSet, R.styleable.ExpandableGridLayout)
        duration = customValues.getInt(R.styleable.ExpandableGridLayout_expandDuration, -1)
        expanded = customValues.getBoolean(R.styleable.ExpandableGridLayout_expanded, false)
        customValues.recycle()
    }


    fun toggle(expanded: Boolean = !this.expanded, function: (() -> Unit)? = null) {
        if (expanded)
            expandView(function)
        else
            hideView(function)
    }

    fun expandView(function: (() -> Unit)? = null) {
        if (expanded) {
            function?.let { it() }
            return
        }
        expanded = true
        measure(WindowManager.LayoutParams.MATCH_PARENT
                , MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED))
        val targetHeight = measuredHeight
        // Older versions of android (pre API 21) cancel animations for views with a height of 0.
        layoutParams.height = 0
        requestLayout()
        visibility = View.VISIBLE
        val a: Animation = object : Animation() {
            override fun applyTransformation(interpolatedTime: Float, t: Transformation) {
                layoutParams.height = if (interpolatedTime == 1f) targetHeight else (targetHeight * interpolatedTime).toInt()
                requestLayout()
            }

            override fun willChangeBounds(): Boolean {
                return true
            }
        }
        a.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationRepeat(animation: Animation?) {

            }

            override fun onAnimationEnd(animation: Animation?) {
                function?.let { it() }
            }

            override fun onAnimationStart(animation: Animation?) {

            }

        })
        if (duration == -1) a.duration = (targetHeight / context.resources.displayMetrics.density * 1.5).toLong() else a.duration = duration.toLong()
        startAnimation(a)
    }

    fun hideView(function: (() -> Unit)? = null) {
        if (!expanded) {
            function?.let { it() }
            return
        }
        expanded = false
        val initialHeight = measuredHeight
        val a: Animation = object : Animation() {
            override fun applyTransformation(interpolatedTime: Float, t: Transformation) {
                if (interpolatedTime == 1f) {
                    visibility = View.GONE
                } else {
                    layoutParams.height = initialHeight - (initialHeight * interpolatedTime).toInt()
                    requestLayout()
                }
            }

            override fun willChangeBounds(): Boolean {
                return true
            }
        }
        a.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationRepeat(animation: Animation?) {

            }

            override fun onAnimationEnd(animation: Animation?) {
                function?.let { it() }
            }

            override fun onAnimationStart(animation: Animation?) {

            }

        })
        if (duration == -1) a.duration = (initialHeight / context.resources.displayMetrics.density * 1.5).toLong() else a.duration = duration.toLong()
        startAnimation(a)
    }
}