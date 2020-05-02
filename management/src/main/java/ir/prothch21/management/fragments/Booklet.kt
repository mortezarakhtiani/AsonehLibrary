package ir.prothch21.management.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import ir.prothch21.management.R
import java.util.*

class Booklet : Fragment() {
    @SuppressLint("StaticFieldLeak")
    var recyclerViewBooklet: RecyclerView? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = LayoutInflater.from(context).inflate(R.layout.booklet, container, false)
        recyclerViewBooklet = view.findViewById(R.id.recyclerView_Booklet)
        return view
    }
}