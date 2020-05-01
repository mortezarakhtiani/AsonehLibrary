package ir.protech21.filepicker

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Fragment
import android.content.Intent
import android.os.Bundle

class FilePicker(val activity: Activity) {

    var intent: Intent? = null
    var function: ((Intent?) -> Unit)? = null
    fun setOnResponse(intent: Intent, function: (Intent?) -> Unit): FilePicker {
        val fragmentTransaction = activity.fragmentManager.beginTransaction()
        this.intent = intent
        this.function = function
        val fragment = StaticFragment()
        fragmentTransaction?.add(fragment, "getusername")
        fragmentTransaction.commit()
        return this
    }

    @SuppressLint("ValidFragment")
    inner class StaticFragment : Fragment() {
        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            startActivityForResult(intent, 2585)
        }

        override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
            super.onActivityResult(requestCode, resultCode, data)
            if (resultCode == Activity.RESULT_OK && requestCode == 2585) {
                function?.let { it(data) }
            }

        }
    }

}