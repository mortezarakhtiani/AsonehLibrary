package ir.protech21.views

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.animation.Animation
import carbon.widget.LinearLayout
import ir.protech21.R
import ir.protech21.ResizeWidthAnimation
import ir.protech21.utils.DensityUtil
import kotlinx.android.synthetic.main.search_view.view.*

class SearchView : LinearLayout {

    var function: ((Boolean) -> Unit)? =null

    constructor(context: Context?) : super(context) {
        init()
    }


    constructor(context: Context?, attrs: AttributeSet?):super(context, attrs){
        init()
    }

    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int):super(context, attrs, defStyleAttr){
        init()
    }

    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int, defStyleRes: Int):super(context, attrs, defStyleAttr, defStyleRes){
        init()
    }

    fun init(){
        orientation= android.widget.LinearLayout.VERTICAL
        val view = LayoutInflater.from(context).inflate(R.layout.search_view,this,false)
        view.search_fab.tag=false
        view.search_fab.setOnClickListener {
            if (view.search_fab.tag as Boolean){
                val anim = ResizeWidthAnimation(this, DensityUtil.dip2px(context, 40f))
                anim.duration = 500
                anim.setAnimationListener(object : Animation.AnimationListener{
                    override fun onAnimationRepeat(animation: Animation?) {
                    }

                    override fun onAnimationEnd(animation: Animation?) {
                        view.searchEditText.visibility= View.GONE
                    }

                    override fun onAnimationStart(animation: Animation?) {
                        view.searchEditText.animate().alpha(0f).setDuration(100).start()
                    }
                })
               startAnimation(anim)
            }else{
                val anim = ResizeWidthAnimation(this, DensityUtil.dip2px(context, 345f))
                anim.duration = 500
                anim.setAnimationListener(object : Animation.AnimationListener{
                    override fun onAnimationRepeat(animation: Animation?) {
                    }

                    override fun onAnimationEnd(animation: Animation?) {
                        view.searchEditText.animate().alpha(1f).setDuration(100).start()
                    }

                    override fun onAnimationStart(animation: Animation?) {
                        view.searchEditText.visibility= View.VISIBLE
                    }
                })
                startAnimation(anim)
            }
            view.search_fab.tag = !(view.search_fab.tag as Boolean)
            function?.let { it1 -> it1(view.search_fab.tag as Boolean) }
        }
        addView(view)
    }

    fun setOnSlider(function: (Boolean) -> Unit) {
        this.function=function
    }


}