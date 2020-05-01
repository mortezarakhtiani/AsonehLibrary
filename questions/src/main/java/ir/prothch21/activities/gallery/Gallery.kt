package ir.prothch21.activities.gallery

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import ir.prothch21.R
import kotlinx.android.synthetic.main.activity_gallery.*
import java.util.*

class Gallery : AppCompatActivity() {
    var arrayList: ArrayList<GalleryModel>? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_gallery)
        val bundle = intent.extras
        arrayList = (if (bundle != null) bundle.getSerializable("gallery") else ArrayList<GalleryModel>()) as ArrayList<GalleryModel>?
        val galleryAdapter = GalleryAdapter(supportFragmentManager)
        for (galleryModel in arrayList!!) {
            galleryAdapter.addFragment(galleryModel)
        }
        viewPager.adapter = galleryAdapter
    }
}