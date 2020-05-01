package ir.prothch21.management.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import ir.prothch21.management.R
import java.util.*

class Deadlines : Fragment() {
    var deadlinesModels = ArrayList<DeadlinesModel>()
    var recyclerView: RecyclerView? = null
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = LayoutInflater.from(context).inflate(R.layout.dead_lines, container, false)
        recyclerView = view.findViewById(R.id.mavaghatRecyclerView)
        //        recyclerView.setAdapter( new DeadlinesAdapter(getActivity(),deadlinesModels));
        return view
    }
}