package com.alvayonara.movieticket.ui.dashboard

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.alvayonara.movieticket.R
import androidx.lifecycle.Observer
import com.alvayonara.movieticket.data.entity.MovieEntity
import com.alvayonara.movieticket.ui.dashboard.ComingSoonAdapter.Companion.TYPE_COMING_SOON
import com.alvayonara.movieticket.utils.Preferences
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.fragment_dashboard.*
import java.text.NumberFormat
import java.util.*

class DashboardFragment : Fragment() {

    private lateinit var mDatabase: DatabaseReference
    private lateinit var preferences: Preferences

    private lateinit var dashboardViewModel: DashboardViewModel

    private lateinit var nowPlayingAdapter: NowPlayingAdapter
    private lateinit var comingSoonAdapter: ComingSoonAdapter

    private var dataList = ArrayList<MovieEntity>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_dashboard, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Initialize Firebase
        mDatabase = FirebaseDatabase.getInstance().getReference("Movie")

        // Initialize Shared Preferences
        preferences = Preferences(requireActivity())

        // Initialize View Model
        dashboardViewModel = ViewModelProvider(
            this,
            ViewModelProvider.NewInstanceFactory()
        )[DashboardViewModel::class.java]

        initView(preferences, mDatabase, dashboardViewModel)
    }

    private fun initView(
        preferences: Preferences,
        mDatabase: DatabaseReference,
        dashboardViewModel: DashboardViewModel
    ) {
        // Set name value
        tv_name.text = preferences.getValues("name")

        // Convert balance to Rupiah
        if (!preferences.getValues("balance").isNullOrEmpty()) {
            convertCurrencyBalance(preferences.getValues("balance")!!.toDouble())
        } else {
            tv_balance.text = getString(R.string.empty_balance)
        }

        if (!preferences.getValues("url").isNullOrEmpty()) {
            // Set profile photo
            Glide.with(this)
                .load(preferences.getValues("url"))
                .apply(RequestOptions.circleCropTransform())
                .into(iv_profile)
        }

        nowPlayingAdapter = NowPlayingAdapter()
        comingSoonAdapter = ComingSoonAdapter(TYPE_COMING_SOON)

        dashboardViewModel.getNowPlayingMovies(mDatabase)
            .observe(viewLifecycleOwner, Observer { movies ->
                nowPlayingAdapter.setMovies(movies)
                nowPlayingAdapter.notifyDataSetChanged()
            })

        dashboardViewModel.getUpcomingMovies(mDatabase)
            .observe(viewLifecycleOwner, Observer { movies ->
                comingSoonAdapter.setMovies(movies)
                comingSoonAdapter.notifyDataSetChanged()
            })

        with(rv_now_playing) {
            layoutManager = LinearLayoutManager(
                requireActivity(),
                LinearLayoutManager.HORIZONTAL,
                false
            )
            adapter = nowPlayingAdapter
        }

        with(rv_coming_soon) {
            layoutManager = LinearLayoutManager(requireActivity())
            adapter = comingSoonAdapter
        }
    }

    private fun convertCurrencyBalance(balance: Double) {
        val localeID = Locale("in", "ID")
        val formatRupiah = NumberFormat.getCurrencyInstance(localeID)
        tv_balance.text = (formatRupiah.format(balance))
    }
}