package com.alvayonara.movieticket.ui.ticket

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.alvayonara.movieticket.R
import com.alvayonara.movieticket.data.entity.MovieEntity
import com.alvayonara.movieticket.ui.dashboard.ComingSoonAdapter
import com.alvayonara.movieticket.ui.dashboard.ComingSoonAdapter.Companion.TYPE_TICKET
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.fragment_ticket.*
import java.util.ArrayList

class TicketFragment : Fragment() {

    private lateinit var mDatabase: DatabaseReference

    private var dataList = ArrayList<MovieEntity>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_ticket, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Initialize Firebase
        mDatabase = FirebaseDatabase.getInstance().getReference("Movie")

        // Get tickets data from Firebase
        getTicketData()
    }

    private fun getTicketData() {
        val comingSoonAdapter = ComingSoonAdapter(TYPE_TICKET)

        mDatabase.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
                Log.d("Error get ticket data: ", p0.message)
            }

            override fun onDataChange(p0: DataSnapshot) {
                for (dataSnapshot in p0.children) {
                    val movies = dataSnapshot.getValue(MovieEntity::class.java)

                    dataList.add(movies!!)
                }

                // Display total movies
                tv_total_movies.text = "${dataList.size} Movies"

                comingSoonAdapter.setMovies(dataList)

                with(rv_ticket) {
                    layoutManager = LinearLayoutManager(requireActivity())
                    adapter = comingSoonAdapter
                }
            }
        })
    }
}