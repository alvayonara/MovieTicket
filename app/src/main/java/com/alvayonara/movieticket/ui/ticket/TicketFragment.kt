package com.alvayonara.movieticket.ui.ticket

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
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

    private lateinit var ticketViewModel: TicketViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_ticket, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Initialize Firebase
        mDatabase = FirebaseDatabase.getInstance().getReference("Movie")

        ticketViewModel = ViewModelProvider(
            this,
            ViewModelProvider.NewInstanceFactory()
        )[TicketViewModel::class.java]

        // Get tickets data from Firebase
        getTicketData()
    }

    private fun getTicketData() {
        val comingSoonAdapter = ComingSoonAdapter(TYPE_TICKET)

        ticketViewModel.getTickets(mDatabase).observe(requireActivity(), Observer { tickets ->
            comingSoonAdapter.setMovies(tickets)
            comingSoonAdapter.notifyDataSetChanged()

            // Display total movies
            tv_total_movies.text = "${tickets.size} Movies"
        })

        with(rv_ticket) {
            layoutManager = LinearLayoutManager(requireActivity())
            adapter = comingSoonAdapter
        }
    }
}