package ir.protech21.bottomsheet

import android.app.Dialog
import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.Typeface
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.Gravity
import android.view.View
import androidx.core.content.ContextCompat
import androidx.gridlayout.widget.GridLayout
import carbon.widget.TextView
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import ir.protech21.R
import kotlinx.android.synthetic.main.list_dialog_city.view.*
import java.util.*

class BottomSheetFragment : BottomSheetDialogFragment() {
    //    private var cityGridLayout: GridLayout? = null
//    private var title: TextView? = null
    var selectedItemPosition = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.CustomBottomSheetDialogTheme);
    }


    override fun setupDialog(dialog: Dialog, style: Int) {
        val view = View.inflate(context, R.layout.list_dialog_city, null)
        dialog.setContentView(view)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        view.city_grid_layout!!.columnCount = columnCount
        view.title!!.text = stringtitle
        if (strings != null) {
            for (i in strings!!.indices) {
                val textView = textView(strings!![i])
                textView.setOnClickListener {
                    dismiss()
                    interFace?.let { it(i) }
                    selectedItemPosition = i
                }
                val params = GridLayout.LayoutParams()
                params.columnSpec = GridLayout.spec(GridLayout.UNDEFINED, 1f)
                view.city_grid_layout!!.addView(textView, params)
            }
        }
        (view.parent as View).setBackgroundColor(Color.TRANSPARENT)
    }


    fun textView(s: String?): TextView {
        val textView = TextView(context)
        textView.setBackgroundColor(ContextCompat.getColor(context!!,R.color.colorBGMain))
        textView.cornerRadius=5f
        textView.strokeWidth=1f
        textView.stroke= ColorStateList.valueOf(ContextCompat.getColor(context!!,R.color.stroke))
        textView.gravity = Gravity.CENTER
        textView.typeface = Typeface.createFromAsset(activity!!.assets, "fonts/iransans.ttf")
        textView.text = s
        textView.setPadding(8, 8, 8, 8)
        textView.setTextColor(Color.WHITE)
        return textView
    }

    fun setOnItemClickListener(function: ((Int) -> Unit)? = null) {
        interFace = function
    }

    val selectedItem: String?
        get() = if (selectedItemPosition == -1) {
            null
        } else strings!![selectedItemPosition]

    companion object {
        private var stringtitle: String? = null
        private var strings: ArrayList<String>? = null
        private var interFace: ((Int) -> Unit)? = null
        var columnCount:Int = 2
        fun newInstance(strings: ArrayList<String>?, title: String = "یکی از آیتم ها را انتخاب کنید",columnCount:Int=2, function: ((Int) -> Unit)? = null): BottomSheetFragment {
            Companion.strings = strings
            stringtitle = title
            interFace = function
            BottomSheetFragment.columnCount=columnCount
            return BottomSheetFragment()
        }


    }
}