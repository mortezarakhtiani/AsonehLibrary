package ir.protech21.filepicker

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Fragment
import android.app.FragmentTransaction
import android.content.Intent
import android.os.Bundle

class FilePicker(val activity: Activity) {

    var intent: Intent? = null
    var function: ((requestCode: Int, resultCode: Int, data: Intent?) -> Unit)? = null
    var listener: ((data: Intent?) -> Unit)? = null
    var requestCode = 2585
    private var fragmentTransaction: FragmentTransaction? = null

    fun setOnResponse(intent: Intent, requestCode: Int = 2585, function: (requestCode: Int, resultCode: Int, data: Intent?) -> Unit): FilePicker {
        fragmentTransaction = activity.fragmentManager.beginTransaction()
        this.intent = intent
        this.requestCode = requestCode
        this.function = function
        val fragment = StaticFragment()
        fragmentTransaction?.add(fragment, "intent")
        fragmentTransaction?.commit()
        return this
    }

    fun setOnResponse(intent: Intent, listener: (data: Intent?) -> Unit): FilePicker {
        fragmentTransaction = activity.fragmentManager.beginTransaction()
        this.intent = intent
        this.listener = listener
        val fragment = StaticFragment()
        fragmentTransaction?.add(fragment, "intent")
        fragmentTransaction?.commit()
        return this
    }


    @SuppressLint("ValidFragment")
    inner class StaticFragment : Fragment() {
        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            startActivityForResult(intent, requestCode)
        }

        override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
            super.onActivityResult(requestCode, resultCode, data)
            function?.let { it(requestCode,resultCode,data) }
            if (resultCode == Activity.RESULT_OK && this@FilePicker.requestCode == requestCode) {
                listener?.let { it(data) }
                fragmentTransaction?.remove(this)
            }
        }
    }

}