package ir.prothch21.activities.gallery

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import java.util.*

class GalleryAdapter internal constructor(fm: FragmentManager?) : FragmentPagerAdapter(fm!!,BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {
    private val fragments = ArrayList<Fragment>()
    override fun getItem(i: Int): Fragment {
        return fragments[i]
    }

    override fun getCount(): Int = fragments.size

    fun addFragment(galleryModel: GalleryModel) {
        val fragment = GalleryFragment()
        val bundle = Bundle()
        bundle.putString("image", galleryModel.link)
        bundle.putString("title", galleryModel.title)
        fragment.arguments = bundle
        fragments.add(fragment)
    }
}