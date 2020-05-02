package com.protech21.asooneh.Actvities.notes

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.androidnetworking.AndroidNetworking
import com.androidnetworking.error.ANError
import com.androidnetworking.interfaces.OkHttpResponseAndJSONArrayRequestListener
import com.androidnetworking.interfaces.OkHttpResponseAndStringRequestListener
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.protech21.asooneh.library.loading.Loading
import ir.prothch21.notes.R
import ir.prothch21.notes.adapter.RememberAdapter
import ir.prothch21.notes.model.RememberModel
import ir.prothch21.notes.remember.Remember
import okhttp3.Response
import org.json.JSONArray
import org.json.JSONException
import java.util.*

class Notes : AppCompatActivity() {
    lateinit var recyclerView: RecyclerView
    var rememberModels = ArrayList<RememberModel>()
    lateinit var deletebutton: FloatingActionButton
    lateinit var loading: Loading
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.notes_activity)
        loading = Loading(this,true)
        deletebutton = findViewById(R.id.deletebutton)
        deletebutton.post(Runnable { deletebutton.hide() })
        recyclerView = findViewById(R.id.recyclerView)
        findViewById<View>(R.id.actionbutton).setOnClickListener { startActivityForResult(Intent(applicationContext, Remember::class.java), 2585) }
        deletebutton.setOnClickListener(View.OnClickListener { v: View? -> delete(0) })
        data
    }

    private fun delete(i: Int) {
        AndroidNetworking.post(getString(R.string.url) + "deleteRemember/").addBodyParameter("code", code!![i]).build().getAsOkHttpResponseAndString(object : OkHttpResponseAndStringRequestListener {
            override fun onResponse(okHttpResponse: Response, response: String) {
                if (i < code!!.size - 1) {
                    delete(i + 1)
                } else {
                    data
                }
            }

            override fun onError(anError: ANError) {}
        })
    }

    private val data: Unit
        get() {
            AndroidNetworking.post(getString(R.string.url)+ "getRemember/").addBodyParameter("id", getSharedPreferences("Profile", Context.MODE_PRIVATE).getString("id", null)).build().getAsOkHttpResponseAndJSONArray(object : OkHttpResponseAndJSONArrayRequestListener {
                override fun onResponse(okHttpResponse: Response, response: JSONArray) {
                    loading.dismiss()
                    rememberModels = ArrayList()
                    for (i in 0 until response.length()) {
                        try {
                            val jsonObject = response.getJSONObject(i)
                            rememberModels.add(RememberModel(jsonObject.getString("title")
                                    , jsonObject.getString("location")
                                    , jsonObject.getString("des")
                                    , jsonObject.getString("code")
                                    , jsonObject.getString("year")
                                    , jsonObject.getString("month")
                                    , jsonObject.getString("day")
                                    , jsonObject.getString("hour")
                                    , jsonObject.getString("minute")))
                        } catch (e: JSONException) {
                            e.printStackTrace()
                        }
                    }
                    val rememberAdapter = RememberAdapter(this@Notes, rememberModels)
                    recyclerView.adapter = rememberAdapter
                    rememberAdapter.setOnItemCheckedListener { position, booleans ->
                        var has = false
                        code = ArrayList()
                        for (i in booleans.indices) {
                            if (booleans[i]) {
                                has = true
                                code!!.add(rememberModels[i].code)
                            }
                        }
                        if (has) {
                            deletebutton.show()
                        } else {
                            deletebutton.hide()
                        }
                    }
//                    findViewById<View>(R.id.textNot).visibility = if (rememberModels.isEmpty()) View.VISIBLE else View.GONE
                    deletebutton.hide()
                }

                override fun onError(anError: ANError) {
                    if (rememberModels.isEmpty()) {
                        findViewById<View>(R.id.textNot).visibility = View.VISIBLE
                        deletebutton.hide()
                    }
                }
            })
        }

    var code: ArrayList<String>? = null

    override fun onActivityResult(requestCode: Int, resultCode: Int, intent: Intent?) {
        super.onActivityResult(requestCode, resultCode, intent)
        if (requestCode == 2585 && resultCode == Activity.RESULT_OK) {
            data
        }
    }

    fun Finish(view: View?) {
        finish()
    }
}