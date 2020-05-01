package ir.prothch21.questions.viewHolders

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import carbon.widget.Button
import com.androidnetworking.AndroidNetworking
import com.androidnetworking.error.ANError
import com.androidnetworking.interfaces.JSONObjectRequestListener
import com.google.android.material.snackbar.Snackbar
import ir.protech21.utils.Utils
import ir.prothch21.R
import kotlinx.android.synthetic.main.object_main_linear.view.*
import org.json.JSONObject

class ButtonViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    val textViewAsk = itemView.textViewAsk
    val description = itemView.description
    val linear = itemView.linear

    fun bind(activity:AppCompatActivity,jsonObject: JSONObject) {
        val jsonArray = jsonObject.getJSONArray("answer")
        if (jsonObject.getString("question").isEmpty()) {
            (textViewAsk?.parent as ViewGroup).removeView(textViewAsk)
        }
        if (jsonObject.getString("description").isEmpty()) {
            (description?.parent as ViewGroup).removeView(description)
        }
        for (js in 0 until jsonArray.length()) {
            val json = jsonArray.getJSONObject(js)
            val button = LayoutInflater.from(itemView.context).inflate(R.layout.object_main_button, linear, false) as Button

            button.text = json.getString("answer")
            button.setOnClickListener {
                AndroidNetworking.post(itemView.context.getString(R.string.url) + "vabaste/details/").addBodyParameter("id", json.getInt("other_question_id").toString()).build().getAsJSONObject(object : JSONObjectRequestListener {
                    override fun onResponse(jsonObjects: JSONObject) {
                        val bundle = Bundle()
                        bundle.putInt("position", -1)
                        bundle.putString("questions", jsonObjects.getJSONArray("questions").toString())
                        bundle.putString("slider", jsonObjects.getJSONArray("slider").toString())
//                        val fragment = StoreDetailsFragment()
//                        fragment.arguments = bundle
//                        Utils.LoadFragment(activity.supportFragmentManager, fragment, R.id.detailsFrameLayout, "other_question")
                    }

                    override fun onError(anError: ANError?) {
                        Snackbar.make(linear!!, "Error : ${anError.toString()}", 500).show()
                    }

                })
            }
            linear?.addView(button)
        }
    }
}