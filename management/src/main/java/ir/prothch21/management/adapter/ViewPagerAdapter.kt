package ir.prothch21.management.adapter

import android.content.Context
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import androidx.viewpager.widget.PagerAdapter
import androidx.viewpager.widget.ViewPager
import ir.protech21.tablayout.viewpager.CustomViewPager
import java.util.*

/**
 * Created by DAT on 8/16/2015.
 */
class ViewPagerAdapter(manager: FragmentManager?, var context: Context?, private val viewPager: ViewPager?) : FragmentStatePagerAdapter(manager!!) {
    private val mFragmentList = ArrayList<Fragment>()
    override fun getItem(position: Int): Fragment {
        return mFragmentList[position]
    }

    override fun getCount(): Int = mFragmentList.size

    fun addFrag(fragment: Array<Fragment>, a: Int) {
        mFragmentList.clear()
        mFragmentList.addAll(listOf(*fragment))
    }

    fun addFrag(fragment: Fragment) {
        mFragmentList.add(fragment)
    }

    private fun removeFrag(position: Int) {
        val fragment = mFragmentList[position]
        mFragmentList.remove(fragment)
        destroyFragmentView(viewPager, position, fragment)
        notifyDataSetChanged()
    }

    private fun destroyFragmentView(container: ViewGroup?, position: Int, `object`: Any) {
        val manager = (`object` as Fragment).fragmentManager
        val trans = manager!!.beginTransaction()
        trans.remove(`object`)
        trans.commit()
    }

    override fun getItemPosition(`object`: Any): Int {
        return PagerAdapter.POSITION_NONE
    }

    fun getFrag(position: Int): Fragment {
        return mFragmentList[position]
    }

}