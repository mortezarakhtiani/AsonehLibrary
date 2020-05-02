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
import com.androidnetworking.interfaces.ParsedRequestListener
import com.google.android.material.floatingactionbutton.FloatingActionButton
import ir.protech21.NavigationTabStrip2
import ir.prothch21.management.adapter.ViewPagerAdapter
import ir.prothch21.management.fragments.*
import ir.prothch21.management.models.Management
import kotlinx.android.synthetic.main.mangement.*
import okhttp3.Response
import org.json.JSONArray
import org.json.JSONException
import java.util.*
import kotlin.collections.ArrayList

class ManagementActivity : AppCompatActivity() {
    var adapter: ViewPagerAdapter? = null
    lateinit var booklet: Booklet
    lateinit var deadlines: Deadlines



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.mangement)
        //        drawer(this);
        adapter = ViewPagerAdapter(supportFragmentManager, this, viewPager)
        booklet = Booklet()
        adapter!!.addFrag(booklet)
        deadlines = Deadlines()
        adapter!!.addFrag(deadlines)
        viewPager!!.adapter = adapter
        nts_center!!.onTabStripSelectedIndexListener = object : NavigationTabStrip2.OnTabStripSelectedIndexListener {
            override fun onStartTabSelected(title: String, index: Int) {
                viewPager!!.currentItem = index
            }

            override fun onEndTabSelected(title: String, index: Int) {}
        }
        AndroidNetworking.post(getString(R.string.url) + "getAqsat/")
                .addBodyParameter("id", getSharedPreferences("Profile", Context.MODE_PRIVATE).getString("id", null))
                .build().getAsObject(Management::class.java, object : ParsedRequestListener<Management> {
            override fun onResponse(response: Management) {
                booklet.recyclerViewBooklet?.adapter = BookletAdapter(this@ManagementActivity, ArrayList(response.all))
                deadlines.recyclerView?.adapter = DeadlinesAdapter(deadlines, ArrayList(response.timeless))
            }

            override fun onError(anError: ANError?) {

            }
        })
        nts_center!!.setViewPager(viewPager)
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
            AndroidNetworking.post(getString(R.string.url) + "getAqsat/").addBodyParameter("id", getSharedPreferences("Profile", Context.MODE_PRIVATE).getString("id", null))
                    .build().getAsObject(Management::class.java, object : ParsedRequestListener<Management> {
                override fun onResponse(response: Management) {
                    booklet.recyclerViewBooklet?.adapter = BookletAdapter(this@ManagementActivity, ArrayList(response.all))
                    deadlines.recyclerView?.adapter = DeadlinesAdapter(deadlines, deadlines.deadlinesModels)
                }

                override fun onError(anError: ANError?) {

                }
            })
        }
    }



    fun Finish(view: View?) {
        finish()
    }
}