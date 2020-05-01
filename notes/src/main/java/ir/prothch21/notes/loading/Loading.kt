package com.protech21.asooneh.library.loading

import android.app.Activity
import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.AnimationDrawable
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.ImageView
import androidx.core.content.ContextCompat
import ir.prothch21.notes.R
import kotlinx.android.synthetic.main.loading.*
import kotlinx.android.synthetic.main.loading.view.*
import java.util.Objects

class Loading {
    var dialog: Dialog? = null
        private set
    private var frameAnimation: AnimationDrawable? = null
    var viewGroup: (activity: Activity) -> ViewGroup = { activity -> activity.window.decorView.findViewById(android.R.id.content) }
    val view: (viewGroup: ViewGroup) -> View = { viewGroup -> LayoutInflater.from(viewGroup.context).inflate(R.layout.loading, viewGroup, false) }
    var activity: Activity? = null

    constructor(context: Activity) {
        dialog = Dialog(context)
        dialog!!.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog!!.setContentView(R.layout.loading)
        dialog?.content?.setBackgroundColor(ContextCompat.getColor(context,android.R.color.transparent))
        dialog!!.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        val imageView = dialog!!.findViewById<ImageView>(R.id.loading)
        frameAnimation = imageView.drawable as AnimationDrawable
        imageView.post { frameAnimation!!.start() }
        dialog!!.show()
    }

    constructor(activity: Activity,boolean: Boolean) {
        this.activity = activity
        val viewGroup = viewGroup(activity)
        val view = view(viewGroup)
        viewGroup.addView(view)
        val frameAnimation = view.loading.drawable as AnimationDrawable
        view.loading.post { frameAnimation.start() }
    }

    constructor(context: Activity, o: Int) {
        dialog = Dialog(context, o)
        dialog!!.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog!!.setContentView(R.layout.loading)
        dialog!!.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        val imageView = dialog!!.findViewById<ImageView>(R.id.loading)
        frameAnimation = imageView.drawable as AnimationDrawable
        imageView.post { frameAnimation!!.start() }
        dialog!!.show()

    }

    fun dismiss() {
        activity?.let {
            val viewGroup = viewGroup(it)
                viewGroup.removeViewAt(viewGroup.childCount - 1)
        }
        frameAnimation?.stop()
        dialog?.dismiss()
    }


}
