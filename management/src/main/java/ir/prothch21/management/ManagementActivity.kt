package ir.prothch21.management

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.viewpager.widget.ViewPager
import com.androidnetworking.AndroidNetworking
import com.androidnetworking.error.ANError
import com.androidnetworking.interfaces.OkHttpResponseAndJSONArrayRequestListener
import com.google.android.material.floatingactionbutton.FloatingActionButton
import ir.protech21.NavigationTabStrip2
import ir.prothch21.management.adapter.ViewPagerAdapter
import ir.prothch21.management.fragments.*
import okhttp3.Response
import org.json.JSONArray
import org.json.JSONException
import java.util.*

class ManagementActivity : AppCompatActivity() {
    var NavigationTabStrip2: NavigationTabStrip2? = null
    var viewPager: ViewPager? = null
    var adapter: ViewPagerAdapter? = null
    var booklet: Booklet? = null
    var deadlines: Deadlines? = null
    var toolbar: Toolbar? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.mangement)
        findViewByIds()
        //        drawer(this);
        adapter = ViewPagerAdapter(supportFragmentManager, this, viewPager)
        booklet = Booklet()
        adapter!!.addFrag(booklet!!)
        deadlines = Deadlines()
        adapter!!.addFrag(deadlines!!)
        viewPager!!.adapter = adapter
        NavigationTabStrip2!!.onTabStripSelectedIndexListener = object : NavigationTabStrip2.OnTabStripSelectedIndexListener {
            override fun onStartTabSelected(title: String, index: Int) {
                viewPager!!.currentItem = index
            }

            override fun onEndTabSelected(title: String, index: Int) {}
        }
        AndroidNetworking.post(getString(R.string.url) + "getAqsat/").addBodyParameter("id", getSharedPreferences("Profile", Context.MODE_PRIVATE).getString("id", null)).build().getAsOkHttpResponseAndJSONArray(object : OkHttpResponseAndJSONArrayRequestListener {
            override fun onResponse(okHttpResponse: Response, response: JSONArray) {
                booklet!!.bookletModels = ArrayList()
                deadlines!!.deadlinesModels = ArrayList()
                try {
                    val `object` = response.getJSONObject(0)
                    val jsonArray = JSONArray(`object`.getString("page"))
                    for (i in 0 until jsonArray.length()) {
                        val jsonObject = jsonArray.getJSONObject(i)
                        booklet!!.bookletModels?.add(BookletModel(jsonObject.getString("title"), jsonObject.getString("type"), jsonObject.getString("logicalNumber"), jsonObject.getString("pay"), jsonObject.getString("notpay"), jsonObject.getString("timeless"), jsonObject.getString("code"), jsonObject.getString("payPrice"), jsonObject.getString("notpayPrice"), jsonObject.getString("moavagPrice")))
                    }
                    booklet!!.recyclerViewBooklet?.adapter = BookletAdapter(this@ManagementActivity, booklet!!.bookletModels!!)
                    val o = response.getJSONObject(1)
                    val array = JSONArray(o.getString("page"))
                    for (i in 0 until array.length()) {
                        val jsonObject = array.getJSONObject(i)
                        deadlines!!.deadlinesModels.add(DeadlinesModel(jsonObject.getString("id"), jsonObject.getString("price"), jsonObject.getString("title"), jsonObject.getString("type"), jsonObject.getString("deadtime")))
                    }
                    deadlines!!.recyclerView?.adapter = DeadlinesAdapter(deadlines!!, deadlines!!.deadlinesModels)
                } catch (e: JSONException) {
                    e.printStackTrace()
                }
            }

            override fun onError(anError: ANError) {}
        })
        NavigationTabStrip2!!.setViewPager(viewPager)
        val FloatingActionButton = findViewById<FloatingActionButton>(R.id.FloatingActionButton)
        FloatingActionButton.setOnClickListener {
            if (viewPager!!.currentItem == 0) {
                val intent = Intent(applicationContext, BookletAdd::class.java)
                startActivityForResult(intent, 0)
            } else {
            }
        }
    }

    public override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 0 && resultCode == Activity.RESULT_OK) {
            AndroidNetworking.post(getString(R.string.url) + "getAqsat/").addBodyParameter("id", getSharedPreferences("Profile", Context.MODE_PRIVATE).getString("id", null)).build().getAsOkHttpResponseAndJSONArray(object : OkHttpResponseAndJSONArrayRequestListener {
                override fun onResponse(okHttpResponse: Response, response: JSONArray) {
                    booklet!!.bookletModels = ArrayList()
                    deadlines!!.deadlinesModels = ArrayList()
                    try {
                        val `object` = response.getJSONObject(0)
                        val jsonArray = JSONArray(`object`.getString("page"))
                        for (i in 0 until jsonArray.length()) {
                            val jsonObject = jsonArray.getJSONObject(i)
                            booklet!!.bookletModels?.add(BookletModel(jsonObject.getString("title"), jsonObject.getString("type"), jsonObject.getString("logicalNumber"), jsonObject.getString("pay"), jsonObject.getString("notpay"), jsonObject.getString("timeless"), jsonObject.getString("code"), jsonObject.getString("payPrice"), jsonObject.getString("notpayPrice"), jsonObject.getString("moavagPrice")))
                        }
                        booklet!!.recyclerViewBooklet?.adapter = BookletAdapter(this@ManagementActivity, booklet!!.bookletModels!!)
                        val o = response.getJSONObject(1)
                        val array = JSONArray(o.getString("page"))
                        for (i in 0 until array.length()) {
                            val jsonObject = array.getJSONObject(i)
                            deadlines!!.deadlinesModels.add(DeadlinesModel(jsonObject.getString("id"), jsonObject.getString("price"), jsonObject.getString("title"), jsonObject.getString("type"), jsonObject.getString("deadtime")))
                        }
                        deadlines!!.recyclerView?.adapter = DeadlinesAdapter(deadlines!!, deadlines!!.deadlinesModels)
                    } catch (e: JSONException) {
                        e.printStackTrace()
                    }
                }

                override fun onError(anError: ANError) {}
            })
        }
    }

    private fun findViewByIds() {
        viewPager = findViewById(R.id.viewPager)
        NavigationTabStrip2 = findViewById(R.id.nts_center)
    }

    fun Finish(view: View?) {
        finish()
    }
}