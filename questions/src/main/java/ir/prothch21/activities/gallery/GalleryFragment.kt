package ir.prothch21.activities.gallery

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.jsibbold.zoomage.ZoomageView
import com.squareup.picasso.Picasso
import ir.prothch21.R

class GalleryFragment : Fragment() {
    var zoomageView: ZoomageView? = null
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view: View = inflater.inflate(R.layout.fragment_gallery, container, false)
        zoomageView = view.findViewById(R.id.imageView)
        val bundle = arguments
        Picasso.get().load(bundle!!.getString("image")).placeholder(R.drawable.ic_logo).into(zoomageView)
        return view
    }
}