package ir.prothch21.management

import android.os.Bundle
import android.widget.HorizontalScrollView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import ir.prothch21.management.fragments.booklet.Booklet
import ir.prothch21.management.fragments.booklet.Booklet2
import java.util.*

class BookletAdd : AppCompatActivity() {
    var fragmentTransaction: FragmentTransaction? = null
    var fragmentManager: FragmentManager? = null
    var linearLayout: LinearLayout? = null
    var scrollView: HorizontalScrollView? = null
    var count = 0
    var fragments = ArrayList<Fragment>()
    var textViews = ArrayList<TextView>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.add_booklet)
        findViewByIds()
        fragmentManager = supportFragmentManager
        parsJson(null)
        addFragment()
    }

    private fun findViewByIds() {
        linearLayout = findViewById(R.id.linearLayout)
        scrollView = findViewById(R.id.r)
    }

    var booklet: Booklet? = null
    var booklet2: Booklet2? = null
    fun parsJson(json: String?) {
        booklet = Booklet()
        booklet2 = Booklet2()
        fragments.add(booklet!!)
        fragments.add(booklet2!!)
    }

    private fun addFragment() {
        count = fragmentManager!!.backStackEntryCount
        fragmentTransaction = fragmentManager!!.beginTransaction()
        fragmentTransaction!!.replace(R.id.detailsFrameLayout, fragments[count], "demofragment")
        fragmentTransaction!!.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_CLOSE)
        fragmentTransaction!!.commit()
    } //
}