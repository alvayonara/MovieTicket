package com.alvayonara.movieticket.ui.settings

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.alvayonara.movieticket.R
import com.alvayonara.movieticket.ui.sign.signin.SignInActivity
import com.alvayonara.movieticket.utils.Preferences
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.fragment_dashboard.*
import kotlinx.android.synthetic.main.fragment_settings.*
import kotlinx.android.synthetic.main.fragment_settings.iv_profile
import kotlinx.android.synthetic.main.fragment_settings.tv_name

class SettingsFragment : Fragment() {

    private lateinit var auth: FirebaseAuth

    private lateinit var preferences: Preferences

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_settings, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Initialize Firebase Auth
        auth = FirebaseAuth.getInstance();

        // Initialize Shared Preferences
        preferences = Preferences(requireActivity())

        initView()
    }

    private fun initView() {
        if (!preferences.getValues("url").isNullOrEmpty()) {
            // Set profile photo
            Glide.with(this)
                .load(preferences.getValues("url"))
                .apply(RequestOptions.circleCropTransform())
                .into(iv_profile)
        }

        tv_name.text = preferences.getValues("name")
        tv_email.text = preferences.getValues("email")

        tv_sign_out.setOnClickListener {
            auth.signOut()
            preferences.clearAllData()

            val intent = Intent(requireActivity(), SignInActivity::class.java)
            startActivity(intent)

            activity?.finish()
        }
    }
}